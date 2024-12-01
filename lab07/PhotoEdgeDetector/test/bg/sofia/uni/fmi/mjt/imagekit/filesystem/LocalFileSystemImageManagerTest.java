package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class LocalFileSystemImageManagerTest {

    private LocalFileSystemImageManager imageManager;
    private File mockDir;
    private File mockFile;

    @BeforeEach
    void setup() {
        imageManager = new LocalFileSystemImageManager();
        mockDir = mock(File.class);
        mockFile = mock(File.class);
    }

    @Test
    void testLoadImageWithValidFile() throws IOException {
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.getName()).thenReturn("test.png");

        BufferedImage mockImage = mock(BufferedImage.class);
        try (var mockedStaticImageIO = mockStatic(ImageIO.class)) {
            mockedStaticImageIO.when(() -> ImageIO.read(mockFile)).thenReturn(mockImage);

            BufferedImage image = imageManager.loadImage(mockFile);
            assertNotNull(image);
            assertSame(mockImage, image);
        }
    }

    @Test
    void testLoadImageWithNullImageThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> imageManager.loadImage(null));
    }

    @Test
    void testLoadImageWithNonExistentThrowsIOException() {
        File imageFile = mock(File.class);
        when(imageFile.exists()).thenReturn(false);
        assertThrows(IOException.class, () -> imageManager.loadImage(imageFile));
    }

    @Test
    void testLoadImageWithInvalidFileThrowsIOException() {
        File imageFile = mock(File.class);
        when(imageFile.isFile()).thenReturn(false);
        assertThrows(IOException.class, () -> imageManager.loadImage(imageFile));
    }

    @Test
    void testLoadImageWithUnsupportedFileFormatThrowsIOException() {
        when(mockFile.getName()).thenReturn("image.txt");
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);

        assertThrows(IOException.class, () -> imageManager.loadImage(mockFile));
    }

    @Test
    void testLoadImagesFromValidDirectory() throws IOException {
        File[] mockFiles = {mock(File.class), mock(File.class)};
        when(mockDir.exists()).thenReturn(true);
        when(mockDir.isDirectory()).thenReturn(true);
        when(mockDir.listFiles()).thenReturn(mockFiles);

        when(mockFiles[0].isFile()).thenReturn(true);
        when(mockFiles[0].exists()).thenReturn(true);
        when(mockFiles[0].getName()).thenReturn("image1.jpg");

        when(mockFiles[1].isFile()).thenReturn(true);
        when(mockFiles[1].exists()).thenReturn(true);
        when(mockFiles[1].getName()).thenReturn("image2.png");

        try (var mockedStaticImageIO = mockStatic(ImageIO.class)) {
            mockedStaticImageIO.when(() -> ImageIO.read(mockFiles[0])).thenReturn(mock(BufferedImage.class));
            mockedStaticImageIO.when(() -> ImageIO.read(mockFiles[1])).thenReturn(mock(BufferedImage.class));

            List<BufferedImage> images = imageManager.loadImagesFromDirectory(mockDir);
            assertEquals(2, images.size());
        }
    }

    @Test
    void testLoadImagesFromDirectoryWithNonImageFiles() throws IOException {
        File[] mockFiles = {mock(File.class), mock(File.class)};
        when(mockDir.exists()).thenReturn(true);
        when(mockDir.isDirectory()).thenReturn(true);
        when(mockDir.listFiles()).thenReturn(mockFiles);

        when(mockFiles[0].isFile()).thenReturn(true);
        when(mockFiles[0].getName()).thenReturn("image1.jpg");

        when(mockFiles[1].isFile()).thenReturn(true);
        when(mockFiles[1].getName()).thenReturn("nonimage.txt");

        assertThrows(IOException.class, () -> imageManager.loadImagesFromDirectory(mockDir));
    }

    @Test
    void testLoadImagesFromDirectoryWithNullDirThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> imageManager.loadImagesFromDirectory(null));
    }

    @Test
    void testLoadImagesFromDirectoryWithNonExistentDirThrowsIOException() {
        when(mockDir.exists()).thenReturn(false);
        assertThrows(IOException.class, () -> imageManager.loadImagesFromDirectory(mockDir));
    }

    @Test
    void testLoadImagesFromDirectoryWithInvalidDirectoryThrowsIOException() {
        when(mockDir.exists()).thenReturn(true);
        when(mockDir.isDirectory()).thenReturn(false);
        assertThrows(IOException.class, () -> imageManager.loadImagesFromDirectory(mockDir));
    }

    @Test
    void testLoadImagesFromDirectoryWithNoFilesThrowsIOException() {
        when(mockDir.exists()).thenReturn(true);
        when(mockDir.isDirectory()).thenReturn(true);
        when(mockDir.listFiles()).thenReturn(null);
        assertThrows(IOException.class, () -> imageManager.loadImagesFromDirectory(mockDir));
    }

    @Test
    void testSaveImageWithNullParentDirectoryThrowsIOException() {
        when(mockFile.getParentFile()).thenReturn(null);
        BufferedImage mockImage = mock(BufferedImage.class);
        assertThrows(IOException.class, () -> imageManager.saveImage(mockImage, mockFile));
    }

    @Test
    void testSaveImageWithNonExistentParentDirectoryThrowsIOException() {
        when(mockFile.getParentFile()).thenReturn(mockDir);
        when(mockFile.getParentFile().exists()).thenReturn(false);
        BufferedImage mockImage = mock(BufferedImage.class);
        assertThrows(IOException.class, () -> imageManager.saveImage(mockImage, mockFile));
    }

    @Test
    void testSaveImageWithInvalidFileFormatThrowsIOException() {
        when(mockFile.getName()).thenReturn("image.txt");
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        BufferedImage mockImage = mock(BufferedImage.class);

        assertThrows(IOException.class, () -> imageManager.saveImage(mockImage, mockFile));
    }

    @Test
    void testSaveImageWithPNGFileFormat() throws IOException {
        File mockParentDir = mock(File.class);
        when(mockFile.getParentFile()).thenReturn(mockParentDir);
        when(mockParentDir.exists()).thenReturn(true);

        when(mockFile.getName()).thenReturn("image.png");
        when(mockFile.exists()).thenReturn(false);

        BufferedImage dummyImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        try (var mockedStaticImageIO = mockStatic(ImageIO.class)) {
            mockedStaticImageIO.when(() -> ImageIO.write(eq(dummyImage), eq("png"), eq(mockFile)))
                .thenReturn(true);

            imageManager.saveImage(dummyImage, mockFile);

            mockedStaticImageIO.verify(() -> ImageIO.write(dummyImage, "png", mockFile));
        }
    }

    @Test
    void testSaveImageWithJPGFileFormat() throws IOException {
        File mockParentDir = mock(File.class);
        when(mockFile.getParentFile()).thenReturn(mockParentDir);
        when(mockParentDir.exists()).thenReturn(true);

        when(mockFile.getName()).thenReturn("image.jpg");
        when(mockFile.exists()).thenReturn(false);

        BufferedImage dummyImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        try (var mockedStaticImageIO = mockStatic(ImageIO.class)) {
            mockedStaticImageIO.when(() -> ImageIO.write(eq(dummyImage), eq("jpg"), eq(mockFile)))
                .thenReturn(true);

            imageManager.saveImage(dummyImage, mockFile);

            mockedStaticImageIO.verify(() -> ImageIO.write(dummyImage, "jpg", mockFile));
        }
    }

    @Test
    void testSaveImageWithJPEGFileFormat() throws IOException {
        File mockParentDir = mock(File.class);
        when(mockFile.getParentFile()).thenReturn(mockParentDir);
        when(mockParentDir.exists()).thenReturn(true);

        when(mockFile.getName()).thenReturn("image.jpeg");
        when(mockFile.exists()).thenReturn(false);

        BufferedImage dummyImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        try (var mockedStaticImageIO = mockStatic(ImageIO.class)) {
            mockedStaticImageIO.when(() -> ImageIO.write(eq(dummyImage), eq("jpeg"), eq(mockFile)))
                .thenReturn(true);

            imageManager.saveImage(dummyImage, mockFile);

            mockedStaticImageIO.verify(() -> ImageIO.write(dummyImage, "jpeg", mockFile));
        }
    }

    @Test
    void testSaveImageWithBMPFileFormat() throws IOException {
        File mockParentDir = mock(File.class);
        when(mockFile.getParentFile()).thenReturn(mockParentDir);
        when(mockParentDir.exists()).thenReturn(true);

        when(mockFile.getName()).thenReturn("image.bmp");
        when(mockFile.exists()).thenReturn(false);

        BufferedImage dummyImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        try (var mockedStaticImageIO = mockStatic(ImageIO.class)) {
            mockedStaticImageIO.when(() -> ImageIO.write(eq(dummyImage), eq("bmp"), eq(mockFile)))
                .thenReturn(true);

            imageManager.saveImage(dummyImage, mockFile);

            mockedStaticImageIO.verify(() -> ImageIO.write(dummyImage, "bmp", mockFile));
        }
    }
}
