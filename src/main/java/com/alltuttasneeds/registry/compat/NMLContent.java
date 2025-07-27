package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.blocks.PetDoorBlock;
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

    record WoodConfig(Supplier<BlockSetType> type, String woodName, Supplier<? extends Block> baseDoor, boolean discrete, boolean normal, boolean indiscrete, boolean transit, boolean pet) {}

    private static final List<WoodConfig> CONFIGS = List.of(
            new WoodConfig( SafeAccess.PINE_SET,   "pine",   SafeAccess.PINE_DOOR,  true,  false, true, true, true),
            new WoodConfig( SafeAccess.WILLOW_SET, "willow", SafeAccess.WILLOW_DOOR,true,  false, true, true, true),
            new WoodConfig( SafeAccess.WALNUT_SET, "walnut", SafeAccess.WALNUT_DOOR,true,  false, true, true, true),
            new WoodConfig( SafeAccess.MAPLE_SET,  "maple",  SafeAccess.MAPLE_DOOR, false, true,  true, true, true)
    );

    static {
        for (NMLContent.WoodConfig cfg : CONFIGS) {
            if (cfg.discrete())   register(cfg.woodName(), "discrete_door",   cfg.type(), cfg.baseDoor(), DoorBlock::new);
            if (cfg.normal()) register(cfg.woodName(), "normal_door", cfg.type(), cfg.baseDoor(), DoorBlock::new);
            if (cfg.indiscrete()) register(cfg.woodName(), "indiscrete_door", cfg.type(), cfg.baseDoor(), DoorBlock::new);
            if (cfg.transit())    register(cfg.woodName(), "transit_door",    cfg.type(), cfg.baseDoor(), TransitDoorBlock::new);
            if (cfg.pet())        register(cfg.woodName(), "pet_door",        cfg.type(), cfg.baseDoor(), PetDoorBlock::new);
        }
    }

    private static void register(
            String woodName,
            String variant,
            Supplier<BlockSetType> setTypeSupplier,
            Supplier<? extends Block> baseDoor,
            BiFunction<BlockSetType, BlockBehaviour.Properties, Block> factory
    ) {
        String name = woodName + "_" + variant;
        Supplier<Block> block = BLOCKS.register(name,
                () -> {
                    BlockSetType resolvedSetType = setTypeSupplier.get();
                    BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(baseDoor.get());
                    return factory.apply(resolvedSetType, properties);
                }
        );
        DOORS.put(name, block);

        Supplier<Item> item = ITEMS.register(name,
                () -> new BlockItem(block.get(), new Item.Properties())
        );
        DOOR_ITEMS.put(name, item);
    }
}