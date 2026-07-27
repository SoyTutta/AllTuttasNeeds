package com.alltuttasneeds.delights.block;

import net.minecraft.util.StringRepresentable;

public enum PotluckPart implements StringRepresentable {
    ORIGIN("origin"),
    FRONT("front"),
    RIGHT("right"),
    DIAGONAL("diagonal"),
    UPPER_ORIGIN("upper_origin"),
    UPPER_FRONT("upper_front"),
    UPPER_RIGHT("upper_right"),
    UPPER_DIAGONAL("upper_diagonal");

    private final String name;

    PotluckPart(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public boolean isUpper() {
        return ordinal() >= UPPER_ORIGIN.ordinal();
    }

    public PotluckPart lowerPart() {
        return switch (this) {
            case UPPER_ORIGIN -> ORIGIN;
            case UPPER_FRONT -> FRONT;
            case UPPER_RIGHT -> RIGHT;
            case UPPER_DIAGONAL -> DIAGONAL;
            default -> this;
        };
    }
}
