package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.blocks.SlidingDoorBlock;
import com.alltuttasneeds.blocks.WeatheringCopperSlidingDoorBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.blockbox.common.registry.ModBlockSets;
import vectorwing.blockbox.common.registry.ModBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BBContent {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "blockbox");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "blockbox");

    public static final Map<String, Supplier<? extends Block>> DOORS = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    @FunctionalInterface
    interface DoorFactory {
        Block create(BlockSetType type, BlockBehaviour.Properties props);
    }

    private static class SafeAccess {
        static final Supplier<? extends Block> GOLD_DOOR_BASE = ModBlocks.GOLDEN_DOOR;
        static final Supplier<BlockSetType> GOLD_SET = ModBlockSets.GOLD;
        static final Supplier<BlockBehaviour.Properties> COPPER_DOOR_PROPS = () ->
                BlockBehaviour.Properties.of()
                        .strength(3.0F, 6.0F)
                        .noOcclusion()
                        .requiresCorrectToolForDrops()
                        .pushReaction(PushReaction.DESTROY);
        static final Supplier<BlockSetType> COPPER_SET = () -> BlockSetType.COPPER;
    }

    record DoorConfig(String name, Supplier<BlockSetType> setType, Supplier<BlockBehaviour.Properties> props, DoorFactory factory) {}

    private static final List<DoorConfig> CONFIGS;

    static {
        CONFIGS = new ArrayList<>();

        CONFIGS.add(new DoorConfig(
                "golden_bars_sliding_door",
                SafeAccess.GOLD_SET,
                () -> BlockBehaviour.Properties.ofFullCopy(SafeAccess.GOLD_DOOR_BASE.get()).mapColor(MapColor.GOLD),
                (type, props) -> new SlidingDoorBlock(type, props)
        ));

        String baseCopperName = "copper_bars_sliding_door";
        for (WeatheringCopper.WeatherState weatherState : WeatheringCopper.WeatherState.values()) {
            for (boolean isWaxed : new boolean[]{false, true}) {
                String variantName = getWeatheredName(weatherState, isWaxed);
                String fullName = variantName.equals("copper") ? baseCopperName : variantName + "_" + baseCopperName;

                Supplier<BlockBehaviour.Properties> copperProps = () ->
                        SafeAccess.COPPER_DOOR_PROPS.get()
                                .mapColor(getWeatheredMapColor(weatherState));

                DoorFactory factory;
                if (isWaxed) {
                    factory = (type, props) -> new SlidingDoorBlock(type, props);
                } else {
                    factory = (type, props) -> new WeatheringCopperSlidingDoorBlock(type, weatherState, props);
                }

                CONFIGS.add(new DoorConfig(fullName, SafeAccess.COPPER_SET, copperProps, factory));
            }
        }

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

    private static String getWeatheredName(WeatheringCopper.WeatherState state, boolean waxed) {
        String prefix = waxed ? "waxed_" : "";
        return switch (state) {
            case UNAFFECTED -> waxed ? "waxed" : "copper";
            case EXPOSED -> prefix + "exposed";
            case WEATHERED -> prefix + "weathered";
            case OXIDIZED -> prefix + "oxidized";
        };
    }

    private static MapColor getWeatheredMapColor(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case EXPOSED -> Blocks.EXPOSED_COPPER.defaultMapColor();
            case WEATHERED -> Blocks.WEATHERED_COPPER.defaultMapColor();
            case OXIDIZED -> Blocks.OXIDIZED_COPPER.defaultMapColor();
            default -> Blocks.COPPER_BLOCK.defaultMapColor();
        };
    }
}