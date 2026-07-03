package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.blocks.SlidingDoorBlock;
import com.alltuttasneeds.registry.compat.framework.CompatRegistrar;
import com.alltuttasneeds.registry.compat.framework.DoorTag;
import com.alltuttasneeds.registry.compat.framework.ExtraDoor;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/** Create door compatibility */
public final class CreateContent implements ModCompat {
    public static final CreateContent INSTANCE = new CreateContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "create");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "create");

    public static final Map<String, Supplier<? extends Block>> DOORS = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<ExtraDoor> EXTRA_DOORS = List.of(
            ExtraDoor.of("andesite_bars_sliding_door",
                    () -> BlockSetType.COPPER,
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_DOOR).sound(SoundType.COPPER),
                    SlidingDoorBlock::new,
                    ExtraDoor.barsAnchor("create", "andesite_bars_sliding_door"),
                    DoorTag.SLIDING, DoorTag.NEEDS_STONE_TOOL),
            ExtraDoor.of("brass_bars_sliding_door",
                    () -> BlockSetType.COPPER,
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_DOOR).mapColor(MapColor.TERRACOTTA_YELLOW).sound(SoundType.COPPER),
                    SlidingDoorBlock::new,
                    ExtraDoor.barsAnchor("create", "brass_bars_sliding_door"),
                    DoorTag.SLIDING, DoorTag.NEEDS_STONE_TOOL),
            ExtraDoor.of("copper_bars_sliding_door",
                    () -> BlockSetType.COPPER,
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_DOOR).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER),
                    SlidingDoorBlock::new,
                    ExtraDoor.barsAnchor("create", "copper_bars_sliding_door"),
                    DoorTag.SLIDING, DoorTag.NEEDS_STONE_TOOL)
    );

    static {
        CompatRegistrar.registerExtraDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, EXTRA_DOORS);
    }

    private CreateContent() {}

    @Override public Mods mod() { return Mods.CREATE; }
    @Override public String namespace() { return "create"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<ExtraDoor> extraDoors() { return EXTRA_DOORS; }
}
