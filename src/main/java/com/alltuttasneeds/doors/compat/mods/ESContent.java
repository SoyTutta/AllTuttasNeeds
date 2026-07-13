package com.alltuttasneeds.doors.compat.mods;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.doors.compat.CompatRegistrar;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.WoodFamily;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.doors.compat.DoorVariant.*;

public final class ESContent implements ModCompat {
    public static final ESContent INSTANCE = new ESContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "enderscape");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "enderscape");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("enderscape:veiled", "veiled",
                    () -> EnderscapeBlocks.VEILED_BLOCK_SET, EnderscapeBlocks.VEILED_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("enderscape:celestial", "celestial",
                    () -> EnderscapeBlocks.CELESTIAL_BLOCK_SET, EnderscapeBlocks.CELESTIAL_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("enderscape:murublight", "murublight",
                    () -> EnderscapeBlocks.MURUBLIGHT_BLOCK_SET, EnderscapeBlocks.MURUBLIGHT_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
    }

    private ESContent() {}

    @Override public Mods mod() { return Mods.ENDERSCAPE; }
    @Override public String namespace() { return "enderscape"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
}
