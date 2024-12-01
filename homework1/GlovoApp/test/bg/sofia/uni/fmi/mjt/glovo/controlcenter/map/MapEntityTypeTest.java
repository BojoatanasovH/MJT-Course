package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidSymbolException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MapEntityTypeTest {

    @Test
    void testFromSymbolWithInvalidSymbolThrowsInvalidSymbolException() {
        assertThrows(InvalidSymbolException.class, () -> MapEntityType.fromSymbol('&'));
    }
}
