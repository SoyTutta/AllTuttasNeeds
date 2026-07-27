package com.alltuttasneeds.delights;

import com.alltuttasneeds.delights.DelightsModule;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class DelightsTags {
    public static final TagKey<Item> FROG_LEGS = modItemTag("foods/frog_legs");
    public static final TagKey<Item> RAW_FROG_LEGS = modItemTag("foods/raw_frog_legs");
    public static final TagKey<Item> COOKED_FROG_LEGS = modItemTag("foods/cooked_frog_legs");

    public static final TagKey<Item> SLIME_JELLY_BLOCKS = modItemTag("slime_jelly_blocks");

    private static TagKey<Item> modItemTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(DelightsModule.MODID, path));
    }

    private static TagKey<Block> modBlockTag(String path) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(DelightsModule.MODID, path));
    }

    private static TagKey<EntityType<?>> modEntityTag(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(DelightsModule.MODID, path));
    }

}