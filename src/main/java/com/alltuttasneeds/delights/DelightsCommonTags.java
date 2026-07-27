package com.alltuttasneeds.delights;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class DelightsCommonTags {
    public static final TagKey<Item> FOODS_COOKED_POTATO= commonItemTag("foods/cooked_potato");

    public static final TagKey<Item> FOODS_BOILED_EGG = commonItemTag("foods/boiled_egg");

    public static final TagKey<Item> FROG_MEAT = commonItemTag("foods/frog_meat");
    public static final TagKey<Item> RAW_FROG_MEAT = commonItemTag("foods/raw_frog_meat");
    public static final TagKey<Item> COOKED_FROG_MEAT = commonItemTag("foods/cooked_frog_meat");

    public static final TagKey<Item> ANY_GUARDIAN_MEATS = commonItemTag("foods/any_guardian_meats");
    public static final TagKey<Item> ANY_GUARDIAN_TAILS = commonItemTag("foods/any_guardian_tails");

    public static final TagKey<Item> GUARDIAN_TAILS = commonItemTag("foods/guardian_tails");
    public static final TagKey<Item> ELDER_GUARDIAN_TAILS = commonItemTag("foods/elder_guardian_tails");

    public static final TagKey<Item> RAW_GUARDIAN_MEAT = commonItemTag("foods/raw_guardian_meat");
    public static final TagKey<Item> COOKED_GUARDIAN_MEAT = commonItemTag("foods/cooked_guardian_meat");
    public static final TagKey<Item> RAW_ELDER_GUARDIAN_MEAT = commonItemTag("foods/raw_elder_guardian_meat");
    public static final TagKey<Item> COOKED_ELDER_GUARDIAN_MEAT = commonItemTag("foods/cooked_elder_guardian_meat");

    public static final TagKey<Item> ROTTEN_MEALS = commonItemTag("foods/rotten_meals");
    public static final TagKey<Item> ROTTEN_VEGETABLES = commonItemTag("foods/rotten_vegetables");

    private static TagKey<Item> commonItemTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
    }
}