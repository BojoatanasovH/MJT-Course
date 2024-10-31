package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class Bicycle extends Vehicle {
    double pricePerDay;
    double pricePerHour;

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent)
            throws InvalidRentingPeriodException {
        long hours = ChronoUnit.HOURS.between(startOfRent, endOfRent);
        long days = hours / 24;

        if (days > 6) {
            throw new InvalidRentingPeriodException("Bicycles cannot be rented for more than a week.");
        }
        hours = hours % 24;

        return days * pricePerDay + hours * pricePerHour;
    }
}
