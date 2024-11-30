import bg.sofia.uni.fmi.mjt.glovo.Glovo;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Main {
    public static void main(String[] args) {
        // Дефинираме картата на района
        char[][] mapLayout = {
            {'#', '#', '#', '#', '#', '#', '#'},
            {'#', '.', '.', '.', '.', '.', '#'},
            {'#', '.', 'R', '.', 'C', '.', '#'},
            {'#', '.', '.', '.', 'A', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#'}
        };

        // Създаваме инстанция на Glovo с подадената карта
        Glovo glovo = new Glovo(mapLayout);

        // Дефинираме клиент и ресторант
        MapEntity client = new MapEntity(new Location(2, 4), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(2, 2), MapEntityType.RESTAURANT);

        // Опитваме различни методи за получаване на доставка
        try {
            // Най-евтина доставка
            Delivery cheapestDelivery = glovo.getCheapestDelivery(client, restaurant, "Pizza Margherita");
            System.out.println("Cheapest delivery: " + cheapestDelivery);

            // Най-бърза доставка
            Delivery fastestDelivery = glovo.getFastestDelivery(client, restaurant, "Burger");
            System.out.println("Fastest delivery: " + fastestDelivery);

            // Най-бърза доставка до определена цена
            double maxPrice = 30.0;
            Delivery fastestUnderPrice = glovo.getFastestDeliveryUnderPrice(client, restaurant, "Sushi", maxPrice);
            System.out.println("Fastest delivery under price " + maxPrice + ": " + fastestUnderPrice);

            // Най-евтина доставка в рамките на определено време
            int maxTime = 20; // в условни единици време
            Delivery cheapestWithinTime = glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, "Pasta", maxTime);
            System.out.println("Cheapest delivery within time " + maxTime + ": " + cheapestWithinTime);

        } catch (NoAvailableDeliveryGuyException e) {
            System.out.println("No delivery guy available: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
