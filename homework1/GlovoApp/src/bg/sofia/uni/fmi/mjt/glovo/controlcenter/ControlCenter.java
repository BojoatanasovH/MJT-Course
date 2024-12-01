package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ControlCenter implements ControlCenterApi {
    private final MapEntity[][] mapLayout;

    public ControlCenter(char[][] layout) {
        mapLayout = new MapEntity[layout.length][layout[0].length];
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                mapLayout[i][j] = new MapEntity(new Location(i, j), MapEntityType.fromSymbol(layout[i][j]));
            }
        }
    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation, double maxPrice,
                                               int maxTime, ShippingMethod shippingMethod) {
        List<MapEntity> deliveryGuys = new ArrayList<>();
        for (MapEntity[] mapEntities : mapLayout) {
            for (MapEntity entity : mapEntities) {
                if (entity.type() == MapEntityType.DELIVERY_GUY_CAR ||
                    entity.type() == MapEntityType.DELIVERY_GUY_BIKE) {
                    deliveryGuys.add(entity);
                }
            }
        }

        if (deliveryGuys.isEmpty()) {
            throw new NoAvailableDeliveryGuyException("No delivery guys are available");
        }

        DeliveryInfo optimalDelivery = null;

        for (MapEntity deliveryGuy : deliveryGuys) {
            DeliveryType deliveryType =
                (deliveryGuy.type() == MapEntityType.DELIVERY_GUY_CAR) ? DeliveryType.CAR : DeliveryType.BIKE;

            int toRestaurantTime = findShortestTime(deliveryGuy.location(), restaurantLocation, deliveryType);
            int toClientTime = findShortestTime(restaurantLocation, clientLocation, deliveryType);
            int totalTime = toRestaurantTime + toClientTime;

            double toRestaurantCost = calculateCost(toRestaurantTime, deliveryType);
            double toClientCost = calculateCost(toClientTime, deliveryType);
            double totalCost = toRestaurantCost + toClientCost;

//            System.out.println("Delivery guy at " + deliveryGuy.location() + ":");
//            System.out.println("  Total time: " + totalTime + ", Total cost: " + totalCost);

            if ((maxPrice != -1 && totalCost > maxPrice) || (maxTime != -1 && totalTime > maxTime)) {
                continue;
            }

            if (optimalDelivery == null ||
                (shippingMethod == ShippingMethod.FASTEST && totalTime < optimalDelivery.getEstimatedTime()) ||
                (shippingMethod == ShippingMethod.CHEAPEST && totalCost < optimalDelivery.getPrice())) {
                optimalDelivery = new DeliveryInfo(deliveryGuy.location(), totalCost, totalTime, deliveryType);
            }
        }

        return optimalDelivery;
    }


    @Override
    public MapEntity[][] getLayout() {
        return mapLayout;
    }

    private int findShortestTime(Location start, Location end, DeliveryType deliveryType) {
        int rows = mapLayout.length;
        int cols = mapLayout[0].length;

        boolean[][] visited = new boolean[rows][cols];
        Queue<Location> queue = new LinkedList<>();
        Map<Location, Integer> distance = new HashMap<>();

        queue.add(start);
        distance.put(start, 0);

        int timePerKm = deliveryType == DeliveryType.CAR ? 3 : 5;

        while (!queue.isEmpty()) {
            Location current = queue.poll();
            if (current.equals(end)) {
                return distance.get(current) * timePerKm;
            }

            for (Location neighbor : getNeighbors(current)) {
                if (!visited[neighbor.x()][neighbor.y()] && canTraverse(neighbor)) {
                    visited[neighbor.x()][neighbor.y()] = true;
                    queue.add(neighbor);
                    distance.put(neighbor, distance.get(current) + 1);
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    private List<Location> getNeighbors(Location location) {
        List<Location> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int newX = location.x() + dir[0];
            int newY = location.y() + dir[1];
            if (newX >= 0 && newY >= 0 && newX < mapLayout.length && newY < mapLayout[0].length) {
                neighbors.add(new Location(newX, newY));
            }
        }
        return neighbors;
    }

    private double calculateCost(double time, DeliveryType deliveryType) {
        int pricePerKm = deliveryType == DeliveryType.CAR ? 5 : 3;
        int timePerKm = deliveryType == DeliveryType.CAR ? 3 : 5;
        return (time / timePerKm) * pricePerKm;
    }

    private boolean canTraverse(Location location) {
        MapEntityType type = mapLayout[location.x()][location.y()].type();
        return type != MapEntityType.WALL;
    }
}
