package com.alltuttasneeds.delights;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.delights.item.*;
import com.alltuttasneeds.delights.DelightsFoodValues;
import com.alltuttasneeds.delights.config.DelightGroup;
import com.alltuttasneeds.delights.config.DelightsConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.item.PlaceableItem;
import vectorwing.farmersdelight.common.item.PopsicleItem;

import java.util.function.Supplier;

import static com.alltuttasneeds.delights.config.DelightGroup.*;

public class DelightsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "tuttasdelights");

    public static Item.Properties basicItem() {
        return (new Item.Properties());
    }

    public static Item.Properties foodItem(FoodProperties food) {
        return (new Item.Properties()).food(food);
    }

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return (new Item.Properties()).food(food).craftRemainder(Items.BOWL).stacksTo(16);
    }
    public static Item.Properties drinkItem() {
        return (new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16);
    }

    /// Dungeons & Delights
    /// Slime
    /// /// ingredients
    public static final Supplier<Item> SLIMECUBE = register(SLIME, "slimecube", ()  ->
            new Item(basicItem())
    );
    /// /// meals
    public static final Supplier<Item> SLIME_DUMPLINGS = register(SLIME, "slime_dumplings", ()  ->
            new SlimeConsumableItem(foodItem(DelightsFoodValues.SLIME_DUMPLINGS))
    );
    public static final Supplier<Item> SQUISHMALLOW = register(SLIME, "squishmallow", ()  ->
            new SlimeConsumableItem(foodItem(DelightsFoodValues.SQUISHMALLOW))
    );
    public static final Supplier<Item> SMOKED_SQUISHMALLOW = register(SLIME, "smoked_squishmallow", ()  ->
            new SlimeConsumableItem(foodItem(DelightsFoodValues.SMOKED_SQUISHMALLOW))
    );
    public static final Supplier<Item> STICKY_GREEN_NOODLES = register(SLIME, "sticky_green_noodles", ()  ->
            new SlimeConsumableItem(bowlFoodItem(DelightsFoodValues.STICKY_GREEN_NOODLES), true)
    );
    public static final Supplier<Item> MINTMALLOW_BIT = register(SLIME, "mintmallow_bite", ()  ->
            new MintmallowBitItem(foodItem(DelightsFoodValues.MINTMALLOW_BIT))
    );
    /// /// feasts
    public static final Supplier<Item> SLIME_JELLY = register(SLIME, "slime_jelly", ()  ->
            new PlaceableItem(DelightsBlocks.SLIME_JELLY_BLOCK.get(), basicItem().stacksTo(1))
    );
    public static final Supplier<Item> SLIME_JELLY_SLICE = register(SLIME, "slime_jelly_slice", ()  ->
            new SlimeConsumableItem(foodItem(DelightsFoodValues.SLIME_JELLY_SLICE), true)
    );

    /// Creeper
    /// /// ingredients
    public static final Supplier<Item> MINT_LETTUCE = register(CREEPER, "mint_lettuce", ()  ->
            new MintConsumableItem(foodItem(DelightsFoodValues.MINT_LETTUCE), 0.25F, 4.0F)
    );
    /// /// meals
    public static final Supplier<Item> SWEET_TRIGGER = register(CREEPER, "sweet_trigger", ()  ->
            new MintConsumableItem(foodItem(DelightsFoodValues.SWEET_TRIGGER), 0.15F, 4.0F)
    );
    public static final Supplier<Item> CREEPER_SALAD = register(CREEPER, "creeper_salad", ()  ->
            new CreeperSaladItem(bowlFoodItem(DelightsFoodValues.CREEPER_SALAD), 0.75F, 16.0F)
    );
    /// /// feasts
    public static final Supplier<Item> MINT_ROULETTE = register(CREEPER, "mint_roulette", ()  ->
            new PlaceableItem(DelightsBlocks.MINT_ROULETTE_BLOCK.get(), basicItem().stacksTo(1))
    );
    /// Undead
    /// /// ingredients
    public static final Supplier<Item> BROKEN_BONES = register(UNDEAD, "broken_bones", ()  ->
            new Item(basicItem())
    );
    public static final Supplier<Item> ROTTEN_BACON = register(UNDEAD, "rotten_bacon", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.ROTTEN_BACON))
    );
    public static final Supplier<Item> ROTTEN_MINCED_BEEF = register(UNDEAD, "rotten_minced_beef", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.ROTTEN_MINCED_BEEF))
    );
    public static final Supplier<Item> ROTTEN_MUTTON_CHOPS = register(UNDEAD, "rotten_mutton_chops", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.ROTTEN_MUTTON_CHOPS))
    );
    public static final Supplier<Item> ROTTEN_CHICKEN_CUTS = register(UNDEAD, "rotten_chicken_cuts", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.ROTTEN_CHICKEN_CUTS))
    );
    public static final Supplier<Item> ROTTEN_SAUSAGE = register(UNDEAD, "rotten_sausage", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.ROTTEN_SAUSAGE))
    );
    public static final Supplier<Item> ROTTEN_MEAT_ON_A_BONE = register(UNDEAD, "rotten_meat_on_a_bone", ()  ->
            new RottenArmItem(foodItem(DelightsFoodValues.ROTTEN_MEAT_ON_A_BONE))
    );
    public static final Supplier<Item> ROTTEN_HAM = register(UNDEAD, "rotten_ham", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.ROTTEN_HAM))
    );
    /// /// meals
    public static final Supplier<Item> DISGUSTING_STEW = register(UNDEAD, "disgusting_stew", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.DISGUSTING_STEW), true)
    );

    /// Guardian
    /// /// ingredients
    public static final Supplier<Item> GUARDIAN = register(DelightGroup.GUARDIAN, "guardian", () -> new Item(basicItem().stacksTo(1)));
    public static final Supplier<Item> GUARDIAN_HEAD = register(DelightGroup.GUARDIAN, "guardian_head", () -> new Item(basicItem()));
    public static final Supplier<Item> RAW_GUARDIAN_TAIL = register(DelightGroup.GUARDIAN, "raw_guardian_tail", () -> new ConsumableItem(foodItem(DelightsFoodValues.RAW_GUARDIAN_TAIL)));
    public static final Supplier<Item> SMOKED_GUARDIAN_TAIL = register(DelightGroup.GUARDIAN, "smoked_guardian_tail", () -> new ConsumableItem(foodItem(DelightsFoodValues.SMOKED_GUARDIAN_TAIL)));
    public static final Supplier<Item> RAW_GUARDIAN_SLICE = register(DelightGroup.GUARDIAN, "raw_guardian_slice", () -> new ConsumableItem(foodItem(DelightsFoodValues.RAW_GUARDIAN_SLICE)));
    public static final Supplier<Item> COOKED_GUARDIAN_SLICE = register(DelightGroup.GUARDIAN, "cooked_guardian_slice", () -> new ConsumableItem(foodItem(DelightsFoodValues.COOKED_GUARDIAN_SLICE)));
    /// /// meals
    public static final Supplier<Item> GUARDIAN_GRILLED_ON_A_STICK = register(DelightGroup.GUARDIAN, "guardian_grilled_on_a_stick", () -> new ConsumableItem(foodItem(DelightsFoodValues.GUARDIAN_GRILLED_ON_A_STICK)));
    /// /// feasts
    public static final Supplier<Item> POTLUCK_SOUP = register(DelightGroup.GUARDIAN, "potluck_soup", () -> new FatPlaceableItem(DelightsBlocks.POTLUCK_SOUP_BLOCK.get(), basicItem().stacksTo(1)));
    public static final Supplier<Item> FIRST_PLATE_OF_POTLUCK_SOUP = register(DelightGroup.GUARDIAN, "first_plate_of_potluck_soup", () -> new ConsumableItem(bowlFoodItem(DelightsFoodValues.FIRST_PLATE_OF_POTLUCK_SOUP), true));
    public static final Supplier<Item> PLATE_OF_POTLUCK_SOUP = register(DelightGroup.GUARDIAN, "plate_of_potluck_soup", () -> new ConsumableItem(bowlFoodItem(DelightsFoodValues.PLATE_OF_POTLUCK_SOUP), true));
    public static final Supplier<Item> TAIL_IN_PLATE_OF_POTLUCK_SOUP = register(DelightGroup.GUARDIAN, "tail_in_plate_of_potluck_soup", () -> new ConsumableItem(bowlFoodItem(DelightsFoodValues.TAIL_IN_PLATE_OF_POTLUCK_SOUP), true));
    /// Elder Guardian
    /// /// ingredients
    public static final Supplier<Item> ELDER_GUARDIAN = register(DelightGroup.GUARDIAN, "elder_guardian", () -> new Item(basicItem().stacksTo(1).rarity(Rarity.EPIC)));
    public static final Supplier<Item> ELDER_GUARDIAN_HEAD = register(DelightGroup.GUARDIAN, "elder_guardian_head", () -> new Item(basicItem().rarity(Rarity.RARE)));
    public static final Supplier<Item> RAW_ELDER_GUARDIAN_TAIL = register(DelightGroup.GUARDIAN, "raw_elder_guardian_tail", () -> new ConsumableItem(foodItem(DelightsFoodValues.RAW_ELDER_GUARDIAN_TAIL).rarity(Rarity.RARE)));
    public static final Supplier<Item> SMOKED_ELDER_GUARDIAN_TAIL = register(DelightGroup.GUARDIAN, "smoked_elder_guardian_tail", () -> new ConsumableItem(foodItem(DelightsFoodValues.SMOKED_ELDER_GUARDIAN_TAIL).rarity(Rarity.RARE)));
    public static final Supplier<Item> RAW_ELDER_GUARDIAN_SLICE = register(DelightGroup.GUARDIAN, "raw_elder_guardian_slice", () -> new ConsumableItem(foodItem(DelightsFoodValues.RAW_ELDER_GUARDIAN_SLICE).rarity(Rarity.RARE)));
    public static final Supplier<Item> COOKED_ELDER_GUARDIAN_SLICE = register(DelightGroup.GUARDIAN, "cooked_elder_guardian_slice", () -> new ConsumableItem(foodItem(DelightsFoodValues.COOKED_ELDER_GUARDIAN_SLICE).rarity(Rarity.RARE)));
    /// /// meals
    public static final Supplier<Item> ELDER_GUARDIAN_GRILLED_ON_A_STICK = register(DelightGroup.GUARDIAN, "elder_guardian_grilled_on_a_stick", () -> new ConsumableItem(foodItem(DelightsFoodValues.ELDER_GUARDIAN_GRILLED_ON_A_STICK).rarity(Rarity.RARE)));
    /// /// feasts
    public static final Supplier<Item> ELDER_POTLUCK_SOUP = register(DelightGroup.GUARDIAN, "elder_potluck_soup", () -> new FatPlaceableItem(DelightsBlocks.ELDER_POTLUCK_SOUP_BLOCK.get(), basicItem().stacksTo(1).rarity(Rarity.EPIC)));
    public static final Supplier<Item> FIRST_PLATE_OF_ELDER_POTLUCK_SOUP = register(DelightGroup.GUARDIAN, "first_plate_of_elder_potluck_soup", () -> new ConsumableItem(bowlFoodItem(DelightsFoodValues.FIRST_PLATE_OF_ELDER_POTLUCK_SOUP).rarity(Rarity.RARE), true));
    public static final Supplier<Item> PLATE_OF_ELDER_POTLUCK_SOUP = register(DelightGroup.GUARDIAN, "plate_of_elder_potluck_soup", () -> new ConsumableItem(bowlFoodItem(DelightsFoodValues.PLATE_OF_ELDER_POTLUCK_SOUP).rarity(Rarity.RARE), true));
    public static final Supplier<Item> TAIL_IN_PLATE_OF_ELDER_POTLUCK_SOUP = register(DelightGroup.GUARDIAN, "tail_in_plate_of_elder_potluck_soup", () -> new ConsumableItem(bowlFoodItem(DelightsFoodValues.TAIL_IN_PLATE_OF_ELDER_POTLUCK_SOUP).rarity(Rarity.RARE), true));

    /// Sniffers
    /// /// ingredients
    public static final Supplier<Item> POD_SLICE = register(ANCIENT, "pod_slice", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.POD_SLICE))
    );
    public static final Supplier<Item> ANCIENT_BOILED_EGG = register(ANCIENT, "ancient_boiled_egg", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.ANCIENT_BOILED_EGG))
    );
    public static final Supplier<Item> ANCIENT_RIBS = register(ANCIENT, "ancient_ribs", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.ANCIENT_RIBS))
    );
    public static final Supplier<Item> COOKED_ANCIENT_RIBS = register(ANCIENT, "cooked_ancient_ribs", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.COOKED_ANCIENT_RIBS))
    );
    /// /// meals
    public static final Supplier<Item> RIBS_WITH_EGGS = register(ANCIENT, "ribs_with_eggs", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.RIBS_WITH_EGGS), true)
    );
    public static final Supplier<Item> ANCIENT_RIBS_WITH_SEEDS = register(ANCIENT, "ancient_ribs_with_seeds", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.ANCIENT_RIBS_WITH_SEEDS), true)
    );
    public static final Supplier<Item> ANCIENT_BREAKFAST = register(ANCIENT, "ancient_breakfast", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.ANCIENT_BREAKFAST), true)
    );
    public static final Supplier<Item> PASTA_WITH_POD = register(ANCIENT, "pasta_with_pod", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.PASTA_WITH_POD), true)
    );
    public static final Supplier<Item> POD_WITH_HONEY_ON_A_STICK = register(ANCIENT, "pod_with_honey_on_a_stick", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.POD_WITH_HONEY_ON_A_STICK))
    );
    public static final Supplier<Item> ANCIENT_SALAD = register(ANCIENT, "ancient_salad", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.ANCIENT_SALAD),false)
    );
    public static final Supplier<Item> ANCIENT_EGG_SANDWICH = register(ANCIENT, "ancient_egg_sandwich", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.ANCIENT_EGG_SANDWICH),false)
    );
    public static final Supplier<Item> HALF_A_ANCIENT_EGG_SANDWICH = register(ANCIENT, "half_a_ancient_egg_sandwich", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.HALF_A_ANCIENT_EGG_SANDWICH),false)
    );
    /// /// feasts
    public static final Supplier<Item> ANCIENT_SOFT_BOILED_EGG_AND_BREADS = register(ANCIENT, "ancient_soft-boiled_egg_and_breads", () ->
            new PlaceableItem(DelightsBlocks.ANCIENT_SOFT_BOILED_EGG_AND_BREADS_BLOCK.get(), basicItem().stacksTo(1))
    );
    public static final Supplier<Item> PLATE_OF_ANCIENT_SOFT_BOILED_EGG_AND_BREAD = register(ANCIENT, "plate_of_ancient_soft-boiled_egg_and_bread", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.PLATE_OF_ANCIENT_SOFT_BOILED_EGG_AND_BREAD), true)
    );

    /// Breaded Delights
    /// /// ingredients
    public static final Supplier<Item> RAW_BREADED_MEAT = register(BREADED, "raw_breaded_meat", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.RAW_BREADED_MEAT))
    );
    public static final Supplier<Item> COOKED_BREADED_MEAT = register(BREADED, "cooked_breaded_meat", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.COOKED_BREADED_MEAT))
    );
    /// /// meals
    public static final Supplier<Item> BREADED_MEAT_SANDWICH = register(BREADED, "breaded_meat_sandwich", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.BREADED_MEAT_SANDWICH))
    );
    public static final Supplier<Item> HALF_A_BREADED_MEAT_SANDWICH = register(BREADED, "half_a_breaded_meat_sandwich", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.HALF_A_BREADED_MEAT_SANDWICH))
    );

    /// Potato Delight
    /// /// ingredients
    public static final Supplier<Item> POTATO_SLICES = register(POTATO, "potato_slices", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.POTATO_SLICES))
    );
    public static final Supplier<Item> BAKED_POTATO_SLICES = register(POTATO, "baked_potato_slices", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.BAKED_POTATO_SLICES))
    );
    public static final Supplier<Item> FRIES_POTATOES = register(POTATO, "fries_potatoes", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.FRIES_POTATOES))
    );
    /// /// meals
    public static final Supplier<Item> POTATO_OMELET = register(POTATO, "potato_omelet", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.POTATO_OMELET), true)
    );
    public static final Supplier<Item> NAPOLITANA_WITH_FRIES_POTATOES = register(POTATO, "napolitana_with_fries_potatoes", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.NAPOLITANA_WITH_FRIES_POTATOES), true)
    );
    public static final Supplier<Item> BREADED_MEAT_WITH_MASHED_POTATO = register(POTATO, "breaded_meat_with_mashed_potato", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.BREADED_MEAT_WITH_MASHED_POTATO), true)
    );
    public static final Supplier<Item> BREADED_MEAT_WITH_PASTA = register(POTATO, "breaded_meat_with_pasta", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.BREADED_MEAT_WITH_PASTA), true)
    );
    public static final Supplier<Item> MASHED_POTATO_WITH_MEATBALLS = register(POTATO, "mashed_potato_with_meatballs", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.MASHED_POTATO_WITH_MEATBALLS), true)
    );
    /// /// feasts
    public static final Supplier<Item> POTATO_AND_MEAT_PIE = register(POTATO, "potato_and_meat_pie", ()  ->
            new PlaceableItem(DelightsBlocks.POTATO_AND_MEAT_PIE_BLOCK.get(), basicItem().stacksTo(1))
    );
    public static final Supplier<Item> PLATE_OF_POTATO_AND_MEAT_PIE = register(POTATO, "plate_of_potato_and_meat_pie", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.PLATE_OF_POTATO_AND_MEAT_PIE), true)
    );
    /// Extra Delights
    /// /// pasta
    public static final Supplier<Item> COOKED_PASTA = register(EXTRA_MEALS, "cooked_pasta", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.COOKED_PASTA))
    );
    /// /// frog
    public static final Supplier<Item> RAW_FROG_LEG = registerFrogLeg("raw_frog_leg", "frog_leg", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.RAW_FROG_LEG))
    );
    public static final Supplier<Item> FROGGLE_RICE_CHOWDER = register(FROG, "froggle_rice_chowder", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.FROGGLE_RICE_CHOWDER),true)
    );
    public static final Supplier<Item> COOKED_FROG_LEG = registerFrogLeg("cooked_frog_leg", "cooked_frog_leg", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.COOKED_FROG_LEG))
    );
    public static final Supplier<Item> FROGGLE_SANDWICH = register(FROG, "froggle_sandwich", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.FROGGLE_SANDWICH))
    );
    public static final Supplier<Item> HALF_A_FROGGLE_SANDWICH = register(FROG, "half_a_froggle_sandwich", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.HALF_A_FROGGLE_SANDWICH))
    );
    public static final Supplier<Item> FROG_LEG_ON_A_STICK = register(FROG, "frog_legs_on_a_stick", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.FROG_LEG_ON_A_STICK))
    );
    public static final Supplier<Item> HONEY_WINGS = register(EXTRA_MEALS, "honey_wings", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.HONEY_WINGS))
    );
    /// /// pig
    public static final Supplier<Item> RAW_SUCKLING_PIG = register(SUCKLING_PIG, "raw_suckling_pig", ()  ->
            new Item(basicItem().stacksTo(1))
    );
    public static final Supplier<Item> SUCKLING_PIG_WITH_VEGETABLES = register(SUCKLING_PIG, "suckling_pig_with_vegetables", ()  ->
            new FatPlaceableItem(DelightsBlocks.SUCKLING_PIG_WITH_VEGETABLES_BLOCK.get(), basicItem().stacksTo(1))
    );
    public static final Supplier<Item> PLATE_OF_MUZZLE_WITH_VEGETABLES = register(SUCKLING_PIG, "plate_of_muzzle_with_vegetables", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.PLATE_OF_MUZZLE_WITH_VEGETABLES), true)
    );
    public static final Supplier<Item> PLATE_OF_HAM_WITH_VEGETABLES = register(SUCKLING_PIG, "plate_of_ham_with_vegetables", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.PLATE_OF_HAM_WITH_VEGETABLES), true)
    );
    public static final Supplier<Item> PLATE_OF_SUCKLING_PIG_WITH_VEGETABLES = register(SUCKLING_PIG, "plate_of_suckling_pig_with_vegetables", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.PLATE_OF_SUCKLING_PIG_WITH_VEGETABLES), true)
    );
    /// /// pops
    public static final Supplier<Item> GLISTERING_MELON_POPSICLE = register(FROZEN_TREATS, "glistering_melon_popsicle", ()  ->
            new PopsicleItem(foodItem(DelightsFoodValues.GLISTERING_MELON_POPSICLE).rarity(Rarity.UNCOMMON))
    );
    public static final Supplier<Item> COCOA_POPSICLE = register(FROZEN_TREATS, "cocoa_popsicle", ()  ->
            new PopsicleItem(foodItem(DelightsFoodValues.COCOA_POPSICLE).rarity(Rarity.UNCOMMON))
    );

    public static final Supplier<Item> MINT_POPSICLE = register(FROZEN_TREATS, "mint_popsicle", ()  ->
            new MintPopsicleItem(foodItem(DelightsFoodValues.MINT_POPSICLE).rarity(Rarity.UNCOMMON), 0.5F, 8.0F)
    );
    public static final Supplier<Item> SWEET_BERRIES_POPSICLE = register(FROZEN_TREATS, "sweet_berries_popsicle", ()  ->
            new PopsicleItem(foodItem(DelightsFoodValues.SWEET_BERRIES_POPSICLE))
    );
    public static final Supplier<Item> GLOW_BERRIES_POPSICLE = register(FROZEN_TREATS, "glow_berries_popsicle", ()  ->
            new PopsicleItem(foodItem(DelightsFoodValues.GLOW_BERRIES_POPSICLE).rarity(Rarity.UNCOMMON))
    );
    public static final Supplier<Item> APPLE_POPSICLE = register(FROZEN_TREATS, "apple_popsicle", ()  ->
            new PopsicleItem(foodItem(DelightsFoodValues.APPLE_POPSICLE))
    );
    public static final Supplier<Item> GOLDEN_APPLE_POPSICLE = register(FROZEN_TREATS, "golden_apple_popsicle", ()  ->
            new PopsicleItem(foodItem(DelightsFoodValues.GOLDEN_APPLE_POPSICLE).rarity(Rarity.UNCOMMON))
    );
    public static final Supplier<Item> TORCH_POPSICLE = register(FROZEN_TREATS, "torch_popsicle", ()  ->
            new PopsicleItem(foodItem(DelightsFoodValues.TORCH_POPSICLE).rarity(Rarity.UNCOMMON))
    );
    /// /// IceCream
    public static final Supplier<Item> ICE_CREAM_IN_A_PUMPKIN_HEAD = register(FROZEN_TREATS, "ice_cream_in_a_pumpkin_head", ()  ->
            new BlockItem(DelightsBlocks.ICE_CREAM_IN_A_PUMPKIN_HEAD_BLOCK.get(), basicItem().stacksTo(1).rarity(Rarity.UNCOMMON))
    );
    public static final Supplier<Item> PUMPKIN_HEAD_SLICE = register(FROZEN_TREATS, "pumpkin_head_slice", ()  ->
            new PopsicleItem(foodItem(DelightsFoodValues.PUMPKIN_HEAD_SLICE).rarity(Rarity.UNCOMMON))
    );
    /// /// meals
    public static final Supplier<Item> BEET_AND_EGG_SALAD = register(EXTRA_MEALS, "beet_and_egg_salad", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.BEET_AND_EGG_SALAD), false)
    );
    public static final Supplier<Item> PASTA_WITH_BEET = register(EXTRA_MEALS, "pasta_with_beet", ()  ->
            new ConsumableItem(bowlFoodItem(DelightsFoodValues.PASTA_WITH_BEET), true)
    );
    public static final Supplier<Item> BEET_JUICE = register(EXTRA_MEALS, "beet_juice", ()  ->
            new DrinkableItem(drinkItem().food(DelightsFoodValues.BEET_JUICE), true, false)
    );
    /// /// half sandwich
    public static final Supplier<Item> HALF_A_BACON_SANDWICH = register(SANDWICH_PORTIONS, "half_a_bacon_sandwich", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.HALF_A_BACON_SANDWICH))
    );
    public static final Supplier<Item> HALF_A_EGG_SANDWICH = register(SANDWICH_PORTIONS, "half_a_egg_sandwich", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.HALF_A_EGG_SANDWICH))
    );
    public static final Supplier<Item> HALF_A_CHICKEN_SANDWICH = register(SANDWICH_PORTIONS, "half_a_chicken_sandwich", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.HALF_A_CHICKEN_SANDWICH))
    );
    public static final Supplier<Item> HALF_A_HAMBURGER = register(SANDWICH_PORTIONS, "half_a_hamburger", ()  ->
            new ConsumableItem(foodItem(DelightsFoodValues.HALF_A_HAMBURGER))
    );

    private static Supplier<Item> register(DelightGroup group, String name, Supplier<Item> item) {
        if (DelightsConfig.isGroupEnabled(group)) {
            return ITEMS.register(name, item);
        }
        return () -> {
            throw new IllegalStateException("Tutta's Delights content group is disabled: " + group.configKey());
        };
    }

    private static Supplier<Item> registerFrogLeg(String name, String replacementName, Supplier<Item> item) {
        if (!DelightsConfig.isGroupEnabled(FROG)) {
            return () -> {
                throw new IllegalStateException("Tutta's Delights content group is disabled: " + FROG.configKey());
            };
        }
        if (Mods.NOMANSLAND.isLoaded() && !DatagenModLoader.isRunningDataGen()) {
            return () -> BuiltInRegistries.ITEM.get(Mods.NOMANSLAND.location(replacementName));
        }
        return ITEMS.register(name, item);
    }
}
