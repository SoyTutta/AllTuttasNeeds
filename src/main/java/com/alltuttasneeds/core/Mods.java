package com.alltuttasneeds.core;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

public enum Mods {
    ALLTUTTASNEEDS  ("alltuttasneeds"),
    VANILLABACKPORT  ("vanillabackport"),
    ABUNDANT_ATMOSPHERE("abundant_atmosphere"),
    ARCHITECTS_PALETTE ("architects_palette"),
    ARTS_AND_CRAFTS    ("arts_and_crafts"),
    BIOMESOPLENTY      ("biomesoplenty"),
    BLOCKBOX           ("blockbox"),
    CREATE             ("create"),
    ENDERSCAPE         ("enderscape"),
    MALUM              ("malum"),
    MYNETHERSDELIGHT   ("mynethersdelight"),
    NATURES_SPIRIT     ("natures_spirit"),
    NEWWORLD           ("newworld"),
    NOMANSLAND         ("nomansland"),
    CAVERNS_AND_CHASMS ("caverns_and_chasms"),
    UPGRADE_AQUATIC ("upgrade_aquatic"),
    AUTUMNITY ("autumnity"),
    ENVIRONMENTAL ("environmental"),
    ATMOSPHERIC ("atmospheric"),
    SPAWN ("spawn"),
    WOODWORKS ("woodworks"),
    FARMERS_DELIGHT ("farmersdelight");

    private final String id;

    Mods(String id) {
        this.id = id;
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(id);
    }

    public String id() {
        return id;
    }

    public ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(id, path);
    }
}
