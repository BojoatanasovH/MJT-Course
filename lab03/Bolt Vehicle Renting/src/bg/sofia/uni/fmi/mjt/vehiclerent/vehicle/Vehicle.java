package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.LocalDateTime;

public abstract sealed class Vehicle permits Bicycle, Car, Caravan {

    final String id;
    final String model;
    Driver rentedBy;
    LocalDateTime startRentTime;

    public Driver getRentedBy() {
        return this.rentedBy;
    }

    public Vehicle(String id, String model) {
        this.id = id;
        this.model = model;
    }

    /**
     * Simulates rental of the vehicle. The vehicle now is considered rented by the provided driver and the start of the rental is the provided date.
     *
     * @param driver        the driver that wants to rent the vehicle.
     * @param startRentTime the start time of the rent
     */
    public void rent(Driver driver, LocalDateTime startRentTime) {
        this.rentedBy = driver;
        this.startRentTime = startRentTime;
    }

    /**
     * Simulates end of rental for the vehicle - it is no longer rented by a driver.
     *
     * @param rentalEnd time of end of rental
     * @throws IllegalArgumentException      in case @rentalEnd is null
     * @throws VehicleNotRentedException     in case the vehicle is not rented at all
     * @throws InvalidRentingPeriodException in case the rentalEnd is before the currently noted start date of rental or
     *                                       in case the Vehicle does not allow the passed period for rental, e.g. Caravans must be rented for at least a day
     *                                       and the driver tries to return them after an hour.
     */
    public void returnBack(LocalDateTime rentalEnd)
        throws InvalidRentingPeriodException, VehicleNotRentedException, IllegalArgumentException {
        if (rentedBy == null) {
            throw new VehicleNotRentedException("Vehicle is not rented.");
        }
        if (rentalEnd.isBefore(startRentTime)) {
            throw new InvalidRentingPeriodException("Rental end date cannot be before start date.");
        }
        rentedBy = null;
        startRentTime = null;
    }

    /**
     * Used to calculate potential rental price without the vehicle to be rented.
     * The calculation is based on the type of the Vehicle (Car/Caravan/Bicycle).
     *
     * @param startOfRent the beginning of the rental
     * @param endOfRent   the end of the rental
     * @return potential price for rent
     * @throws InvalidRentingPeriodException in case the vehicle cannot be rented for that period of time or
     *                                       the period is not valid (end date is before start date)
     */
    public abstract double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent)
        throws InvalidRentingPeriodException;

    public LocalDateTime getStartRentTime() {
        return this.startRentTime;
    }

    public double getDriverFee(Driver driver) {
        final double juniorFee = 10.0;
        final double seniorFee = 15.0;

        return switch (driver.ageGroup()) {
            case JUNIOR -> juniorFee;
            case SENIOR -> seniorFee;
            case EXPERIENCED -> 0.0;
        };
    }
}
