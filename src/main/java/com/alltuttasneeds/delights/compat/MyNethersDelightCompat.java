package com.alltuttasneeds.delights.compat;

import com.alltuttasneeds.delights.config.DelightGroup;
import com.alltuttasneeds.delights.config.DelightsConfig;
import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.function.Supplier;

import static com.alltuttasneeds.delights.DelightsItems.foodItem;

public class MyNethersDelightCompat {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "mynethersdelight");
    public static final Supplier<Item> HALF_A_NETHER_BURGER = register(DelightGroup.SANDWICH_PORTIONS, "half_a_nether_burger", () ->
            new ConsumableItem(foodItem(new FoodProperties.Builder().nutrition(6).saturationModifier(0.75F).fast().build()), false));
    public static final Supplier<Item> HALF_A_HOTDOG = register(DelightGroup.SANDWICH_PORTIONS, "half_a_hotdog", () ->
            new ConsumableItem(foodItem(new FoodProperties.Builder().nutrition(4).saturationModifier(0.45F).fast().build()), false));
    public static final Supplier<Item> HOTDOG_WITH_FRIES_POTATOES = register(DelightGroup.POTATO, "hotdog_with_fries_potatoes", () ->
            new ConsumableItem(foodItem(new FoodProperties.Builder().nutrition(12).saturationModifier(0.55F).build()), false));
    public static final Supplier<Item> HALF_A_HOTDOG_WITH_FRIES_POTATOES = register("half_a_hotdog_with_fries_potatoes", () ->
                    new ConsumableItem(foodItem(new FoodProperties.Builder().nutrition(6).saturationModifier(0.55F).fast().build()), false),
            DelightGroup.POTATO, DelightGroup.SANDWICH_PORTIONS);
    public static final Supplier<Item> HALF_A_HOTDOG_WITH_MIXED_SALAD = register(DelightGroup.SANDWICH_PORTIONS, "half_a_hotdog_with_mixed_salad", () ->
            new ConsumableItem(foodItem(new FoodProperties.Builder().nutrition(5).saturationModifier(0.5F)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 50, 0), 1.0F).fast().build()), true));
    public static final Supplier<Item> HALF_A_HOTDOG_WITH_NETHER_SALAD = register(DelightGroup.SANDWICH_PORTIONS, "half_a_hotdog_with_nether_salad", () ->
            new ConsumableItem(foodItem(new FoodProperties.Builder().nutrition(5).saturationModifier(0.5F)
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 120, 0), 0.3F).fast().build()), false));
    public static final Supplier<Item> HALF_A_CHILIDOG = register(DelightGroup.SANDWICH_PORTIONS, "half_a_chilidog", () ->
            new ConsumableItem(foodItem(new FoodProperties.Builder().nutrition(6).saturationModifier(0.45F)
                    .effect(() -> new MobEffectInstance(MNDEffects.GPUNGENT, 300, 0), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0), 1.0F).fast().build()), true));

    public static void addFrozenTreats(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.FROZEN_TREATS)) return;
        output.accept(MNDItems.TEAR_POPSICLE.get());
    }

    public static void addPotato(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.POTATO)) return;
        output.accept(HOTDOG_WITH_FRIES_POTATOES.get());
        if (DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)) {
            output.accept(HALF_A_HOTDOG_WITH_FRIES_POTATOES.get());
        }
    }

    public static void addSandwichPortions(CreativeModeTab.Output output) {
        if (!DelightsConfig.isGroupEnabled(DelightGroup.SANDWICH_PORTIONS)) return;
        output.accept(MNDItems.NETHER_BURGER.get());
        output.accept(HALF_A_NETHER_BURGER.get());
        output.accept(MNDItems.HOTDOG.get());
        output.accept(HALF_A_HOTDOG.get());
        output.accept(MNDItems.HOTDOG_WITH_MIXED_SALAD.get());
        output.accept(HALF_A_HOTDOG_WITH_MIXED_SALAD.get());
        output.accept(MNDItems.HOTDOG_WITH_NETHER_SALAD.get());
        output.accept(HALF_A_HOTDOG_WITH_NETHER_SALAD.get());
        output.accept(MNDItems.CHILIDOG.get());
        output.accept(HALF_A_CHILIDOG.get());
    }

    private static Supplier<Item> register(DelightGroup group, String name, Supplier<Item> item) {
        return register(name, item, group);
    }

    private static Supplier<Item> register(String name, Supplier<Item> item, DelightGroup... groups) {
        if (DelightsConfig.areGroupsEnabled(groups)) {
            return ITEMS.register(name, item);
        }
        return () -> {
            throw new IllegalStateException("Tutta's Delights compatibility content is disabled: " + name);
        };
    }
}
