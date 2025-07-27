package com.alltuttasneeds.registry.compat;

import biomesoplenty.api.block.BOPBlocks;
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

public class BoPContent {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "biomesoplenty");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "biomesoplenty");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static class SafeAccess {
        static final Supplier<? extends Block> FIR_DOOR = () -> BOPBlocks.FIR_DOOR;
        static final Supplier<? extends Block> PINE_DOOR = () -> BOPBlocks.PINE_DOOR;
        static final Supplier<? extends Block> MAPLE_DOOR = () -> BOPBlocks.MAPLE_DOOR;
        static final Supplier<? extends Block> REDWOOD_DOOR = () -> BOPBlocks.REDWOOD_DOOR;
        static final Supplier<? extends Block> MAHOGANY_DOOR = () -> BOPBlocks.MAHOGANY_DOOR;
        static final Supplier<? extends Block> JACARANDA_DOOR = () -> BOPBlocks.JACARANDA_DOOR;
        static final Supplier<? extends Block> PALM_DOOR = () -> BOPBlocks.PALM_DOOR;
        static final Supplier<? extends Block> WILLOW_DOOR = () -> BOPBlocks.WILLOW_DOOR;
        static final Supplier<? extends Block> DEAD_DOOR = () -> BOPBlocks.DEAD_DOOR;
        static final Supplier<? extends Block> MAGIC_DOOR = () -> BOPBlocks.MAGIC_DOOR;
        static final Supplier<? extends Block> UMBRAN_DOOR = () -> BOPBlocks.UMBRAN_DOOR;
        static final Supplier<? extends Block> HELLBARK_DOOR = () -> BOPBlocks.HELLBARK_DOOR;
        static final Supplier<? extends Block> EMPYREAL_DOOR = () -> BOPBlocks.EMPYREAL_DOOR;

        static final Supplier<BlockSetType> FIR_SET = () -> ((DoorBlock) FIR_DOOR.get()).type();
        static final Supplier<BlockSetType> PINE_SET = () -> ((DoorBlock) PINE_DOOR.get()).type();
        static final Supplier<BlockSetType> MAPLE_SET = () -> ((DoorBlock) MAPLE_DOOR.get()).type();
        static final Supplier<BlockSetType> REDWOOD_SET = () -> ((DoorBlock) REDWOOD_DOOR.get()).type();
        static final Supplier<BlockSetType> MAHOGANY_SET = () -> ((DoorBlock) MAHOGANY_DOOR.get()).type();
        static final Supplier<BlockSetType> JACARANDA_SET = () -> ((DoorBlock) JACARANDA_DOOR.get()).type();
        static final Supplier<BlockSetType> PALM_SET = () -> ((DoorBlock) PALM_DOOR.get()).type();
        static final Supplier<BlockSetType> WILLOW_SET = () -> ((DoorBlock) WILLOW_DOOR.get()).type();
        static final Supplier<BlockSetType> DEAD_SET = () -> ((DoorBlock) DEAD_DOOR.get()).type();
        static final Supplier<BlockSetType> MAGIC_SET = () -> ((DoorBlock) MAGIC_DOOR.get()).type();
        static final Supplier<BlockSetType> UMBRAN_SET = () -> ((DoorBlock) UMBRAN_DOOR.get()).type();
        static final Supplier<BlockSetType> HELLBARK_SET = () -> ((DoorBlock) HELLBARK_DOOR.get()).type();
        static final Supplier<BlockSetType> EMPYREAL_SET = () -> ((DoorBlock) EMPYREAL_DOOR.get()).type();
    }

    record WoodConfig(Supplier<BlockSetType> type, String woodName, Supplier<? extends Block> baseDoor, boolean discrete, boolean normal, boolean indiscrete, boolean transit, boolean pet) {}

    private static final List<WoodConfig> CONFIGS = List.of(
            new WoodConfig(SafeAccess.FIR_SET, "fir", SafeAccess.FIR_DOOR, false, true, true, true, true),
            new WoodConfig(SafeAccess.PINE_SET, "pine", SafeAccess.PINE_DOOR, false, true, true, true, true),
            new WoodConfig(SafeAccess.MAPLE_SET, "maple", SafeAccess.MAPLE_DOOR, false, true, true, true, true),
            new WoodConfig(SafeAccess.REDWOOD_SET, "redwood", SafeAccess.REDWOOD_DOOR, false, true, true, true, true),
            new WoodConfig(SafeAccess.MAHOGANY_SET, "mahogany", SafeAccess.MAHOGANY_DOOR, true, true, false, true, true),
            new WoodConfig(SafeAccess.JACARANDA_SET, "jacaranda", SafeAccess.JACARANDA_DOOR, true, true, false, true, true),
            new WoodConfig(SafeAccess.PALM_SET, "palm", SafeAccess.PALM_DOOR, true, true, false, true, true),
            new WoodConfig(SafeAccess.WILLOW_SET, "willow", SafeAccess.WILLOW_DOOR, true, true, false, true, true),
            new WoodConfig(SafeAccess.DEAD_SET, "dead", SafeAccess.DEAD_DOOR, true, true, false, true, true),
            new WoodConfig(SafeAccess.MAGIC_SET, "magic", SafeAccess.MAGIC_DOOR, true, true, false, true, true),
            new WoodConfig(SafeAccess.UMBRAN_SET, "umbran", SafeAccess.UMBRAN_DOOR, true, false, true, true, true),
            new WoodConfig(SafeAccess.HELLBARK_SET, "hellbark", SafeAccess.HELLBARK_DOOR, true, false, true, true, true),
            new WoodConfig(SafeAccess.EMPYREAL_SET, "empyreal", SafeAccess.EMPYREAL_DOOR, false, true, true, true, true)
    );

    static {
        for (BoPContent.WoodConfig cfg : CONFIGS) {
            if (cfg.discrete())   register(cfg.woodName(), "discrete_door",   cfg.type(), cfg.baseDoor(), DoorBlock::new);
            if (cfg.normal())     register(cfg.woodName(), "normal_door",     cfg.type(), cfg.baseDoor(), DoorBlock::new);
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