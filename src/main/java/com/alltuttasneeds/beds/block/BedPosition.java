package com.alltuttasneeds.beds.block;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum BedPosition implements StringRepresentable {
    SINGLE,
    LEFT,
    CENTER,
    RIGHT;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
