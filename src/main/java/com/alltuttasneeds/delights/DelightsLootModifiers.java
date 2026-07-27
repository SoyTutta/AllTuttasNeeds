package com.alltuttasneeds.delights;

import com.mojang.serialization.MapCodec;
import com.alltuttasneeds.delights.DelightsModule;
import com.alltuttasneeds.delights.loot.ReplaceLootModifier;
import com.alltuttasneeds.delights.loot.AddNamedItemLootModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class DelightsLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, DelightsModule.MODID);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> REMPLACE_LOOT =
            LOOT_MODIFIERS.register("remplace_item", ReplaceLootModifier.CODEC);
    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_NAMED_ITEM =
            LOOT_MODIFIERS.register("add_named_item", AddNamedItemLootModifier.CODEC);
}
