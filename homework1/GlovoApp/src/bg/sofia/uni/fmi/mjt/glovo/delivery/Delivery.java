package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public record Delivery(Location client, Location restaurant, Location deliveryGuy, String foodItem, double price,
                       int estimatedTime) {
    public Delivery {
        if (client == null || restaurant == null || deliveryGuy == null || foodItem == null || foodItem.isEmpty()) {
            throw new NullPointerException("Locations and foodItem cannot be null");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (estimatedTime < 0) {
            throw new IllegalArgumentException("Estimated time cannot be negative");
        }
    }
}
