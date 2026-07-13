package com.alltuttasneeds.beds.block;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class BedStateProperties {
    public static final EnumProperty<BedPosition> BED_POSITION = EnumProperty.create("bed_position", BedPosition.class);
    public static final BooleanProperty BUNK = BooleanProperty.create("bunk");

    private BedStateProperties() {}
}
