package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidSymbolException;

public enum MapEntityType {
    ROAD('.'),
    WALL('#'),
    RESTAURANT('R'),
    CLIENT('C'),
    DELIVERY_GUY_CAR('A'),
    DELIVERY_GUY_BIKE('B');
    // ...

    private final char symbol;

    MapEntityType(char symbol) {
        this.symbol = symbol;
    }

    public static MapEntityType fromSymbol(char symbol) {
        for (MapEntityType type : values()) {
            if (type.symbol == symbol) {
                return type;
            }
        }
        throw new InvalidSymbolException("Invalid symbol for MapEntityType: " + symbol);
    }
}