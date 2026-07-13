package com.alltuttasneeds.doors.compat.mods;

import com.alltuttasneeds.core.Mods;
import biomesoplenty.api.block.BOPBlocks;
import com.alltuttasneeds.doors.compat.CompatRegistrar;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.WoodFamily;
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

public final class BoPContent implements ModCompat {
    public static final BoPContent INSTANCE = new BoPContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "biomesoplenty");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "biomesoplenty");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("biomesoplenty:fir", "fir",
                    setTypeFromDoor(() -> BOPBlocks.FIR_DOOR), () -> BOPBlocks.FIR_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("biomesoplenty:pine", "pine",
                    setTypeFromDoor(() -> BOPBlocks.PINE_DOOR), () -> BOPBlocks.PINE_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("biomesoplenty:maple", "maple",
                    setTypeFromDoor(() -> BOPBlocks.MAPLE_DOOR), () -> BOPBlocks.MAPLE_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("biomesoplenty:redwood", "redwood",
                    setTypeFromDoor(() -> BOPBlocks.REDWOOD_DOOR), () -> BOPBlocks.REDWOOD_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("biomesoplenty:mahogany", "mahogany",
                    setTypeFromDoor(() -> BOPBlocks.MAHOGANY_DOOR), () -> BOPBlocks.MAHOGANY_DOOR,
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("biomesoplenty:jacaranda", "jacaranda",
                    setTypeFromDoor(() -> BOPBlocks.JACARANDA_DOOR), () -> BOPBlocks.JACARANDA_DOOR,
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("biomesoplenty:palm", "palm",
                    setTypeFromDoor(() -> BOPBlocks.PALM_DOOR), () -> BOPBlocks.PALM_DOOR,
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("biomesoplenty:willow", "willow",
                    setTypeFromDoor(() -> BOPBlocks.WILLOW_DOOR), () -> BOPBlocks.WILLOW_DOOR,
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("biomesoplenty:dead", "dead",
                    setTypeFromDoor(() -> BOPBlocks.DEAD_DOOR), () -> BOPBlocks.DEAD_DOOR,
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("biomesoplenty:magic", "magic",
                    setTypeFromDoor(() -> BOPBlocks.MAGIC_DOOR), () -> BOPBlocks.MAGIC_DOOR,
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("biomesoplenty:umbran", "umbran",
                    setTypeFromDoor(() -> BOPBlocks.UMBRAN_DOOR), () -> BOPBlocks.UMBRAN_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("biomesoplenty:hellbark", "hellbark",
                    setTypeFromDoor(() -> BOPBlocks.HELLBARK_DOOR), () -> BOPBlocks.HELLBARK_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR),
                    true),
            new WoodFamily("biomesoplenty:empyreal", "empyreal",
                    setTypeFromDoor(() -> BOPBlocks.EMPYREAL_DOOR), () -> BOPBlocks.EMPYREAL_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
    }

    private BoPContent() {}

    @Override public Mods mod() { return Mods.BIOMESOPLENTY; }
    @Override public String namespace() { return "biomesoplenty"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }

    @Override
    public String renderTypeKey(String doorName) {
        return doorName.startsWith("magic_") ? "translucent" : "cutout";
    }
}
