package com.alltuttasneeds.beds.compat.mods;

import com.alltuttasneeds.beds.BedColor;
import com.alltuttasneeds.core.Mods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public final class ArtsAndCraftsBedColors {
    public static final BedColor BLEACHED = new BedColor(
            "bleached",
            DyeColor.WHITE,
            ResourceLocation.fromNamespaceAndPath("arts_and_crafts", "bleached_wool"),
            ResourceLocation.fromNamespaceAndPath("arts_and_crafts", "bleachdew"),
            Mods.ARTS_AND_CRAFTS);

    private ArtsAndCraftsBedColors() {}
}
