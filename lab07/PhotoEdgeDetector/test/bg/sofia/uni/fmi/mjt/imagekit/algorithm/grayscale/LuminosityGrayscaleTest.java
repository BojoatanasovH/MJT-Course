package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LuminosityGrayscaleTest {
    private final LuminosityGrayscale luminosityGrayscale = new LuminosityGrayscale();

    @Test
    void testProcessValidImage() {
        BufferedImage inputImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        inputImage.setRGB(0, 0, new Color(255, 0, 0).getRGB());
        inputImage.setRGB(0, 1, new Color(0, 255, 0).getRGB());
        inputImage.setRGB(1, 0, new Color(0, 0, 255).getRGB());
        inputImage.setRGB(1, 1, new Color(255, 255, 255).getRGB());

        BufferedImage grayscaleImage = luminosityGrayscale.process(inputImage);

        assertEquals(2, grayscaleImage.getWidth(), "Width should match.");
        assertEquals(2, grayscaleImage.getHeight(), "Height should match");

        int grayRed = new Color(grayscaleImage.getRGB(0, 0)).getRed();
        int grayGreen = new Color(grayscaleImage.getRGB(0, 1)).getRed();
    }

    @Test
    void testProcessWithNullImageThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> luminosityGrayscale.process(null));
    }
}
