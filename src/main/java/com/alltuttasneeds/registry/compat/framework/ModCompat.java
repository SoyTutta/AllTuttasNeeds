package com.alltuttasneeds.registry.compat.framework;

import com.alltuttasneeds.registry.compat.Mods;
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

    /** Registry namespace these blocks/items are registered under. */
    String namespace();

    DeferredRegister<Block> blocks();
    DeferredRegister<Item> items();

    /** Every door block registered by this compat, keyed by registry name (no namespace). */
    Map<String, Supplier<? extends Block>> doors();

    /** Every door item registered by this compat, keyed by registry name (no namespace). */
    Map<String, Supplier<Item>> doorItems();

    default List<WoodFamily> woodFamilies()             { return List.of(); }
    default List<SecretDoorFamily> secretDoorFamilies() { return List.of(); }
    default List<ExtraDoor> extraDoors()                { return List.of(); }

    default boolean isLoaded() { return mod().isLoaded(); }

    /**
     * Builds a ResourceLocation under this compat's registry namespace.
     * <p>
     * Use this instead of {@code mod().location(name)} whenever constructing resource
     * locations for blocks/items registered by this compat. The registry namespace
     * ({@link #namespace()}) may differ from the mod ID — e.g. TDContent registers
     * under {@code "tuttasdoors"} while its mod ID is {@code "alltuttasneeds"}.
     */
    default ResourceLocation registryLocation(String name) {
        return ResourceLocation.fromNamespaceAndPath(namespace(), name);
    }

    /**
     * False when this compat generates its own blockstates externally and ATN must not
     * produce conflicting ones.
     */
    default boolean requiresBlockStateGeneration() { return true; }

    /**
     * Render type for a specific door name. Defaults to {@code "cutout"}.
     */
    default String renderTypeKey(String doorName) { return "cutout"; }

    /**
     * Recipe-folder prefix (e.g. {@code "crafting/"} or {@code "wood/<name>/"}) a wood
     * family's generated recipe IDs should be nested under, to avoid colliding with that
     * mod's own recipes. {@code null} (the default) keeps recipes at the namespace root.
     */
    default String recipeFolder(WoodFamily family) { return null; }

    /** Copper-weathering chains (oxidation + waxing) this compat contributes, if any. */
    default List<WeatheringDoorChain.Link> weatheringLinks() { return List.of(); }

    default void registerToBus(IEventBus modEventBus) {
        blocks().register(modEventBus);
        items().register(modEventBus);
    }
}