package com.alltuttasneeds.delights;

import com.alltuttasneeds.delights.crafting.ConfigurableCheeseIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class DelightsIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, DelightsModule.MODID);

    public static final Supplier<IngredientType<?>> CONFIGURABLE_CHEESE = INGREDIENT_TYPES.register(
            "configurable_cheese", () -> new IngredientType<>(ConfigurableCheeseIngredient.CODEC));

    private DelightsIngredientTypes() {}
}
