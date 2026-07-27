package com.alltuttasneeds.core.condition;

import com.alltuttasneeds.AllTuttasNeeds;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ATNConditions {
    private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, AllTuttasNeeds.MODID);

    public static final Supplier<MapCodec<ModuleEnabledCondition>> MODULE_ENABLED =
            CONDITION_CODECS.register("module_enabled", () -> ModuleEnabledCondition.CODEC);
    public static final Supplier<MapCodec<DoorSetEnabledCondition>> DOOR_SET_ENABLED =
            CONDITION_CODECS.register("door_set_enabled", () -> DoorSetEnabledCondition.CODEC);
    public static final Supplier<MapCodec<DelightGroupEnabledCondition>> DELIGHT_GROUP_ENABLED =
            CONDITION_CODECS.register("delight_group_enabled", () -> DelightGroupEnabledCondition.CODEC);
    public static final Supplier<MapCodec<DelightRecipeEnabledCondition>> DELIGHT_RECIPE_ENABLED =
            CONDITION_CODECS.register("delight_recipe_enabled", () -> DelightRecipeEnabledCondition.CODEC);

    private ATNConditions() {}

    public static void register(IEventBus modEventBus) {
        CONDITION_CODECS.register(modEventBus);
    }
}
