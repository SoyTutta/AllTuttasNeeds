package com.alltuttasneeds.delights.compat;

import com.alltuttasneeds.delights.config.DelightGroup;
import com.alltuttasneeds.delights.config.DelightsConfig;
import com.sammy.minersdelight.setup.MDItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.function.Supplier;

import static com.alltuttasneeds.delights.DelightsItems.foodItem;

public final class MinersDelightCompat {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "minersdelight");

    public static final Supplier<Item> HALF_A_VEGAN_HAMBURGER = register("half_a_vegan_hamburger", 6, 0.8F);
    public static final Supplier<Item> HALF_A_CAVEBURGER = register("half_a_caveburger", 6, 0.6F);
    public static final Supplier<Item> HALF_A_INSECT_SANDWICH = register("half_a_insect_sandwich", 5, 0.55F);
    public static final Supplier<Item> HALF_A_SQUID_SANDWICH = register("half_a_squid_sandwich", 5, 0.45F);

    private MinersDelightCompat() {}

    public static void addSandwichPortions(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)) return;
        output.accept(MDItems.VEGAN_HAMBURGER.get());
        output.accept(HALF_A_VEGAN_HAMBURGER.get());
        output.accept(MDItems.CAVE_HAMBURGER.get());
        output.accept(HALF_A_CAVEBURGER.get());
        output.accept(MDItems.INSECT_SANDWICH.get());
        output.accept(HALF_A_INSECT_SANDWICH.get());
        output.accept(MDItems.SQUID_SANDWICH.get());
        output.accept(HALF_A_SQUID_SANDWICH.get());
    }

    private static Supplier<Item> register(String name, int nutrition, float saturation) {
        if (DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)) {
            return ITEMS.register(name, () -> new ConsumableItem(foodItem(new FoodProperties.Builder()
                    .nutrition(nutrition).saturationModifier(saturation).fast().build()), false));
        }
        return () -> {
            throw new IllegalStateException("Tutta's Delights sandwich portions are disabled: " + name);
        };
    }
}
