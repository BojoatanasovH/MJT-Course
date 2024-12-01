package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {
    private final ImageAlgorithm grayscaleAlgorithm;
    private final int[][] sobelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    private final int[][] sobelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
    private final int rgbMaxValue = 255;

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        BufferedImage grayscaleImage = grayscaleAlgorithm.process(image);

        int width = grayscaleImage.getWidth();
        int height = grayscaleImage.getHeight();

        BufferedImage edgeDetectedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int gx = 0;
                int gy = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixel = new Color(grayscaleImage.getRGB(x + i, y + j)).getRed();
                        gx += sobelX[i + 1][j + 1] * pixel;
                        gy += sobelY[i + 1][j + 1] * pixel;
                    }
                }
                int gradient = (int) Math.sqrt(gx * gx + gy * gy);
                gradient = Math.min(rgbMaxValue, gradient);

                int rgb = new Color(gradient, gradient, gradient).getRGB();
                edgeDetectedImage.setRGB(x, y, rgb);
            }
        }
        return edgeDetectedImage;
    }
}
