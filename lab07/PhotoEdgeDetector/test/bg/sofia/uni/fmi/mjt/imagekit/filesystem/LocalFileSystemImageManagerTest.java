package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LocalFileSystemImageManagerTest {

    private LocalFileSystemImageManager imageManager;

    @BeforeEach
    void setUp() {
        imageManager = new LocalFileSystemImageManager();
    }

    @Test
    void testLoadImageThrowsIllegalArgumentExceptionWhenFileIsNull() throws IOException {
        File imageFile = null;

        assertThrows(IllegalArgumentException.class, () -> imageManager.loadImage(imageFile));
    }

    @Test
    void testLoadImageThrowsIOExceptionWithNonExistentFile() throws IOException {
        File imageFile = new File("/asdasd/nonexistent.txt");

        assertThrows(IOException.class, () -> imageManager.loadImage(imageFile));
    }

    @Test
    void testLoadImageThrowsIOExceptionWithNonRegularFile() throws IOException {
        File imageFile = new File("../src");

        assertThrows(IOException.class, () -> imageManager.loadImage(imageFile));
    }

    @Test
    void testLoadImageThrowsIOExceptionWithNonSupportedFileFormat() throws IOException {
        File imageFile = mock(File.class);
        when(imageFile.getName()).thenReturn("nonexistent.txt");

        assertThrows(IOException.class, () -> imageManager.loadImage(imageFile));
    }

    @Test
    void testLoadImageUnsupportedFileFormat() {
        File imageFile = mock(File.class);

        when(imageFile.getName()).thenReturn("document.txt");
        when(imageFile.exists()).thenReturn(true);

        assertThrows(IOException.class, () -> imageManager.loadImage(imageFile));
    }

    @Test
    void loadImagesFromDirectoryWithNullDirectory() {
        File imageDirectory = null;

        assertThrows(IllegalArgumentException.class, () -> imageManager.loadImagesFromDirectory(imageDirectory));
    }

    @Test
    void loadImagesFromDirectoryWithNullFiles() {
        File imageDirectory = mock(File.class);
        when(imageDirectory.exists()).thenReturn(true);
        when(imageDirectory.isDirectory()).thenReturn(true);
        when(imageDirectory.listFiles()).thenReturn(null);

        assertThrows(IOException.class, () -> imageManager.loadImagesFromDirectory(imageDirectory));
    }

    @Test
    void testLoadImagesFromDirectoryValid() throws IOException {
        LocalFileSystemImageManager imageManagerSpy = Mockito.spy(new LocalFileSystemImageManager());

        File mockDirectory = mock(File.class);
        when(mockDirectory.exists()).thenReturn(true);
        when(mockDirectory.isDirectory()).thenReturn(true);

        File mockFile1 = mock(File.class);
        File mockFile2 = mock(File.class);

        when(mockFile1.getName()).thenReturn("image1.jpg");
        when(mockFile1.exists()).thenReturn(true);
        when(mockFile1.isFile()).thenReturn(true);

        when(mockFile2.getName()).thenReturn("image2.png");
        when(mockFile2.exists()).thenReturn(true);
        when(mockFile2.isFile()).thenReturn(true);

        File[] mockFiles = {mockFile1, mockFile2};
        when(mockDirectory.listFiles()).thenReturn(mockFiles);

        BufferedImage mockImage = mock(BufferedImage.class);
        doReturn(mockImage).when(imageManagerSpy).loadImage(mockFile1);
        doReturn(mockImage).when(imageManagerSpy).loadImage(mockFile2);

        List<BufferedImage> images = imageManagerSpy.loadImagesFromDirectory(mockDirectory);

        assertNotNull(images);
        assertEquals(2, images.size());
        verify(mockDirectory, times(1)).listFiles();
        verify(imageManagerSpy, times(1)).loadImage(mockFile1);
        verify(imageManagerSpy, times(1)).loadImage(mockFile2);
    }

    @Test
    void testLoadImagesFromDirectoryWithUnsupportedFilesThrowsException() {
        File mockDirectory = mock(File.class);
        when(mockDirectory.exists()).thenReturn(true);
        when(mockDirectory.isDirectory()).thenReturn(true);

        File mockFile1 = mock(File.class);
        when(mockFile1.getName()).thenReturn("image1.jpg");
        when(mockFile1.exists()).thenReturn(true);
        when(mockFile1.isFile()).thenReturn(true);

        File mockFile2 = mock(File.class);
        when(mockFile2.getName()).thenReturn("file1.txt");
        when(mockFile2.exists()).thenReturn(true);
        when(mockFile2.isFile()).thenReturn(true);

        File[] mockFiles = {mockFile1, mockFile2};
        when(mockDirectory.listFiles()).thenReturn(mockFiles);

        assertThrows(IOException.class, () -> imageManager.loadImagesFromDirectory(mockDirectory));
    }

    @Test
    void testLoadImagesFromDirectoryWhenDirectoryDoesNotExist() throws IOException {
        File mockDirectory = mock(File.class);
        when(mockDirectory.exists()).thenReturn(false);
        when(mockDirectory.isDirectory()).thenReturn(false);

        assertThrows(IOException.class, () -> imageManager.loadImagesFromDirectory(mockDirectory));
    }

    @Test
    void testSaveImageWithValidImage() throws IOException {
        BufferedImage mockImage = mock(BufferedImage.class);
        File mockFile = mock(File.class);

        when(mockFile.getParentFile()).thenReturn(mock(File.class));
        when(mockFile.getParentFile().exists()).thenReturn(true);
        when(mockFile.getName()).thenReturn("image.jpg");
        when(mockFile.exists()).thenReturn(false);

        boolean writeResult = true;
        try (MockedStatic<ImageIO> imageIOMock = mockStatic(ImageIO.class)) {
            imageIOMock.when(() -> ImageIO.write(mockImage, "jpg", mockFile)).thenReturn(writeResult);

            assertDoesNotThrow(() -> imageManager.saveImage(mockImage, mockFile));
            imageIOMock.verify(() -> ImageIO.write(mockImage, "jpg", mockFile), times(1));
        }
    }

    @Test
    void testSaveImageNullArguments() {
        BufferedImage mockImage = mock(BufferedImage.class);
        File mockFile = mock(File.class);

        assertThrows(IllegalArgumentException.class, () -> imageManager.saveImage(null, mockFile));
        assertThrows(IllegalArgumentException.class, () -> imageManager.saveImage(mockImage, null));
    }

    @Test
    void testSaveImageFileAlreadyExists() {
        BufferedImage mockImage = mock(BufferedImage.class);
        File mockFile = mock(File.class);

        when(mockFile.exists()).thenReturn(true);

        assertThrows(IOException.class, () -> imageManager.saveImage(mockImage, mockFile));
        verify(mockFile, times(1)).exists();
    }

    @Test
    void testSaveImageWithNoParentDirectory() {
        BufferedImage mockImage = mock(BufferedImage.class);
        File mockFile = mock(File.class);

        when(mockFile.getParentFile()).thenReturn(null);

        assertThrows(IOException.class, () -> imageManager.saveImage(mockImage, mockFile));
    }

    @Test
    void testSaveImageWithUnsupportedFileFormat() {
        BufferedImage mockImage = mock(BufferedImage.class);
        File mockFile = mock(File.class);

        when(mockFile.getParentFile()).thenReturn(mock(File.class));
        when(mockFile.getParentFile().exists()).thenReturn(true);
        when(mockFile.getName()).thenReturn("image.txt");

        assertThrows(IOException.class, () -> imageManager.saveImage(mockImage, mockFile));
    }

    @Test
    void testSaveImageFailedWriteOperation() throws IOException {
        BufferedImage mockImage = mock(BufferedImage.class);
        File mockFile = mock(File.class);

        when(mockFile.getParentFile()).thenReturn(mock(File.class));
        when(mockFile.getParentFile().exists()).thenReturn(true);
        when(mockFile.getName()).thenReturn("image.jpg");
        when(mockFile.exists()).thenReturn(false);

        boolean writeResult = false;
        try (MockedStatic<ImageIO> imageIOMock = mockStatic(ImageIO.class)) {
            imageIOMock.when(() -> ImageIO.write(mockImage, "jpg", mockFile)).thenReturn(writeResult);

            assertThrows(IOException.class, () -> imageManager.saveImage(mockImage, mockFile));
            imageIOMock.verify(() -> ImageIO.write(mockImage, "jpg", mockFile), times(1));
        }
    }
}
