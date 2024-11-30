package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeliveryTest {

    private Location clientLocation;
    private Location restaurantLocation;
    private Location deliveryGuyLocation;

    @BeforeEach
    void setup() {
        clientLocation = new Location(1, 1);
        restaurantLocation = new Location(2, 2);
        deliveryGuyLocation = new Location(3, 3);
    }

    @Test
    void testValidDeliveryCreation() {
        Delivery delivery = new Delivery(clientLocation, restaurantLocation, deliveryGuyLocation, "Pizza", 10, 15);

        assertEquals(clientLocation, delivery.client());
        assertEquals(restaurantLocation, delivery.restaurant());
        assertEquals(deliveryGuyLocation, delivery.deliveryGuy());
        assertEquals("Pizza", delivery.foodItem());
        assertEquals(15, delivery.estimatedTime());
        assertEquals(10, delivery.price());
    }

    @Test
    void testDeliveryWithNullClientLocationThrowsNullPointerException() {
        assertThrows(NullPointerException.class,
            () -> new Delivery(null, restaurantLocation, deliveryGuyLocation, "Pizza", 10, 15));
    }

    @Test
    void testDeliveryWithNullRestaurantLocationThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new Delivery(clientLocation, null, deliveryGuyLocation, "Pizza", 10, 15));
    }

    @Test
    void testDeliveryWithNullDeliveryGuyLocationThrowsNullPointerException() {
        assertThrows(NullPointerException.class,
            () -> new Delivery(clientLocation, restaurantLocation, null, "Pizza", 10, 15));
    }

    @Test
    void testDeliveryWithNullFoodItemThrowsNullPointerException() {
        assertThrows(NullPointerException.class,
            () -> new Delivery(clientLocation, restaurantLocation, deliveryGuyLocation, null, 10, 15));
    }

    @Test
    void testDeliveryWithEmptyFoodItemThrowsNullPointerException() {
        assertThrows(NullPointerException.class,
            () -> new Delivery(clientLocation, restaurantLocation, deliveryGuyLocation, "", 10, 15));
    }

    @Test
    void testDeliveryWithNegativePriceThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Delivery(clientLocation, restaurantLocation, deliveryGuyLocation, "Pizza", -10, 15));
    }

    @Test
    void testDeliveryWithNegativeEstimatedTimeThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Delivery(clientLocation, restaurantLocation, deliveryGuyLocation, "Pizza", 10, -15));
    }
}
