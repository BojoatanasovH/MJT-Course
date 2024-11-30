package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ControlCenterTest {

    private ControlCenter controlCenter;
    private Location client;
    private Location restaurant;

    @BeforeEach
    void setUp() {
        char[][] mapLayout = {
            {'#', '#', '#', '.', '#'},
            {'#', '.', 'B', 'R', '.'},
            {'.', '.', '#', '.', '#'},
            {'#', 'C', '.', 'A', '.'},
            {'#', '.', '#', '#', '#'}
        };
        restaurant = new Location(1, 3);
        client = new Location(3, 1);
        controlCenter = new ControlCenter(mapLayout);
    }

    @Test
    void testFindOptimalDeliveryGuyReturnsNullWhenNoPathExists() {
        DeliveryInfo deliveryInfo =
            controlCenter.findOptimalDeliveryGuy(restaurant, client, 20.0, 10, ShippingMethod.FASTEST);
        assertNull(deliveryInfo);
    }

    @Test
    void testGetLayoutReturnsCorrectMap() {
        assertNotNull(controlCenter.getLayout());
        assertEquals(MapEntityType.RESTAURANT, controlCenter.getLayout()[1][3].type());
    }

    @Test
    void testFindOptimalDeliveryGuyWithNoDeliveryGuysThrowsNoAvailableDeliveryGuysException() {
        char[][] mapLayout = {
            {'#', '#', '#', '.', '#'},
            {'#', '.', '.', 'R', '.'},
            {'.', '.', '#', '.', '#'},
            {'#', 'C', '.', '.', '.'},
            {'#', '.', '#', '#', '#'}
        };
        controlCenter = new ControlCenter(mapLayout);
        assertThrows(NoAvailableDeliveryGuyException.class,
            () -> controlCenter.findOptimalDeliveryGuy(client, restaurant, -1, -1, ShippingMethod.CHEAPEST));
    }
}
