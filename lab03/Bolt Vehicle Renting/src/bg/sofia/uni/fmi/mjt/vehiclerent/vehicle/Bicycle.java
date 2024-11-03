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
        final int hoursInDay = 24;
        final int daysInWeek = 7;

        long hours = ChronoUnit.HOURS.between(startOfRent, endOfRent);
        long days = hours / hoursInDay;

        if (days > (daysInWeek - 1)) {
            throw new InvalidRentingPeriodException("Bicycles cannot be rented for more than a week.");
        }
        hours = 0L;

        return days * pricePerDay + hours * pricePerHour;
    }
}
