package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidEntityException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidSymbolException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlovoTest {

    private ControlCenter controlCenterMock;
    private GlovoApi glovo;
    private MapEntity[][] mapLayout;
    private MapEntity client;
    private MapEntity restaurant;

    @BeforeEach
    void setUp() {
        controlCenterMock = mock(ControlCenter.class);
        glovo = new Glovo(controlCenterMock);

        // Initialize a default map layout
        char[][] layout = {
            {'.', '#', '.', '.', '#'},
            {'.', 'C', '.', '.', '.'}, // Client at (1, 1)
            {'.', 'R', '.', '.', '.'}, // Restaurant at (2, 1)
            {'.', '.', 'A', '.', '.'}, // Delivery guy with car at (3, 2)
            {'#', '.', '.', '.', '#'}
        };
        mapLayout = createMapLayout(layout);
        when(controlCenterMock.getLayout()).thenReturn(mapLayout);
        client = mapLayout[1][1];
        restaurant = mapLayout[2][1];
    }

    private MapEntity[][] createMapLayout(char[][] layout) {
        int rows = layout.length;
        int cols = layout[0].length;
        MapEntity[][] mapLayout = new MapEntity[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = layout[i][j];
                Location location = new Location(i, j);

                switch (cell) {
                    case 'C' -> mapLayout[i][j] = new MapEntity(location, MapEntityType.CLIENT);
                    case 'R' -> mapLayout[i][j] = new MapEntity(location, MapEntityType.RESTAURANT);
                    case 'A' -> mapLayout[i][j] = new MapEntity(location, MapEntityType.DELIVERY_GUY_CAR);
                    case 'B' -> mapLayout[i][j] = new MapEntity(location, MapEntityType.DELIVERY_GUY_BIKE);
                    case '.' -> mapLayout[i][j] = new MapEntity(location, MapEntityType.ROAD);
                    case '#' -> mapLayout[i][j] = new MapEntity(location, MapEntityType.WALL);
                    default -> throw new InvalidSymbolException("Invalid symbol: " + cell);
                }
            }
        }

        return mapLayout;
    }

    @Test
    void testCreateMapLayoutWithInvalidSymbolThrowsInvalidSymbolException() {
        char[][] layout = {
            {'.', '#', '.', '.', '#'},
            {'.', 'C', '.', '.', '.'},
            {'.', 'R', '.', '.', '.'},
            {'.', '.', 'A', '.', '3'},
            {'#', '.', '.', '.', '#'}
        };
        assertThrows(InvalidSymbolException.class, () -> createMapLayout(layout));
    }

    @Test
    void testGetCheapestDeliveryNoDeliveryGuy() {
        when(controlCenterMock.findOptimalDeliveryGuy(
            eq(restaurant.location()), eq(client.location()), eq(-1.0), eq(-1), eq(ShippingMethod.CHEAPEST)))
            .thenReturn(null);

        assertThrows(NoAvailableDeliveryGuyException.class,
            () -> glovo.getCheapestDelivery(client, restaurant, "Pizza"));
    }

    @Test
    void testGetFastestDelivery() throws NoAvailableDeliveryGuyException {
        DeliveryInfo deliveryInfo = new DeliveryInfo(new Location(0, 0), 15.0, 20, DeliveryType.CAR);

        when(controlCenterMock.findOptimalDeliveryGuy(
            eq(restaurant.location()), eq(client.location()), eq(-1.0), eq(-1), eq(ShippingMethod.FASTEST)))
            .thenReturn(deliveryInfo);

        Delivery delivery = glovo.getFastestDelivery(client, restaurant, "Pizza");

        assertNotNull(delivery);
        assertEquals(client.location(), delivery.client());
        assertEquals(restaurant.location(), delivery.restaurant());
        assertEquals(deliveryInfo.getDeliveryGuyLocation(), delivery.deliveryGuy());
        assertEquals(deliveryInfo.getPrice(), delivery.price());
        assertEquals(deliveryInfo.getEstimatedTime(), delivery.estimatedTime());
    }

    @Test
    void testGetFastestDeliveryUnderPriceInvalidPrice() {
        assertThrows(IllegalArgumentException.class,
            () -> glovo.getFastestDeliveryUnderPrice(client, restaurant, "Pizza", -10.0));
    }

    @Test
    void testGetFastestDeliveryUnderPrice() throws NoAvailableDeliveryGuyException {
        DeliveryInfo deliveryInfo = new DeliveryInfo(new Location(0, 0), 9.99, 30, DeliveryType.BIKE);

        when(controlCenterMock.findOptimalDeliveryGuy(
            eq(restaurant.location()), eq(client.location()), eq(10.0), eq(-1), eq(ShippingMethod.FASTEST)))
            .thenReturn(deliveryInfo);

        Delivery delivery = glovo.getFastestDeliveryUnderPrice(client, restaurant, "Pizza", 10.0);

        assertNotNull(delivery);
        assertEquals(9.99, delivery.price());
        assertEquals(30, delivery.estimatedTime());
    }

    @Test
    void testGetCheapestDeliveryWithinTimeLimitInvalidTime() {
        assertThrows(IllegalArgumentException.class,
            () -> glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, "Pizza", -5));
    }

    @Test
    void testGetCheapestDeliveryWithinTimeLimit() throws NoAvailableDeliveryGuyException {
        DeliveryInfo deliveryInfo = new DeliveryInfo(new Location(0, 0), 5.99, 15, DeliveryType.BIKE);

        when(controlCenterMock.findOptimalDeliveryGuy(
            eq(restaurant.location()), eq(client.location()), eq(-1.0), eq(15), eq(ShippingMethod.CHEAPEST)))
            .thenReturn(deliveryInfo);

        Delivery delivery = glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, "Pizza", 15);

        assertNotNull(delivery);
        assertEquals(5.99, delivery.price());
        assertEquals(15, delivery.estimatedTime());
    }

    @Test
    void testValidateOrderInvalidEntities() {
        assertThrows(InvalidOrderException.class,
            () -> glovo.getFastestDelivery(null, restaurant, "Pizza"));

        MapEntity client = mapLayout[1][1];

        assertThrows(NoAvailableDeliveryGuyException.class,
            () -> glovo.getFastestDelivery(client, restaurant, "Pizza"));
    }

    @Test
    void testValidateOrderInvalidMapEntity() {
        assertThrows(InvalidEntityException.class,
            () -> glovo.getFastestDelivery(new MapEntity(new Location(-1, -1), MapEntityType.CLIENT), restaurant,
                "Pizza"));
    }

    @Test
    void testGetFastestDeliveryUnderPriceWhenInfoIsNullThrowsNoAvailableDeliveryGuyException() {
        when(controlCenterMock.findOptimalDeliveryGuy(
            eq(restaurant.location()),
            eq(client.location()),
            eq(10),
            eq(-1),
            eq(ShippingMethod.FASTEST))).thenReturn(null);

        assertThrows(NoAvailableDeliveryGuyException.class,
            () -> glovo.getFastestDeliveryUnderPrice(client, restaurant, "Pizza", 10));
    }

    @Test
    void testCreateDeliveryCreatesCorrectDelivery() throws NoAvailableDeliveryGuyException {
        DeliveryInfo deliveryInfo = new DeliveryInfo(new Location(3, 2), 15.0, 20, DeliveryType.CAR);

        when(controlCenterMock.findOptimalDeliveryGuy(
            eq(restaurant.location()), eq(client.location()), eq(-1.0), eq(-1), eq(ShippingMethod.FASTEST)))
            .thenReturn(deliveryInfo);

        Delivery delivery = glovo.getFastestDelivery(client, restaurant, "Burger");

        assertNotNull(delivery);
        assertEquals(client.location(), delivery.client());
        assertEquals(restaurant.location(), delivery.restaurant());
        assertEquals(deliveryInfo.getDeliveryGuyLocation(), delivery.deliveryGuy());
        assertEquals("Burger", delivery.foodItem());
        assertEquals(deliveryInfo.getPrice(), delivery.price());
        assertEquals(deliveryInfo.getEstimatedTime(), delivery.estimatedTime());
    }

    @Test
    void testGetCheapestDeliveryWithinTimeLimitWithNullInfoThrowsNoAvailableDeliveryGuyException() {
        when(controlCenterMock.findOptimalDeliveryGuy(
            eq(restaurant.location()),
            eq(client.location()),
            eq(-1),
            eq(10),
            eq(ShippingMethod.CHEAPEST)
        )).thenReturn(null);
        assertThrows(NoAvailableDeliveryGuyException.class,
            () -> glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, "Pizza", 10));
    }

    @Test
    void testGetFastestDeliveryWithOutOfBoundsClient() {
        MapEntity client = new MapEntity(new Location(10, 10), MapEntityType.CLIENT);

        assertThrows(InvalidEntityException.class,
            () -> glovo.getFastestDelivery(client, restaurant, "Pizza"));
    }

    @Test
    void testGetFastestDeliveryWithNullMapCell() {
        MapEntity restaurant = new MapEntity(new Location(1, 0), MapEntityType.RESTAURANT);

        assertThrows(NoAvailableDeliveryGuyException.class,
            () -> glovo.getFastestDelivery(client, restaurant, "Pizza"));
    }
}
