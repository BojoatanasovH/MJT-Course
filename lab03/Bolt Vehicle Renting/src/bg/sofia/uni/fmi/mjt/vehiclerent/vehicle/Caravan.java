package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class Caravan extends Vehicle {
    FuelType fuelType;
    int numberOfSeats;
    int numberOfBeds;
    double pricePerWeek;
    double pricePerDay;
    double pricePerHour;

    public Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds,
                   double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
        this.numberOfBeds = numberOfBeds;
        this.pricePerWeek = pricePerWeek;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent)
        throws InvalidRentingPeriodException {
        long days = ChronoUnit.DAYS.between(startOfRent, endOfRent);
        if (days < 1) {
            throw new InvalidRentingPeriodException("Caravans must be rented for at least one day.");
        }

        final int daysInWeek = 7;
        final int priceOfSeat = 5;
        final int priceOfBed = 10;

        long weeks = days / daysInWeek;
        days = days % daysInWeek;

        double totalPrice = weeks * pricePerWeek + days * pricePerDay;
        totalPrice += fuelType.getDailyFee() * (weeks * daysInWeek + days);
        totalPrice += numberOfSeats * priceOfSeat + numberOfBeds * priceOfBed;

        totalPrice += getDriverFee(this.rentedBy);
        return totalPrice;
    }
}
