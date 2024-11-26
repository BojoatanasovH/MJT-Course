import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.SobelEdgeDetection;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.FileSystemImageManager;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.LocalFileSystemImageManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FileSystemImageManager fsImageManager = new LocalFileSystemImageManager();

        BufferedImage image = fsImageManager.loadImage(new File("./kitten.png"));
        BufferedImage image2 = fsImageManager.loadImage(new File("./car.jpg"));

        ImageAlgorithm grayscaleAlgorithm = new LuminosityGrayscale();
        BufferedImage grayscaleImage = grayscaleAlgorithm.process(image);

        BufferedImage grayscaleImage2 = grayscaleAlgorithm.process(image2);

        ImageAlgorithm sobelEdgeDetection = new SobelEdgeDetection(grayscaleAlgorithm);
        BufferedImage edgeDetectedImage = sobelEdgeDetection.process(image);
        BufferedImage edgeDetectedImage2 = sobelEdgeDetection.process(image2);

        fsImageManager.saveImage(grayscaleImage, new File("./kitten-grayscale.png"));
        fsImageManager.saveImage(grayscaleImage2, new File("./car-grayscale.jpg"));
        fsImageManager.saveImage(edgeDetectedImage, new File("./kitten-edge-detected.png"));
        fsImageManager.saveImage(edgeDetectedImage2, new File("./car-edge-detected.jpg"));
    }
}
