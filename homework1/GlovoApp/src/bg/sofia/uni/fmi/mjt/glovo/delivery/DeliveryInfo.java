package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public class DeliveryInfo {
    Location deliveryGuyLocation;
    double price;
    int estimatedTime;
    DeliveryType deliveryType;

    public DeliveryInfo(Location deliveryGuyLocation, double price, int estimatedTime, DeliveryType deliveryType) {
        this.deliveryGuyLocation = deliveryGuyLocation;
        this.deliveryType = deliveryType;
        this.price = price;
        this.estimatedTime = estimatedTime;
    }

    public Location getDeliveryGuyLocation() {
        return deliveryGuyLocation;
    }

    public double getPrice() {
        return price;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }
}
