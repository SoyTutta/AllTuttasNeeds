package com.alltuttasneeds.delights.crafting;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.delights.CompatTags;
import com.alltuttasneeds.delights.DelightsIngredientTypes;
import com.alltuttasneeds.delights.config.DelightsConfig;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.Arrays;
import java.util.stream.Stream;

public final class ConfigurableCheeseIngredient implements ICustomIngredient {
    public static final ConfigurableCheeseIngredient INSTANCE = new ConfigurableCheeseIngredient();
    public static final MapCodec<ConfigurableCheeseIngredient> CODEC = MapCodec.unit(INSTANCE);

    private ConfigurableCheeseIngredient() {}

    public static Ingredient ingredient() {
        return INSTANCE.toVanilla();
    }

    private static Ingredient selectedIngredient() {
        if (DelightsConfig.isModuleEnabled() && DelightsConfig.useCheeseWedges()
                && Mods.BREWIN_AND_CHEWIN.isLoaded()) {
            return Ingredient.of(CompatTags.FOOD_CHEESE_WEDGE);
        }
        return Ingredient.of(Tags.Items.DRINKS_MILK);
    }

    @Override
    public boolean test(ItemStack stack) {
        return selectedIngredient().test(stack);
    }

    @Override
    public Stream<ItemStack> getItems() {
        return Arrays.stream(selectedIngredient().getItems());
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return DelightsIngredientTypes.CONFIGURABLE_CHEESE.get();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ConfigurableCheeseIngredient;
    }

    @Override
    public int hashCode() {
        return ConfigurableCheeseIngredient.class.hashCode();
    }
}
