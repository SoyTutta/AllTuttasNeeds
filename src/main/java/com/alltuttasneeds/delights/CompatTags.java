package com.alltuttasneeds.delights;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CompatTags {

    public static final TagKey<Item> FOOD_CHEESE_WEDGE = externalItemTag("brewinandchewin", "foods/cheese_wedge");
    public static final TagKey<Item> FOOD_HORROR_MEAT = externalItemTag("brewinandchewin", "foods/horror_meat");
    public static final TagKey<Item> FOOD_PIZZA_TOPPING = externalItemTag("brewinandchewin", "foods/pizza_topping");
    public static final TagKey<Item> FOOD_JERKY_MEAT = externalItemTag("brewinandchewin", "foods/jerky_meat");
    public static final TagKey<Item> BOILED_EGG_CANDIDATE = externalItemTag("mynethersdelight", "boiled_egg_candidate");

    public CompatTags() {
    }

    private static TagKey<Item> externalItemTag(String modId, String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(modId, path));
    }
}
