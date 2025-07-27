package com.alltuttasneeds.registry;

import biomesoplenty.api.block.BOPBlocks;
import com.alltuttasneeds.AllTuttasNeeds;
import com.alltuttasneeds.registry.compat.*;
import com.farcr.nomansland.common.registry.blocks.NMLBlocks;
import com.alltuttasneeds.ATNTextUtils;
import com.kekecreations.arts_and_crafts.core.registry.ACBlocks;
import com.kryptography.newworld.init.NWBlocks;
import com.sammy.malum.registry.common.block.MalumBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.hibiscus.naturespirit.registration.NSBlocks;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TDCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AllTuttasNeeds.MODID);

    record DoorFamily(Supplier<ItemLike> originalDoor, Map<String, Supplier<Item>> variants) {}

    private static final Map<String, DoorFamily> DOOR_FAMILIES = new LinkedHashMap<>();

    private static void accept(CreativeModeTab.Output output, Supplier<? extends ItemLike> item) {
        if (item != null && item.get() != null) {
            output.accept(item.get());
        }
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TUTTAS_DOORS = TABS.register("tuttasdoors",
            () -> CreativeModeTab.builder()
                    .title(ATNTextUtils.getTranslation("itemGroup.tuttasdoors"))
                    .icon(() -> new ItemStack(TDContent.ICON_ITEM.get()))
                    .displayItems((parameters, output) -> {
                        populateDoorFamilies();

                        for (DoorFamily family : DOOR_FAMILIES.values()) {
                            for (Supplier<Item> itemSupplier : family.variants().values()) {
                                accept(output, itemSupplier);
                            }
                        }

                        accept(output, TDContent.DOOR_ITEMS.get("iron_bars_sliding_door"));
                        if (Mods.CREATE.isLoaded()) {
                            accept(output, CreateContent.DOOR_ITEMS.get("andesite_bars_sliding_door"));
                            accept(output, CreateContent.DOOR_ITEMS.get("brass_bars_sliding_door"));
                            accept(output, CreateContent.DOOR_ITEMS.get("copper_bars_sliding_door"));
                        }
                        if (Mods.BLOCKBOX.isLoaded()) {
                            List<String> blockBoxOrder = List.of(
                                    "copper_bars_sliding_door",
                                    "exposed_copper_bars_sliding_door",
                                    "weathered_copper_bars_sliding_door",
                                    "oxidized_copper_bars_sliding_door",

                                    "waxed_copper_bars_sliding_door",
                                    "waxed_exposed_copper_bars_sliding_door",
                                    "waxed_weathered_copper_bars_sliding_door",
                                    "waxed_oxidized_copper_bars_sliding_door",

                                    "golden_bars_sliding_door"
                            );

                            for (String key : blockBoxOrder) {
                                if (BBContent.DOOR_ITEMS.containsKey(key)) {
                                    accept(output, BBContent.DOOR_ITEMS.get(key));
                                }
                            }
                        }
                    })
                    .build()
    );

    private static void populateDoorFamilies() {
        DOOR_FAMILIES.clear();

        addFamily("minecraft:oak",      "oak",      () -> Items.OAK_DOOR,      TDContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
        addFamily("minecraft:spruce",   "spruce",   () -> Items.SPRUCE_DOOR,   TDContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
        addFamily("minecraft:birch",    "birch",    () -> Items.BIRCH_DOOR,    TDContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
        addFamily("minecraft:jungle",   "jungle",   () -> Items.JUNGLE_DOOR,   TDContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
        addFamily("minecraft:acacia",   "acacia",   () -> Items.ACACIA_DOOR,   TDContent.DOOR_ITEMS, List.of("discrete", "normal", "original", "transit", "pet"));
        addFamily("minecraft:dark_oak", "dark_oak", () -> Items.DARK_OAK_DOOR, TDContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
        addFamily("minecraft:mangrove", "mangrove", () -> Items.MANGROVE_DOOR, TDContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
        addFamily("minecraft:cherry",   "cherry",   () -> Items.CHERRY_DOOR,   TDContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
        addFamily("minecraft:bamboo",   "bamboo",   () -> Items.BAMBOO_DOOR,   TDContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
        addFamily("minecraft:crimson",  "crimson",  () -> Items.CRIMSON_DOOR,  TDContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
        addFamily("minecraft:warped",   "warped",   () -> Items.WARPED_DOOR,   TDContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));

        if (Mods.MYNETHERSDELIGHT.isLoaded()) {
            addFamily("mynethersdelight:powdery", "powdery", MNDItems.POWDERY_DOOR, MNDContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
        }
        if (Mods.NEWWORLD.isLoaded()) {
            addFamily("newworld:fir", "fir", NWBlocks.FIR_DOOR, NWContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
        }
        if (Mods.ARTS_AND_CRAFTS.isLoaded()) {
            addFamily("artsandcrafts:cork", "cork", ACBlocks.CORK_DOOR, ACContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
        }
        if (Mods.ENDERSCAPE.isLoaded()) {
            addFamily("enderscape:veiled",    "veiled",    EnderscapeBlocks.VEILED_DOOR,     ESContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("enderscape:celestial", "celestial", EnderscapeBlocks.CELESTIAL_DOOR,  ESContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("enderscape:murublight","murublight",EnderscapeBlocks.MURUBLIGHT_DOOR, ESContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
        }
        if (Mods.NOMANSLAND.isLoaded()) {
            addFamily("nomansland:pine",   "pine",   NMLBlocks.PINE.door(),   NMLContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("nomansland:willow", "willow", NMLBlocks.WILLOW.door(), NMLContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("nomansland:walnut", "walnut", NMLBlocks.WALNUT.door(), NMLContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("nomansland:maple",  "maple",  NMLBlocks.MAPLE.door(),  NMLContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
        }

        if (Mods.MALUM.isLoaded()) {
            addFamily("malum:runewood", "runewood", MalumBlocks.RUNEWOOD_DOOR, MalumContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("malum:bolted_runewood", "bolted_runewood", MalumBlocks.BOLTED_RUNEWOOD_DOOR, MalumContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("malum:runewood_boards", "runewood_boards", MalumBlocks.RUNEWOOD_BOARDS_DOOR, MalumContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("malum:bolted_runewood_boards", "bolted_runewood_boards", MalumBlocks.BOLTED_RUNEWOOD_BOARDS_DOOR, MalumContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));

            addFamily("malum:soulwood", "soulwood", MalumBlocks.SOULWOOD_DOOR, MalumContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("malum:bolted_soulwood", "bolted_soulwood", MalumBlocks.BOLTED_SOULWOOD_DOOR, MalumContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("malum:soulwood_boards", "soulwood_boards", MalumBlocks.SOULWOOD_BOARDS_DOOR, MalumContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("malum:bolted_soulwood_boards", "bolted_soulwood_boards", MalumBlocks.BOLTED_SOULWOOD_BOARDS_DOOR, MalumContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
        }

        if (Mods.BIOMESOPLENTY.isLoaded()) {
            addFamily("biomesoplenty:fir",       "fir",       () -> BOPBlocks.FIR_DOOR,       BoPContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("biomesoplenty:pine",      "pine",      () -> BOPBlocks.PINE_DOOR,      BoPContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("biomesoplenty:maple",     "maple",     () -> BOPBlocks.MAPLE_DOOR,     BoPContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("biomesoplenty:redwood",   "redwood",   () -> BOPBlocks.REDWOOD_DOOR,   BoPContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("biomesoplenty:mahogany",  "mahogany",  () -> BOPBlocks.MAHOGANY_DOOR,  BoPContent.DOOR_ITEMS, List.of("discrete", "normal",   "original", "transit", "pet"));
            addFamily("biomesoplenty:jacaranda", "jacaranda", () -> BOPBlocks.JACARANDA_DOOR, BoPContent.DOOR_ITEMS, List.of("discrete", "normal",   "original", "transit", "pet"));
            addFamily("biomesoplenty:palm",      "palm",      () -> BOPBlocks.PALM_DOOR,      BoPContent.DOOR_ITEMS, List.of("discrete", "normal",   "original", "transit", "pet"));
            addFamily("biomesoplenty:willow",    "willow",    () -> BOPBlocks.WILLOW_DOOR,    BoPContent.DOOR_ITEMS, List.of("discrete", "normal",   "original", "transit", "pet"));
            addFamily("biomesoplenty:dead",      "dead",      () -> BOPBlocks.DEAD_DOOR,      BoPContent.DOOR_ITEMS, List.of("discrete", "normal",   "original", "transit", "pet"));
            addFamily("biomesoplenty:magic",     "magic",     () -> BOPBlocks.MAGIC_DOOR,     BoPContent.DOOR_ITEMS, List.of("discrete", "normal",   "original", "transit", "pet"));
            addFamily("biomesoplenty:umbran",    "umbran",    () -> BOPBlocks.UMBRAN_DOOR,    BoPContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("biomesoplenty:hellbark",  "hellbark",  () -> BOPBlocks.HELLBARK_DOOR,  BoPContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("biomesoplenty:empyreal",  "empyreal",  () -> BOPBlocks.EMPYREAL_DOOR,  BoPContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
        }
        if (Mods.NATURES_SPIRIT.isLoaded()) {
            addFamily("natures_spirit:aspen",      "aspen",      NSBlocks.ASPEN.getDoor(),      NSContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:cedar",      "cedar",      NSBlocks.CEDAR.getDoor(),      NSContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:coconut",    "coconut",    NSBlocks.COCONUT.getDoor(),    NSContent.DOOR_ITEMS, List.of("discrete", "normal", "original", "transit", "pet"));
            addFamily("natures_spirit:cypress",    "cypress",    NSBlocks.CYPRESS.getDoor(),    NSContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:fir",        "fir",        NSBlocks.FIR.getDoor(),        NSContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:ghaf",       "ghaf",       NSBlocks.GHAF.getDoor(),       NSContent.DOOR_ITEMS, List.of("discrete", "normal", "original", "transit", "pet"));
            addFamily("natures_spirit:joshua",     "joshua",     NSBlocks.JOSHUA.getDoor(),     NSContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:larch",      "larch",      NSBlocks.LARCH.getDoor(),      NSContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:mahogany",   "mahogany",   NSBlocks.MAHOGANY.getDoor(),   NSContent.DOOR_ITEMS, List.of("discrete", "normal", "original", "transit", "pet"));
            addFamily("natures_spirit:maple",      "maple",      NSBlocks.MAPLE.getDoor(),      NSContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:olive",      "olive",      NSBlocks.OLIVE.getDoor(),      NSContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:palo_verde", "palo_verde", NSBlocks.PALO_VERDE.getDoor(), NSContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:redwood",    "redwood",    NSBlocks.REDWOOD.getDoor(),    NSContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:saxaul",     "saxaul",     NSBlocks.SAXAUL.getDoor(),     NSContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:sugi",       "sugi",       NSBlocks.SUGI.getDoor(),       NSContent.DOOR_ITEMS, List.of("original", "normal", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:willow",     "willow",     NSBlocks.WILLOW.getDoor(),     NSContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
            addFamily("natures_spirit:wisteria",   "wisteria",   NSBlocks.WISTERIA.getDoor(),   NSContent.DOOR_ITEMS, List.of("discrete", "original", "indiscrete", "transit", "pet"));
        }
    }

    private static void addFamily(String familyId, String woodName, Supplier<? extends ItemLike> originalDoor, Map<String, ? extends Supplier<? extends Item>> variantMap, List<String> displayOrder) {
        if (DOOR_FAMILIES.containsKey(familyId)) {
            return;
        }

        Map<String, Supplier<Item>> variants = new LinkedHashMap<>();
        for (String type : displayOrder) {
            if ("original".equals(type)) {
                variants.put(woodName + "_original", () -> originalDoor.get().asItem());
            } else {
                String key = woodName + "_" + type + "_door";
                if (variantMap.containsKey(key)) {
                    variants.put(key, (Supplier<Item>) variantMap.get(key));
                }
            }
        }

        DOOR_FAMILIES.put(familyId, new DoorFamily(() -> originalDoor.get().asItem(), variants));
    }
}