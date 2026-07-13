package com.alltuttasneeds.doors.compat;

import com.alltuttasneeds.core.Mods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface ModCompat {

    Mods mod();

    String namespace();

    DeferredRegister<Block> blocks();
    DeferredRegister<Item> items();

    Map<String, Supplier<? extends Block>> doors();

    Map<String, Supplier<Item>> doorItems();

    default List<WoodFamily> woodFamilies()             { return List.of(); }
    default List<SecretDoorFamily> secretDoorFamilies() { return List.of(); }
    default List<ExtraDoor> extraDoors()                { return List.of(); }

    default boolean isLoaded() { return mod().isLoaded(); }

    default ResourceLocation registryLocation(String name) {
        return ResourceLocation.fromNamespaceAndPath(namespace(), name);
    }

    default boolean requiresBlockStateGeneration() { return true; }

    default String renderTypeKey(String doorName) { return "cutout"; }

    default String recipeFolder(WoodFamily family) { return null; }

    default List<WeatheringDoorChain.Link> weatheringLinks() { return List.of(); }

    default void registerToBus(IEventBus modEventBus) {
        blocks().register(modEventBus);
        items().register(modEventBus);
    }
}