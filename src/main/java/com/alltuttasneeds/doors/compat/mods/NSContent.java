package com.alltuttasneeds.doors.compat.mods;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.doors.compat.CompatRegistrar;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.WoodFamily;
import net.hibiscus.naturespirit.registration.NSBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.doors.compat.CompatRegistrar.setTypeFromDoor;
import static com.alltuttasneeds.doors.compat.DoorVariant.*;

public final class NSContent implements ModCompat {
    public static final NSContent INSTANCE = new NSContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "natures_spirit");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "natures_spirit");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("natures_spirit:aspen", "aspen",
                    setTypeFromDoor(NSBlocks.ASPEN.getDoor()), NSBlocks.ASPEN.getDoor(),
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:cedar", "cedar",
                    setTypeFromDoor(NSBlocks.CEDAR.getDoor()), NSBlocks.CEDAR.getDoor(),
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:coconut", "coconut",
                    setTypeFromDoor(NSBlocks.COCONUT.getDoor()), NSBlocks.COCONUT.getDoor(),
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:cypress", "cypress",
                    setTypeFromDoor(NSBlocks.CYPRESS.getDoor()), NSBlocks.CYPRESS.getDoor(),
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:fir", "fir",
                    setTypeFromDoor(NSBlocks.FIR.getDoor()), NSBlocks.FIR.getDoor(),
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:ghaf", "ghaf",
                    setTypeFromDoor(NSBlocks.GHAF.getDoor()), NSBlocks.GHAF.getDoor(),
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:joshua", "joshua",
                    setTypeFromDoor(NSBlocks.JOSHUA.getDoor()), NSBlocks.JOSHUA.getDoor(),
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:larch", "larch",
                    setTypeFromDoor(NSBlocks.LARCH.getDoor()), NSBlocks.LARCH.getDoor(),
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:mahogany", "mahogany",
                    setTypeFromDoor(NSBlocks.MAHOGANY.getDoor()), NSBlocks.MAHOGANY.getDoor(),
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:maple", "maple",
                    setTypeFromDoor(NSBlocks.MAPLE.getDoor()), NSBlocks.MAPLE.getDoor(),
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:olive", "olive",
                    setTypeFromDoor(NSBlocks.OLIVE.getDoor()), NSBlocks.OLIVE.getDoor(),
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:palo_verde", "palo_verde",
                    setTypeFromDoor(NSBlocks.PALO_VERDE.getDoor()), NSBlocks.PALO_VERDE.getDoor(),
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:redwood", "redwood",
                    setTypeFromDoor(NSBlocks.REDWOOD.getDoor()), NSBlocks.REDWOOD.getDoor(),
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:saxaul", "saxaul",
                    setTypeFromDoor(NSBlocks.SAXAUL.getDoor()), NSBlocks.SAXAUL.getDoor(),
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:sugi", "sugi",
                    setTypeFromDoor(NSBlocks.SUGI.getDoor()), NSBlocks.SUGI.getDoor(),
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:willow", "willow",
                    setTypeFromDoor(NSBlocks.WILLOW.getDoor()), NSBlocks.WILLOW.getDoor(),
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("natures_spirit:wisteria", "wisteria",
                    setTypeFromDoor(NSBlocks.WISTERIA.getDoor()), NSBlocks.WISTERIA.getDoor(),
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
    }

    private NSContent() {}

    @Override public Mods mod() { return Mods.NATURES_SPIRIT; }
    @Override public String namespace() { return "natures_spirit"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
}
