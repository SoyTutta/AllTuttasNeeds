package com.alltuttasneeds.beds;

import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

public record BlanketMaterial(String suffix, boolean supportsDeluxe, BooleanSupplier enabled) {
    public boolean isEnabled() {
        return enabled.getAsBoolean();
    }

    @Nullable
    public Item itemFor(DyeColor color) {
        return BedBlanketIngredients.itemFor(suffix, color);
    }

    @Nullable
    public DyeColor colorFor(ItemStack stack) {
        return BedBlanketIngredients.colorFor(suffix, stack);
    }

    public boolean isDirectApplyEnabled() {
        return !TBConfig.directApplyDisabled.get().contains(suffix);
    }
}
