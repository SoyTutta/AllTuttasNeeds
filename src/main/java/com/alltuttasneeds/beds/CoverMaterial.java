package com.alltuttasneeds.beds;

import com.alltuttasneeds.beds.config.TBConfig;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

public record CoverMaterial(String suffix, BooleanSupplier enabled) {
    public boolean isEnabled() {
        return enabled.getAsBoolean();
    }

    @Nullable
    public Item ingredient() {
        return BedCoverIngredients.ingredientFor(suffix);
    }

    public boolean isDirectApplyEnabled() {
        return !TBConfig.directApplyDisabled.get().contains(suffix);
    }
}
