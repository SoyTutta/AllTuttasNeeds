package com.alltuttasneeds.registry;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;

import com.alltuttasneeds.blocks.PetDoorBlock;
import com.alltuttasneeds.blocks.SecretDoorBlock;
import com.alltuttasneeds.blocks.SlidingDoorBlock;
import com.alltuttasneeds.blocks.TransitDoorBlock;
import com.alltuttasneeds.registry.compat.NMLContent;
import com.farcr.nomansland.common.registry.blocks.NMLBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.world.level.block.DoorBlock;

public class TDContent {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "tuttasdoors");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "tuttasdoors");

    public static final DeferredHolder<Item, Item> ICON_ITEM;

    public static final Map<String, Supplier<? extends Block>> DOORS = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    interface DoorFactory {
        Block create(BlockSetType type, BlockBehaviour.Properties props);
    }

    record WoodConfig(String name, Supplier<Block> baseDoor, Supplier<BlockSetType> setType,
                      boolean discrete, boolean normal, boolean indiscrete, boolean transit, boolean pet, boolean sliding) {}

    private record SecretDoorConfig(String woodName, Supplier<Block> bookshelf, Supplier<BlockSetType> setType) {}

    private static final List<WoodConfig> CONFIGS = List.of(
            new WoodConfig("oak",      () -> Blocks.OAK_DOOR,      () -> BlockSetType.OAK,      true, false, true,  true,  true, false),
            new WoodConfig("spruce",   () -> Blocks.SPRUCE_DOOR,   () -> BlockSetType.SPRUCE,   false, true,  true,  true,  true, false),
            new WoodConfig("birch",    () -> Blocks.BIRCH_DOOR,    () -> BlockSetType.BIRCH,    false, true,  true,  true,  true, false),
            new WoodConfig("jungle",   () -> Blocks.JUNGLE_DOOR,   () -> BlockSetType.JUNGLE,   true, false, true,  true,  true, false),
            new WoodConfig("acacia",   () -> Blocks.ACACIA_DOOR,   () -> BlockSetType.ACACIA,   true, true,  false, true,  true, false),
            new WoodConfig("dark_oak", () -> Blocks.DARK_OAK_DOOR, () -> BlockSetType.DARK_OAK, false, true,  true,  true,  true, false),
            new WoodConfig("mangrove", () -> Blocks.MANGROVE_DOOR, () -> BlockSetType.MANGROVE, false, true,  true,  true,  true, false),
            new WoodConfig("cherry",   () -> Blocks.CHERRY_DOOR,   () -> BlockSetType.CHERRY,   true, false, true,  true,  true, false),
            new WoodConfig("bamboo",   () -> Blocks.BAMBOO_DOOR,   () -> BlockSetType.BAMBOO,   true, false, true,  true,  true, false),
            new WoodConfig("crimson",  () -> Blocks.CRIMSON_DOOR,  () -> BlockSetType.CRIMSON,  false, true,  true,  true,  true, false),
            new WoodConfig("warped",   () -> Blocks.WARPED_DOOR,   () -> BlockSetType.WARPED,   false, true,  true,  true,  true, false),
            new WoodConfig("iron",     () -> Blocks.IRON_DOOR,     () -> BlockSetType.IRON,     false, false, false, false, false, true)
    );

    private static final List<SecretDoorConfig> SECRET_DOOR_CONFIGS = List.of(
            new SecretDoorConfig("oak", () -> Blocks.BOOKSHELF, () -> BlockSetType.OAK)
    );

    private static final Map<String, DoorFactory> FACTORIES = Map.of(
            "discrete",   DoorBlock::new,
            "normal",     DoorBlock::new,
            "indiscrete", DoorBlock::new,
            "transit",    TransitDoorBlock::new,
            "pet",        PetDoorBlock::new,
            "sliding",    SlidingDoorBlock::new
    );

    static {
        String iconName = "oak_discrete_door";
        Supplier<Block> iconBlock = BLOCKS.register(iconName, () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
        DOORS.put(iconName, iconBlock);
        ICON_ITEM = ITEMS.register(iconName, () -> new BlockItem(iconBlock.get(), new Item.Properties()));
        DOOR_ITEMS.put(iconName, ICON_ITEM);

        for (WoodConfig cfg : CONFIGS) {
            if (cfg.discrete()) register(cfg, "discrete",   FACTORIES.get("discrete"));
            if (cfg.normal())   register(cfg, "normal",     FACTORIES.get("normal"));
            if (cfg.indiscrete()) register(cfg, "indiscrete", FACTORIES.get("indiscrete"));
            if (cfg.transit())  register(cfg, "transit",    FACTORIES.get("transit"));
            if (cfg.pet())      register(cfg, "pet",        FACTORIES.get("pet"));
            if (cfg.sliding())  register(cfg, "sliding",    FACTORIES.get("sliding"));
        }
        registerSecretDoors();
    }

    private static void register(WoodConfig config, String variant, DoorFactory factory) {
        String name = (config.setType().get() == BlockSetType.IRON && variant.equals("sliding"))
                ? "iron_bars_sliding_door"
                : config.name() + "_" + variant + "_door";

        if (DOORS.containsKey(name)) return;

        Supplier<Block> block = BLOCKS.register(name, () -> {
            Block baseDoor = config.baseDoor().get();
            BlockSetType setType = config.setType().get();
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(baseDoor).noOcclusion();
            return factory.create(setType, properties);
        });

        DOORS.put(name, block);
        Supplier<Item> item = ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        DOOR_ITEMS.put(name, item);
    }

    private static void registerSecretDoors() {
        for (SecretDoorConfig config : SECRET_DOOR_CONFIGS) {
            String name = config.woodName() + "_bookshelf_door";

            Supplier<Block> block = BLOCKS.register(name, () -> {
                Block baseBookshelf = config.bookshelf().get();
                BlockSetType setType = config.setType().get();
                BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(baseBookshelf).noOcclusion();
                return new SecretDoorBlock(setType, properties);
            });

            DOORS.put(name, block);
            Supplier<Item> item = ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
            DOOR_ITEMS.put(name, item);
        }
    }
}