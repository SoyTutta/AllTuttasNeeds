package com.alltuttasneeds.beds;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BooleanSupplier;

public record CoverMaterial(String suffix, BooleanSupplier enabled) {
    public boolean isEnabled() {
        return enabled.getAsBoolean();
    }

    @Nullable
    public Item ingredient() {
        return BedCoverIngredients.ingredientFor(suffix);
    }

    public List<Item> ingredients() {
        return BedCoverIngredients.ingredientsFor(suffix);
    }

    public boolean matches(ItemStack stack) {
        return ingredients().stream().anyMatch(stack::is);
    }

    public boolean isDirectApplyEnabled() {
        return BedIngredientSyncState.isDirectApplyEnabled(suffix);
    }
}
