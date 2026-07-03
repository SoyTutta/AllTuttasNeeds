package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.registry.compat.framework.CompatRegistrar;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.WoodFamily;
import com.ninni.spawn.registry.SpawnBlockSetType;
import com.ninni.spawn.registry.SpawnBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.registry.compat.framework.DoorVariant.*;

/** Spawn door compatibility. */
public final class SContent implements ModCompat {
    public static final SContent INSTANCE = new SContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "spawn");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "spawn");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("spawn:rotten", "rotten",
                    () -> SpawnBlockSetType.ROTTEN, SpawnBlocks.ROTTEN_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("spawn:date", "date",
                    () -> SpawnBlockSetType.DATE, SpawnBlocks.DATE_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
    }

    private SContent() {}

    @Override public Mods mod() { return Mods.SPAWN; }
    @Override public String namespace() { return "spawn"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
}
