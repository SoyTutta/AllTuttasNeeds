package com.alltuttasneeds.beds;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class TBTags {
    public static final TagKey<Item> BASIC_TIER_BEDS = itemTag("tooltip/basic_tier");
    public static final TagKey<Item> LOW_TIER_BEDS = itemTag("tooltip/low_tier");
    public static final TagKey<Item> NORMAL_TIER_BEDS = itemTag("tooltip/normal_tier");
    public static final TagKey<Item> DELUXE_TIER_BEDS = itemTag("tooltip/deluxe_tier");

    private TBTags() {}

    private static TagKey<Item> itemTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("tuttasbeds", path));
    }
}
