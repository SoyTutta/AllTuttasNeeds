package com.alltuttasneeds.delights.compat;

import com.alltuttasneeds.delights.config.DelightGroup;
import com.alltuttasneeds.delights.config.DelightsConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;
import umpaz.brewinandchewin.common.registry.BnCItems;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.function.Supplier;

import static com.alltuttasneeds.delights.DelightsItems.foodItem;

public class BrewinAndChewinCompat {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "brewinandchewin");
    public static final Supplier<Item> HALF_A_HAM_AND_CHEESE_SANDWICH = DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)
            ? ITEMS.register("half_a_ham_and_cheese_sandwich", ()  ->
            new ConsumableItem(foodItem(new FoodProperties.Builder().nutrition(5).saturationModifier(0.9F).fast().build()), false))
            : () -> { throw new IllegalStateException("Tutta's Delights sandwich portions are disabled"); };

    public static void addSandwichPortions(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)) return;
        output.accept(BnCItems.HAM_AND_CHEESE_SANDWICH);
        output.accept(HALF_A_HAM_AND_CHEESE_SANDWICH.get());
    }
}
