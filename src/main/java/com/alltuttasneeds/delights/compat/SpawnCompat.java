package com.alltuttasneeds.delights.compat;

import com.alltuttasneeds.delights.config.DelightGroup;
import com.alltuttasneeds.delights.config.DelightsConfig;
import com.ninni.spawn.registry.SpawnItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.function.Supplier;

import static com.alltuttasneeds.delights.DelightsItems.foodItem;

public final class SpawnCompat {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "spawn");

    public static final Supplier<Item> HALF_A_TUNA_SANDWICH = DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)
            ? ITEMS.register("half_a_tuna_sandwich", () -> new ConsumableItem(foodItem(new FoodProperties.Builder()
                    .nutrition(4).saturationModifier(1.0F).fast().build()), false))
            : () -> { throw new IllegalStateException("Tutta's Delights sandwich portions are disabled"); };

    private SpawnCompat() {}

    public static void addSandwichPortions(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)) return;
        output.accept(SpawnItems.TUNA_SANDWICH.get());
        output.accept(HALF_A_TUNA_SANDWICH.get());
    }
}
