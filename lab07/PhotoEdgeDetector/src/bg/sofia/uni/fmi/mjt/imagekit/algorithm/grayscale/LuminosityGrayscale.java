package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {
    private static final double RED = 0.21;
    private static final double GREEN = 0.72;
    private static final double BLUE = 0.07;

    @Override
    public BufferedImage process(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color color = new Color(image.getRGB(i, j));
                int gray = (int) (RED * color.getRed() + GREEN * color.getGreen() + BLUE * color.getBlue());
                int rgb = new Color(gray, gray, gray).getRGB();
                grayscaleImage.setRGB(i, j, rgb);
            }
        }

        return grayscaleImage;
    }
}
