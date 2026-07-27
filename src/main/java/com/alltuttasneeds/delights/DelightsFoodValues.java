package com.alltuttasneeds.delights;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class DelightsFoodValues
    {
        public static final FoodProperties SLIME_DUMPLINGS = (new FoodProperties.Builder())
                .nutrition(5).saturationModifier(0.8f)
                .build();
        public static final FoodProperties SQUISHMALLOW = (new FoodProperties.Builder())
                .nutrition(2).saturationModifier(0.2f)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 300, 0, false, false), 1.0F)
                .alwaysEdible().fast().build();
        public static final FoodProperties SMOKED_SQUISHMALLOW = (new FoodProperties.Builder())
                .nutrition(3).saturationModifier(0.2f)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 600, 0, false, false), 1.0F)
                .alwaysEdible().fast().build();
        public static final FoodProperties STICKY_GREEN_NOODLES = (new FoodProperties.Builder())
                .nutrition(12).saturationModifier(0.8f)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 6000, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties MINTMALLOW_BIT = (new FoodProperties.Builder())
                .nutrition(4).saturationModifier(0.3f)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 600, 0, false, false), 1.0F)
                .alwaysEdible().fast().build();
        public static final FoodProperties SLIME_JELLY_SLICE = (new FoodProperties.Builder())
                .nutrition(2).saturationModifier(0.1F)
                .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0, false, false), 1.0F)
                .alwaysEdible().fast().build();

        public static final FoodProperties MINT_LETTUCE = (new FoodProperties.Builder())
                .nutrition(1).saturationModifier(0.1F)
                .alwaysEdible().fast().build();
        public static final FoodProperties SWEET_TRIGGER = (new FoodProperties.Builder())
                .nutrition(6).saturationModifier(0.5F)
                .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0, false, false), 1.0F)
                .alwaysEdible().fast().build();
        public static final FoodProperties CREEPER_SALAD = (new FoodProperties.Builder())
                .nutrition(6).saturationModifier(0.5F)
                .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 1, false, false), 1.0F)
                .build();

        public static final FoodProperties ROTTEN_BACON = (new FoodProperties.Builder())
                .nutrition(2).saturationModifier(0.3F)
                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 300, 0, false, false), 0.5F)
                .build();
        public static final FoodProperties ROTTEN_MINCED_BEEF = (new FoodProperties.Builder())
                .nutrition(2).saturationModifier(0.3F)
                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 300, 0, false, false), 0.5F)
                .build();
        public static final FoodProperties ROTTEN_MUTTON_CHOPS = (new FoodProperties.Builder())
                .nutrition(2).saturationModifier(0.3F)
                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 300, 0, false, false), 0.5F)
                .build();
        public static final FoodProperties ROTTEN_CHICKEN_CUTS = (new FoodProperties.Builder())
                .nutrition(1).saturationModifier(0.3F)
                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 300, 0, false, false), 0.5F)
                .build();
        public static final FoodProperties ROTTEN_SAUSAGE = (new FoodProperties.Builder())
                .nutrition(2).saturationModifier(0.3F)
                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 300, 0, false, false), 0.5F)
                .build();
        public static final FoodProperties DISGUSTING_STEW = (new FoodProperties.Builder())
                .nutrition(6).saturationModifier(0.4F)
                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 3600, 0, false, false), 0.5F)
                .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 1200, 0, false, false), 0.5F)
                .build();
        public static final FoodProperties ROTTEN_MEAT_ON_A_BONE = (new FoodProperties.Builder())
                .nutrition(6).saturationModifier(0.4F)
                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 1200, 0, true, true), 0.75F)
                .build();
        public static final FoodProperties ROTTEN_HAM = (new FoodProperties.Builder())
                .nutrition(8).saturationModifier(0.5F)
                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 1200, 0, true, true), 0.75F)
                .build();

        public static final FoodProperties RAW_GUARDIAN_TAIL = (new FoodProperties.Builder())
                .nutrition(6).saturationModifier(0.3F)
                .build();
        public static final FoodProperties SMOKED_GUARDIAN_TAIL = (new FoodProperties.Builder())
                .nutrition(9).saturationModifier(0.8F)
                .build();
        public static final FoodProperties RAW_GUARDIAN_SLICE = (new FoodProperties.Builder())
                .nutrition(2).saturationModifier(0.1F)
                .build();
        public static final FoodProperties COOKED_GUARDIAN_SLICE = (new FoodProperties.Builder())
                .nutrition(3).saturationModifier(0.8F)
                .build();

        public static final FoodProperties GUARDIAN_GRILLED_ON_A_STICK = (new FoodProperties.Builder())
                .nutrition(6).saturationModifier(0.8F)
                .build();
        public static final FoodProperties FIRST_PLATE_OF_POTLUCK_SOUP = (new FoodProperties.Builder())
                .nutrition(12).saturationModifier(0.75F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 4800, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties PLATE_OF_POTLUCK_SOUP = (new FoodProperties.Builder())
                .nutrition(10).saturationModifier(0.6F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 3600, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties TAIL_IN_PLATE_OF_POTLUCK_SOUP = (new FoodProperties.Builder())
                .nutrition(14).saturationModifier(0.8F)
                .effect(() -> new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 300, 0, false, false), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 3600, 0, false, false), 1.0F)
                .build();

        public static final FoodProperties RAW_ELDER_GUARDIAN_TAIL = (new FoodProperties.Builder())
                .nutrition(8).saturationModifier(0.3F)
                .build();
        public static final FoodProperties SMOKED_ELDER_GUARDIAN_TAIL = (new FoodProperties.Builder())
                .nutrition(12).saturationModifier(0.8F)
                .build();
        public static final FoodProperties RAW_ELDER_GUARDIAN_SLICE = (new FoodProperties.Builder())
                .nutrition(3).saturationModifier(0.1F)
                .build();
        public static final FoodProperties COOKED_ELDER_GUARDIAN_SLICE = (new FoodProperties.Builder())
                .nutrition(5).saturationModifier(0.8F)
                .build();

        public static final FoodProperties ELDER_GUARDIAN_GRILLED_ON_A_STICK = (new FoodProperties.Builder())
                .nutrition(9).saturationModifier(0.8F)
                .build();
        public static final FoodProperties FIRST_PLATE_OF_ELDER_POTLUCK_SOUP = (new FoodProperties.Builder())
                .nutrition(13).saturationModifier(0.80F)
                .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 0, false, false), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 4800, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties PLATE_OF_ELDER_POTLUCK_SOUP = (new FoodProperties.Builder())
                .nutrition(10).saturationModifier(0.75F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 3600, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties TAIL_IN_PLATE_OF_ELDER_POTLUCK_SOUP = (new FoodProperties.Builder())
                .nutrition(15).saturationModifier(0.9F)
                .effect(() -> new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 0, false, false), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0, false, false), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 3600, 0, false, false), 1.0F)
                .build();

        public static final FoodProperties POT_SLICE = (new FoodProperties.Builder())
                .nutrition(1).saturationModifier(0.3F)
                .build();
        public static final FoodProperties ANCIENT_BOILED_EGG = (new FoodProperties.Builder())
                .nutrition(5).saturationModifier(0.7F)
                .build();
        public static final FoodProperties ANCIENT_RIBS = (new FoodProperties.Builder())
                .nutrition(5).saturationModifier(0.4F)
                .build();
        public static final FoodProperties COOKED_ANCIENT_RIBS = (new FoodProperties.Builder())
                .nutrition(10).saturationModifier(0.7F)
                .build();
        public static final FoodProperties RIBS_WITH_EGGS = (new FoodProperties.Builder())
                .nutrition(12).saturationModifier(0.5F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 1200, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties ANCIENT_RIBS_WITH_SEEDS = (new FoodProperties.Builder())
                .nutrition(12).saturationModifier(0.7F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 1200, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties ANCIENT_BREAKFAST = (new FoodProperties.Builder())
                .nutrition(10).saturationModifier(0.6F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 1200, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties PASTA_WITH_POT = (new FoodProperties.Builder())
                .nutrition(12).saturationModifier(0.7F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 3600, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties POT_WITH_HONEY_ON_A_STICK = (new FoodProperties.Builder())
                .nutrition(6).saturationModifier(0.5F)
                .build();
        public static final FoodProperties ANCIENT_SALAD = (new FoodProperties.Builder())
                .nutrition(12).saturationModifier(0.7F)
                .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 600, 1, false, false), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 1, false, false), 1.0F)
                .build();
        public static final FoodProperties ANCIENT_EGG_SANDWICH = (new FoodProperties.Builder())
                .nutrition(14).saturationModifier(0.7F)
                .build();
        public static final FoodProperties HALF_A_ANCIENT_EGG_SANDWICH = (new FoodProperties.Builder())
                .nutrition(8).saturationModifier(0.7F)
                .fast().build();
        public static final FoodProperties PLATE_OF_ANCIENT_SOFT_BOILED_EGG_AND_BREAD = (new FoodProperties.Builder())
                .nutrition(14).saturationModifier(0.75F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 3600, 0, false, false), 1.0F)
                .build();

        public static final FoodProperties RAW_BREADED_MEAT = (new FoodProperties.Builder())
                .nutrition(4).saturationModifier(0.3F)
                .build();
        public static final FoodProperties COOKED_BREADED_MEAT = (new FoodProperties.Builder())
                .nutrition(9).saturationModifier(0.7F)
                .build();
        public static final FoodProperties BREADED_MEAT_SANDWICH = (new FoodProperties.Builder())
                .nutrition(14).saturationModifier(0.9F)
                .build();
        public static final FoodProperties HALF_A_BREADED_MEAT_SANDWICH = (new FoodProperties.Builder())
                .nutrition(7).saturationModifier(0.9F)
                .fast().build();

        public static final FoodProperties POTATO_SLICES = (new FoodProperties.Builder())
                .nutrition(1).saturationModifier(0F)
                .fast().build();
        public static final FoodProperties BAKED_POTATO_SLICES = (new FoodProperties.Builder())
                .nutrition(2).saturationModifier(0.75F)
                .fast().build();
        public static final FoodProperties MASHED_POTATO = (new FoodProperties.Builder())
                .nutrition(3).saturationModifier(0.7F)
                .build();
        public static final FoodProperties FRIES_POTATOES = (new FoodProperties.Builder())
                .nutrition(5).saturationModifier(0.6F)
                .alwaysEdible().fast().build();
        public static final FoodProperties POTATO_OMELET = (new FoodProperties.Builder())
                .nutrition(13).saturationModifier(0.75F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 1800, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties NAPOLITANA_WITH_FRIES_POTATOES = (new FoodProperties.Builder())
                .nutrition(14).saturationModifier(0.9F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 3200, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties BREADED_MEAT_WITH_MASHED_POTATO = (new FoodProperties.Builder())
                .nutrition(12).saturationModifier(0.9F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 1800, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties BREADED_MEAT_WITH_PASTA = (new FoodProperties.Builder())
                .nutrition(13).saturationModifier(0.9F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 1800, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties MASHED_POTATO_WITH_MEATBALLS = (new FoodProperties.Builder())
                .nutrition(10).saturationModifier(0.8F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 1800, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties PLATE_OF_POTATO_AND_MEAT_PIE = (new FoodProperties.Builder())
                .nutrition(14).saturationModifier(0.7F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 3200, 0, false, false), 1.0F)
                .build();

        public static final FoodProperties COOKED_PASTA = (new FoodProperties.Builder())
                .nutrition(7).saturationModifier(0.65F)
                .build();

        public static final FoodProperties RAW_FROG_LEG = (new FoodProperties.Builder())
                .nutrition(2).saturationModifier(0.4F)
                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)
                .fast().build();
        public static final FoodProperties COOKED_FROG_LEG = (new FoodProperties.Builder())
                .nutrition(5).saturationModifier(0.4F)
                .fast().build();
        public static final FoodProperties FROGGLE_RICE_CHOWDER = (new FoodProperties.Builder())
                .nutrition(10).saturationModifier(0.7F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 1200, 0, false, false), 1.0F)
                .alwaysEdible().fast().build();
        public static final FoodProperties FROGGLE_SANDWICH = (new FoodProperties.Builder())
                .nutrition(12).saturationModifier(0.75F)
                .build();
        public static final FoodProperties HALF_A_FROGGLE_SANDWICH = (new FoodProperties.Builder())
                .nutrition(6).saturationModifier(0.75F)
                .fast().build();
        public static final FoodProperties FROG_LEG_ON_A_STICK = (new FoodProperties.Builder())
                .nutrition(8).saturationModifier(0.75F)
                .build();
        public static final FoodProperties HONEY_WINGS = (new FoodProperties.Builder())
                .nutrition(8).saturationModifier(0.65F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 1200, 0, false, false), 1.0F)
                .alwaysEdible().fast().build();

        public static final FoodProperties GLISTERING_MELON_POPSICLE = (new FoodProperties.Builder())
                .nutrition(3).saturationModifier(1.2F)
                .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0, false, false), 1.0F)
                .alwaysEdible().fast().build();
        public static final FoodProperties COCOA_POPSICLE = (new FoodProperties.Builder())
                .nutrition(3).saturationModifier(0.2F)
                .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0, false, false), 1.0F)
                .alwaysEdible().fast().build();
        public static final FoodProperties MINT_POPSICLE = (new FoodProperties.Builder())
                .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0, false, false), 1.0F)
                .nutrition(3).saturationModifier(0.3F)
                .alwaysEdible().fast().build();
        public static final FoodProperties SWEET_BERRIES_POPSICLE = (new FoodProperties.Builder())
                .nutrition(3).saturationModifier(0.2F)
                .alwaysEdible().fast().build();
        public static final FoodProperties GLOW_BERRIES_POPSICLE = (new FoodProperties.Builder())
                .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 300, 0, false, false), 1.0F)
                .nutrition(3).saturationModifier(0.3F)
                .alwaysEdible().fast().build();
        public static final FoodProperties APPLE_POPSICLE = (new FoodProperties.Builder())
                .nutrition(4).saturationModifier(0.3F)
                .alwaysEdible().fast().build();
        public static final FoodProperties GOLDEN_APPLE_POPSICLE = (new FoodProperties.Builder())
                .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 0, false, false), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 600, 0, false, false), 1.0F)
                .nutrition(4).saturationModifier(1.2F)
                .alwaysEdible().fast().build();
        public static final FoodProperties TORCH_POPSICLE = (new FoodProperties.Builder())
            .nutrition(4).saturationModifier(0.3F)
                .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false), 1.0F)
                .alwaysEdible().fast().build();

        public static final FoodProperties PLATE_OF_MUZZLE_WITH_VEGETABLES = (new FoodProperties.Builder())
                .nutrition(14).saturationModifier(0.9F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 7800, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties PLATE_OF_HAM_WITH_VEGETABLES = (new FoodProperties.Builder())
                .nutrition(14).saturationModifier(0.75F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 6000, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties PLATE_OF_SUCKLING_PIG_WITH_VEGETABLES = (new FoodProperties.Builder())
                .nutrition(12).saturationModifier(0.6F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 3600, 0, false, false), 1.0F)
                .build();

        public static final FoodProperties ICE_CREAM_IN_A_PUMPKIN_HEAD = (new FoodProperties.Builder())
                .nutrition(12).saturationModifier(0.6F)
                .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 0, false, false), 1.0F)
                .alwaysEdible().build();
        public static final FoodProperties PUMPKIN_HEAD_SLICE = (new FoodProperties.Builder())
                .nutrition(4).saturationModifier(0.6F)
                .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0, false, false), 1.0F)
                .alwaysEdible().fast().build();

        public static final FoodProperties BEET_AND_EGG_SALAD = (new FoodProperties.Builder())
                .nutrition(7).saturationModifier(1F)
                .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties PASTA_WITH_BEET = (new FoodProperties.Builder())
                .nutrition(10).saturationModifier(0.9F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 3200, 0, false, false), 1.0F)
                .build();
        public static final FoodProperties BEET_JUICE = (new FoodProperties.Builder())
                .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 0, false, false), 1.0F)
                .build();

        public static final FoodProperties HALF_A_BACON_SANDWICH = (new FoodProperties.Builder())
                .nutrition(5).saturationModifier(0.8F)
                .fast().build();
        public static final FoodProperties HALF_A_EGG_SANDWICH = (new FoodProperties.Builder())
                .nutrition(4).saturationModifier(0.8F)
                .fast().build();
        public static final FoodProperties HALF_A_CHICKEN_SANDWICH = (new FoodProperties.Builder())
                .fast().nutrition(5).saturationModifier(0.8F)
                .build();
        public static final FoodProperties HALF_A_HAMBURGER = (new FoodProperties.Builder())
                .fast().nutrition(6).saturationModifier(0.75F)
                .build();

    }
