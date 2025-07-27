package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.blocks.SlidingDoorBlock; // Revisa que la importación sea correcta
import com.simibubi.create.AllBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CreateContent {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "create");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "create");

    public static final Map<String, Supplier<? extends Block>> DOORS = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    @FunctionalInterface
    interface DoorFactory {
        Block create(BlockSetType type, BlockBehaviour.Properties props);
    }

    record DoorConfig(String name, Supplier<BlockSetType> setType, Supplier<BlockBehaviour.Properties> props, DoorFactory factory) {}

    private static final List<DoorConfig> CONFIGS;

    static {
        CONFIGS = new ArrayList<>();
        DoorFactory slidingFactory = (type, props) -> new SlidingDoorBlock(type, props);

        CONFIGS.add(new DoorConfig(
                "andesite_bars_sliding_door",
                () -> BlockSetType.COPPER,
                () -> BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE).sound(SoundType.COPPER),
                slidingFactory
        ));

        CONFIGS.add(new DoorConfig(
                "brass_bars_sliding_door",
                () -> BlockSetType.COPPER,
                () -> BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_DOOR).mapColor(MapColor.TERRACOTTA_YELLOW).sound(SoundType.COPPER),
                slidingFactory
        ));

        CONFIGS.add(new DoorConfig(
                "copper_bars_sliding_door",
                () -> BlockSetType.COPPER,
                () -> BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_DOOR).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER),
                slidingFactory
        ));

        for (DoorConfig cfg : CONFIGS) {
            register(cfg);
        }
    }

    private static void register(DoorConfig cfg) {
        Supplier<Block> block = BLOCKS.register(cfg.name(), () -> {
            BlockSetType setType = cfg.setType().get();
            BlockBehaviour.Properties properties = cfg.props().get();
            return cfg.factory().create(setType, properties);
        });

        DOORS.put(cfg.name(), block);
        Supplier<Item> item = ITEMS.register(cfg.name(), () -> new BlockItem(block.get(), new Item.Properties()));
        DOOR_ITEMS.put(cfg.name(), item);
    }
}