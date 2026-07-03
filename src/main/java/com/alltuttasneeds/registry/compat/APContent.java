package com.alltuttasneeds.registry.compat;

import architectspalette.core.registry.APBlocks;
import com.alltuttasneeds.blocks.SlidingDoorBlock;
import com.alltuttasneeds.registry.compat.framework.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static architectspalette.core.registry.APBlocks.ENTWINE;
import static architectspalette.core.registry.APBlocks.SUNMETAL;
import static com.alltuttasneeds.registry.compat.framework.DoorVariant.*;

/** Architects Palette door compatibility. */
public final class APContent implements ModCompat {
    public static final APContent INSTANCE = new APContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "architects_palette");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "architects_palette");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("architects_palette:twisted", "twisted",
                    () -> BlockSetType.OAK, APBlocks.TWISTED_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    private static final List<ExtraDoor> EXTRA_DOORS = buildExtraDoors();

    private static List<ExtraDoor> buildExtraDoors() {
        List<ExtraDoor> doors = new ArrayList<>();

        doors.add(ExtraDoor.of("entwine_bars_sliding_door",
                () -> BlockSetType.IRON,
                () -> BlockBehaviour.Properties.ofFullCopy(ENTWINE.get()).noOcclusion(),
                SlidingDoorBlock::new,
                ExtraDoor.barsAnchor("architects_palette", "entwine_bars_sliding_door"),
                DoorTag.SLIDING, DoorTag.NEEDS_STONE_TOOL));
        doors.add(ExtraDoor.of("sunmetal_bars_sliding_door",
                () -> BlockSetType.IRON,
                () -> BlockBehaviour.Properties.ofFullCopy(SUNMETAL.get()).noOcclusion(),
                SlidingDoorBlock::new,
                ExtraDoor.barsAnchor("architects_palette", "sunmetal_bars_sliding_door"),
                DoorTag.SLIDING, DoorTag.NEEDS_STONE_TOOL));

        return List.copyOf(doors);
    }

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
        CompatRegistrar.registerExtraDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, EXTRA_DOORS);
    }

    private APContent() {}

    @Override public Mods mod() { return Mods.ARCHITECTS_PALETTE; }
    @Override public String namespace() { return "architects_palette"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
    @Override public List<ExtraDoor> extraDoors()    { return EXTRA_DOORS; }
    @Override public String recipeFolder(WoodFamily family) { return "crafting/"; }
}