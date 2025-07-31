package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.blocks.PetDoorBlock;
import com.alltuttasneeds.blocks.SecretDoorBlock;
import com.alltuttasneeds.blocks.TransitDoorBlock;
import com.farcr.nomansland.common.registry.blocks.NMLBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class NMLContent {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "nomansland");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "nomansland");

    public static final Map<String, Supplier<? extends Block>> DOORS = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static class SafeAccess {
        static final Supplier<? extends Block> PINE_DOOR   = NMLBlocks.PINE.door();
        static final Supplier<? extends Block> WILLOW_DOOR = NMLBlocks.WILLOW.door();
        static final Supplier<? extends Block> WALNUT_DOOR = NMLBlocks.WALNUT.door();
        static final Supplier<? extends Block> MAPLE_DOOR  = NMLBlocks.MAPLE.door();

        static final Supplier<BlockSetType> PINE_SET   = () -> ((DoorBlock) PINE_DOOR.get()).type();
        static final Supplier<BlockSetType> WILLOW_SET = () -> ((DoorBlock) WILLOW_DOOR.get()).type();
        static final Supplier<BlockSetType> WALNUT_SET = () -> ((DoorBlock) WALNUT_DOOR.get()).type();
        static final Supplier<BlockSetType> MAPLE_SET  = () -> ((DoorBlock) MAPLE_DOOR.get()).type();

        static final Supplier<Block> PINE_BOOKSHELF = NMLBlocks.PINE.bookshelf();
        static final Supplier<Block> WILLOW_BOOKSHELF = NMLBlocks.WILLOW.bookshelf();
        static final Supplier<Block> WALNUT_BOOKSHELF = NMLBlocks.WALNUT.bookshelf();
        static final Supplier<Block> MAPLE_BOOKSHELF = NMLBlocks.MAPLE.bookshelf();

        static final Supplier<Block> SPRUCE_BOOKSHELF = NMLBlocks.SPRUCE_BOOKSHELF;
        static final Supplier<Block> BIRCH_BOOKSHELF = NMLBlocks.BIRCH_BOOKSHELF;
        static final Supplier<Block> JUNGLE_BOOKSHELF = NMLBlocks.JUNGLE_BOOKSHELF;
        static final Supplier<Block> ACACIA_BOOKSHELF = NMLBlocks.ACACIA_BOOKSHELF;
        static final Supplier<Block> DARK_OAK_BOOKSHELF = NMLBlocks.DARK_OAK_BOOKSHELF;
        static final Supplier<Block> MANGROVE_BOOKSHELF = NMLBlocks.MANGROVE_BOOKSHELF;
        static final Supplier<Block> CHERRY_BOOKSHELF = NMLBlocks.CHERRY_BOOKSHELF;
        static final Supplier<Block> BAMBOO_BOOKSHELF = NMLBlocks.BAMBOO_BOOKSHELF;
        static final Supplier<Block> CRIMSON_BOOKSHELF = NMLBlocks.CRIMSON_BOOKSHELF;
        static final Supplier<Block> WARPED_BOOKSHELF = NMLBlocks.WARPED_BOOKSHELF;
    }

    private record WoodConfig(Supplier<BlockSetType> type, String woodName, Supplier<? extends Block> baseDoor, boolean discrete, boolean normal, boolean indiscrete, boolean transit, boolean pet) {}
    private record SecretDoorConfig(String woodName, Supplier<Block> bookshelf, Supplier<BlockSetType> setType) {}

    private static List<WoodConfig> WOOD_CONFIGS;
    private static List<SecretDoorConfig> SECRET_DOOR_CONFIGS;

    static {
        WOOD_CONFIGS = List.of(
                new WoodConfig( SafeAccess.PINE_SET,   "pine",   SafeAccess.PINE_DOOR,  true,  false, true, true, true),
                new WoodConfig( SafeAccess.WILLOW_SET, "willow", SafeAccess.WILLOW_DOOR,true,  false, true, true, true),
                new WoodConfig( SafeAccess.WALNUT_SET, "walnut", SafeAccess.WALNUT_DOOR,true,  false, true, true, true),
                new WoodConfig( SafeAccess.MAPLE_SET,  "maple",  SafeAccess.MAPLE_DOOR, false, true,  true, true, true)
        );

        SECRET_DOOR_CONFIGS = List.of(
                new SecretDoorConfig("pine",   SafeAccess.PINE_BOOKSHELF,   SafeAccess.PINE_SET),
                new SecretDoorConfig("willow", SafeAccess.WILLOW_BOOKSHELF, SafeAccess.WILLOW_SET),
                new SecretDoorConfig("walnut", SafeAccess.WALNUT_BOOKSHELF, SafeAccess.WALNUT_SET),
                new SecretDoorConfig("maple",  SafeAccess.MAPLE_BOOKSHELF,  SafeAccess.MAPLE_SET),
                new SecretDoorConfig("spruce",   SafeAccess.SPRUCE_BOOKSHELF,   () -> BlockSetType.SPRUCE),
                new SecretDoorConfig("birch",    SafeAccess.BIRCH_BOOKSHELF,    () -> BlockSetType.BIRCH),
                new SecretDoorConfig("jungle",   SafeAccess.JUNGLE_BOOKSHELF,   () -> BlockSetType.JUNGLE),
                new SecretDoorConfig("acacia",   SafeAccess.ACACIA_BOOKSHELF,   () -> BlockSetType.ACACIA),
                new SecretDoorConfig("dark_oak", SafeAccess.DARK_OAK_BOOKSHELF, () -> BlockSetType.DARK_OAK),
                new SecretDoorConfig("mangrove", SafeAccess.MANGROVE_BOOKSHELF, () -> BlockSetType.MANGROVE),
                new SecretDoorConfig("cherry",   SafeAccess.CHERRY_BOOKSHELF,   () -> BlockSetType.CHERRY),
                new SecretDoorConfig("bamboo",   SafeAccess.BAMBOO_BOOKSHELF,   () -> BlockSetType.BAMBOO),
                new SecretDoorConfig("crimson",  SafeAccess.CRIMSON_BOOKSHELF,  () -> BlockSetType.CRIMSON),
                new SecretDoorConfig("warped",   SafeAccess.WARPED_BOOKSHELF,   () -> BlockSetType.WARPED)
        );

        for (NMLContent.WoodConfig cfg : WOOD_CONFIGS) {
            if (cfg.discrete())   register(cfg.woodName(), "discrete_door",   cfg.type(), cfg.baseDoor(), DoorBlock::new);
            if (cfg.normal())     register(cfg.woodName(), "normal_door",     cfg.type(), cfg.baseDoor(), DoorBlock::new);
            if (cfg.indiscrete()) register(cfg.woodName(), "indiscrete_door", cfg.type(), cfg.baseDoor(), DoorBlock::new);
            if (cfg.transit())    register(cfg.woodName(), "transit_door",    cfg.type(), cfg.baseDoor(), TransitDoorBlock::new);
            if (cfg.pet())        register(cfg.woodName(), "pet_door",        cfg.type(), cfg.baseDoor(), PetDoorBlock::new);
        }
        registerSecretDoors();
    }

    private static void register(
            String woodName, String variant, Supplier<BlockSetType> setTypeSupplier,
            Supplier<? extends Block> baseDoor, BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory
    ) {
        String name = woodName + "_" + variant;
        Supplier<Block> block = BLOCKS.register(name, () -> {
            BlockSetType resolvedSetType = setTypeSupplier.get();
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(baseDoor.get());
            return factory.apply(resolvedSetType, properties);
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