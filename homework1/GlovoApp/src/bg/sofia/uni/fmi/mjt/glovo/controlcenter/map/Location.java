package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidEntityException;

public record Location(int x, int y) {
    public Location {
        if (x < 0 || y < 0) {
            throw new InvalidEntityException("Location coordinates cannot be negative: " + x + " " + y);
        }
    }
}