package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.doors.compat.DoorVariant;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.WoodFamily;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

final class RecipeIngredients {

    private RecipeIngredients() {}

    static ItemLike getDoorItem(WoodFamily family, ModCompat compat, DoorVariant variant) {
        return getItemLike(compat.namespace() + ":" + family.registryName() + "_" + variant.suffix());
    }

    static ItemLike getItemLike(String id) {
        if (id == null || id.isBlank() || id.startsWith("#")) return Items.AIR;
        ResourceLocation rl = ResourceLocation.tryParse(id);
        if (rl == null) return Items.AIR;
        return () -> BuiltInRegistries.ITEM.getOptional(rl).orElse(Items.AIR);
    }
}