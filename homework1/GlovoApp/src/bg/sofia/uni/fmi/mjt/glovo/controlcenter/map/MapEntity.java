package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidEntityException;

public record MapEntity(Location location, MapEntityType type) {
    public MapEntity {
        if (type == null) {
            throw new InvalidEntityException("Map entity type cannot be null");
        }
        if (location == null) {
            throw new InvalidEntityException("Map entity location cannot be null");
        }
    }
}
