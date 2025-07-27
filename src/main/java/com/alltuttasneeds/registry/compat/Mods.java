package com.alltuttasneeds.registry.compat;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

public enum Mods {
    ARTS_AND_CRAFTS,
    BIOMESOPLENTY,
    BLOCKBOX,
    CREATE,
    ENDERSCAPE,
    MYNETHERSDELIGHT,
    MALUM,
    NATURES_SPIRIT,
    NEWWORLD,
    NOMANSLAND,
    TUTTASDOORS;
    private final String id;

    Mods() {
        id = name().toLowerCase();
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(id);
    }

    public ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(id, path);
    }
}