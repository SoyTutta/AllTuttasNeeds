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

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
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
    }

    private record WoodConfig(Supplier<BlockSetType> type, String woodName, Supplier<? extends Block> baseDoor, boolean discrete, boolean normal, boolean indiscrete, boolean transit, boolean pet) {}

    private static final List<WoodConfig> WOOD_CONFIGS = List.of(
            new WoodConfig( SafeAccess.PINE_SET,   "pine",   SafeAccess.PINE_DOOR,  true,  false, true, true, true),
            new WoodConfig( SafeAccess.WILLOW_SET, "willow", SafeAccess.WILLOW_DOOR,true,  false, true, true, true),
            new WoodConfig( SafeAccess.WALNUT_SET, "walnut", SafeAccess.WALNUT_DOOR,true,  false, true, true, true),
            new WoodConfig( SafeAccess.MAPLE_SET,  "maple",  SafeAccess.MAPLE_DOOR, false, true,  true, true, true)
    );

    private record SecretDoorConfig(String woodName, Supplier<Block> bookshelf, Supplier<BlockSetType> setType) {}

    private static final List<SecretDoorConfig> SECRET_DOOR_CONFIGS = List.of(
            new SecretDoorConfig("pine",   () -> NMLBlocks.PINE.bookshelf().get(),   SafeAccess.PINE_SET),
            new SecretDoorConfig("willow", () -> NMLBlocks.WILLOW.bookshelf().get(), SafeAccess.WILLOW_SET),
            new SecretDoorConfig("walnut", () -> NMLBlocks.WALNUT.bookshelf().get(), SafeAccess.WALNUT_SET),
            new SecretDoorConfig("maple",  () -> NMLBlocks.MAPLE.bookshelf().get(),  SafeAccess.MAPLE_SET),

            new SecretDoorConfig("spruce",   () -> NMLBlocks.SPRUCE_BOOKSHELF.get(),   () -> BlockSetType.SPRUCE),
            new SecretDoorConfig("birch",    () -> NMLBlocks.BIRCH_BOOKSHELF.get(),    () -> BlockSetType.BIRCH),
            new SecretDoorConfig("jungle",   () -> NMLBlocks.JUNGLE_BOOKSHELF.get(),   () -> BlockSetType.JUNGLE),
            new SecretDoorConfig("acacia",   () -> NMLBlocks.ACACIA_BOOKSHELF.get(),   () -> BlockSetType.ACACIA),
            new SecretDoorConfig("dark_oak", () -> NMLBlocks.DARK_OAK_BOOKSHELF.get(), () -> BlockSetType.DARK_OAK),
            new SecretDoorConfig("mangrove", () -> NMLBlocks.MANGROVE_BOOKSHELF.get(), () -> BlockSetType.MANGROVE),
            new SecretDoorConfig("cherry",   () -> NMLBlocks.CHERRY_BOOKSHELF.get(),   () -> BlockSetType.CHERRY),
            new SecretDoorConfig("bamboo",   () -> NMLBlocks.BAMBOO_BOOKSHELF.get(),   () -> BlockSetType.BAMBOO),
            new SecretDoorConfig("crimson",  () -> NMLBlocks.CRIMSON_BOOKSHELF.get(),  () -> BlockSetType.CRIMSON),
            new SecretDoorConfig("warped",   () -> NMLBlocks.WARPED_BOOKSHELF.get(),   () -> BlockSetType.WARPED)
    );

    static {
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