package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.registry.compat.framework.CompatRegistrar;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.WoodFamily;
import com.blackgear.vanillabackport.common.registries.ModBlockSetTypes;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.registry.compat.framework.DoorVariant.*;

/** Vanilla Backport door compatibility. */
public final class VBContent implements ModCompat {
    public static final VBContent INSTANCE = new VBContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "tuttasdoors");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "tuttasdoors");

    public static final Map<String, Supplier<? extends Block>> DOORS = new HashMap<>();
    public static final Map<String, Supplier<Item>> DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily(
                    "minecraft:pale_oak",
                    "pale_oak",
                    () -> ModBlockSetTypes.PALE_OAK,
                    ModBlocks.PALE_OAK_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)
            )
    );

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
    }

    private VBContent() {}

    @Override public Mods mod() { return Mods.VANILLABACKPORT; }
    @Override public String namespace() { return "tuttasdoors"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
}