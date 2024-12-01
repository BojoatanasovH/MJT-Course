package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {
    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new IOException("Files does not exists or is not regular");
        }

        String fileName = imageFile.getName().toLowerCase();
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png") &&
            !fileName.endsWith(".bmp")) {
            throw new IOException("File is not one of the supported formats");
        }

        return ImageIO.read(imageFile);
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imageDirectory) throws IOException {
        if (imageDirectory == null) {
            throw new IllegalArgumentException("Image directory cannot be null");
        }
        if (!imageDirectory.exists() || !imageDirectory.isDirectory()) {
            throw new IOException("Image directory does not exist or is not a directory");
        }

        File[] files = imageDirectory.listFiles();
        if (files == null) {
            throw new IOException("Failed to list files in directory");
        }
        List<BufferedImage> images = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                if (!name.endsWith(".jpg") && !name.endsWith(".jpeg") && !name.endsWith(".png") &&
                    !name.endsWith(".bmp")) {
                    throw new IOException(
                        "Directory contains files that are not of the supported formats: " + file.getName());
                }
            }
            try {
                images.add(loadImage(file));
            } catch (IOException e) {
                //Skipping files that cannot be loaded
            }
        }
        return images;
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("Image and imageFile cannot be null");
        }

        if (imageFile.exists()) {
            throw new IOException("File already exists: " + imageFile.getAbsolutePath());
        }

        File parentDirectory = imageFile.getParentFile();
        if (parentDirectory == null || !parentDirectory.exists()) {
            throw new IOException("Parent directory does not exist");
        }

        String name = imageFile.getName();
        if (!name.endsWith(".jpg") && !name.endsWith(".jpeg") && !name.endsWith(".png") &&
            !name.endsWith(".bmp")) {
            throw new IOException("Unsupported file format");
        }

        String[] nameSplit = name.split("\\.");
        String format = nameSplit[nameSplit.length - 1];
        if (!ImageIO.write(image, format, imageFile)) {
            throw new IOException("Failed to save image: " + imageFile.getAbsolutePath());
        }
    }
}
