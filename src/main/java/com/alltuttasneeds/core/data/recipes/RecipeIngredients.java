package com.alltuttasneeds.core.data.recipes;

import com.alltuttasneeds.registry.compat.framework.DoorVariant;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.WoodFamily;
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