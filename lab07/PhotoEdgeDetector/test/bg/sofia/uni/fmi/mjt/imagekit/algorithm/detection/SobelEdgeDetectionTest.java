package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SobelEdgeDetectionTest {
    private ImageAlgorithm grayscaleAlgorithm;
    private SobelEdgeDetection sobelEdgeDetection;

    @BeforeEach
    void setup() {
        grayscaleAlgorithm = mock(ImageAlgorithm.class);
        sobelEdgeDetection = new SobelEdgeDetection(grayscaleAlgorithm);
    }

    @Test
    void testProcessValidImage() {
        BufferedImage inputImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        BufferedImage grayscaleImage = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);

        when(grayscaleAlgorithm.process(inputImage)).thenReturn(grayscaleImage);

        BufferedImage result = sobelEdgeDetection.process(inputImage);
        assertNotNull(result, "Processed image should not be null.");

        verify(grayscaleAlgorithm, times(1)).process(inputImage);
    }

    @Test
    void testProcessNullImageThrowsException() {
        assertThrows(NullPointerException.class, () -> sobelEdgeDetection.process(null));
    }
}
