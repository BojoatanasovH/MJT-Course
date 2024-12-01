package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidEntityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocationTest {

    @Test
    void testLocationWithInvalidXThrowsInvalidEntityException() {
        assertThrows(InvalidEntityException.class, () -> new Location(-1, 3));
    }

    @Test
    void testLocationWithInvalidYThrowsInvalidEntityException() {
        assertThrows(InvalidEntityException.class, () -> new Location(1, -3));
    }
}
