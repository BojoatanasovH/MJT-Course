package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenterApi;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidEntityException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Glovo implements GlovoApi {
    ControlCenterApi controlCenter;

    public Glovo(char[][] mapLayout) {
        this.controlCenter = new ControlCenter(mapLayout);
    }

    //Constructor for testing
    public Glovo(ControlCenter controlCenter) {
        this.controlCenter = controlCenter;
    }

    @Override
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {
        DeliveryInfo info = controlCenter.findOptimalDeliveryGuy(
            restaurant.location(),
            client.location(),
            -1,
            -1,
            ShippingMethod.CHEAPEST
        );

        if (info == null) {
            throw new NoAvailableDeliveryGuyException("No available delivery guys");
        }
        return createDelivery(client, restaurant, foodItem, info);
    }

    @Override
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {
        validateOrder(client, restaurant, foodItem);
        DeliveryInfo info = controlCenter.findOptimalDeliveryGuy(
            restaurant.location(),
            client.location(),
            -1,
            -1,
            ShippingMethod.FASTEST
        );

        if (info == null) {
            throw new NoAvailableDeliveryGuyException("No available delivery guys");
        }
        return createDelivery(client, restaurant, foodItem, info);
    }

    @Override
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant, String foodItem,
                                                 double maxPrice)
        throws NoAvailableDeliveryGuyException {
        validateOrder(client, restaurant, foodItem);

        if (maxPrice <= 0) {
            throw new IllegalArgumentException("Maximum price must be greater than zero");
        }

        DeliveryInfo info = controlCenter.findOptimalDeliveryGuy(
            restaurant.location(),
            client.location(),
            maxPrice,
            -1,
            ShippingMethod.FASTEST
        );

        if (info == null) {
            throw new NoAvailableDeliveryGuyException("No delivery guys are available for order");
        }

        return createDelivery(client, restaurant, foodItem, info);
    }

    @Override
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant, String foodItem,
                                                       int maxTime)
        throws NoAvailableDeliveryGuyException {

        validateOrder(client, restaurant, foodItem);

        if (maxTime <= 0) {
            throw new IllegalArgumentException("Maximum time must be greater than zero");
        }

        DeliveryInfo info = controlCenter.findOptimalDeliveryGuy(
            restaurant.location(),
            client.location(),
            -1,
            maxTime,
            ShippingMethod.CHEAPEST
        );

        if (info == null) {
            throw new NoAvailableDeliveryGuyException("No delivery guys are available for order");
        }

        return createDelivery(client, restaurant, foodItem, info);
    }

    private void validateOrder(MapEntity client, MapEntity restaurant, String foodItem) {
        if (client == null || restaurant == null || foodItem == null || foodItem.isEmpty()) {
            throw new InvalidOrderException("Client, restaurant and food item must not be empty or null");
        }
        isEntityValid(this.controlCenter.getLayout(), client);
        isEntityValid(this.controlCenter.getLayout(), restaurant);
    }

    private void isEntityValid(MapEntity[][] mapLayout, MapEntity entity) {
        Location location = entity.location();
        int x = location.x();
        int y = location.y();

        if (x < 0 || x >= mapLayout.length || y < 0 || y >= mapLayout[0].length || mapLayout[x][y] == null) {
            throw new InvalidEntityException("Invalid map entity: " + entity);
        }
        if (mapLayout[x][y].type() == MapEntityType.WALL) {
            throw new InvalidEntityException("Invalid location: wall at (" + x + ", " + y + ").");
        }
    }

    private Delivery createDelivery(MapEntity client, MapEntity restaurant, String foodItem, DeliveryInfo info) {
        return new Delivery(
            client.location(),
            restaurant.location(),
            info.getDeliveryGuyLocation(),
            foodItem,
            info.getPrice(),
            info.getEstimatedTime()
        );
    }
}
