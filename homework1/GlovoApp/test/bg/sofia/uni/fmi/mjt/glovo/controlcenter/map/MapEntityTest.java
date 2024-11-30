package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidEntityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MapEntityTest {

    @Test
    void testMapEntityWithNullTypeThrowsInvalidEntityException() {
        assertThrows(InvalidEntityException.class, () -> new MapEntity(new Location(0, 0), null));
    }

    @Test
    void testMapEntityWithNullLocationThrowsInvalidEntityException() {
        assertThrows(InvalidEntityException.class, () -> new MapEntity(null, MapEntityType.ROAD));
    }
}
