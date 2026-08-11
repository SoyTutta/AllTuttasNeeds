package com.alltuttasneeds.beds;

import com.alltuttasneeds.beds.loot.BlanketDropLootModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class BedsLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, "tuttasbeds");

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> BLANKET_DROP =
            LOOT_MODIFIERS.register("blanket_drop", BlanketDropLootModifier.CODEC);

    private BedsLootModifiers() {}
}
