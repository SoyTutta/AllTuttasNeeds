package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.blocks.PetDoorBlock;
import com.alltuttasneeds.blocks.TransitDoorBlock;
import com.sammy.malum.registry.common.block.MalumBlocks;
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

public class MalumContent {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "malum");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "malum");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static class SafeAccess {
        // Obtenemos las puertas base de Malum
        static final Supplier<? extends Block> RUNEWOOD_DOOR = MalumBlocks.RUNEWOOD_DOOR;
        static final Supplier<? extends Block> BOLTED_RUNEWOOD_DOOR = MalumBlocks.BOLTED_RUNEWOOD_DOOR;
        static final Supplier<? extends Block> RUNEWOOD_BOARD_DOOR = MalumBlocks.RUNEWOOD_BOARDS_DOOR;
        static final Supplier<? extends Block> BOLTED_RUNEWOOD_BOARD_DOOR = MalumBlocks.BOLTED_RUNEWOOD_BOARDS_DOOR;
        static final Supplier<? extends Block> SOULWOOD_DOOR = MalumBlocks.SOULWOOD_DOOR;
        static final Supplier<? extends Block> BOLTED_SOULWOOD_DOOR = MalumBlocks.BOLTED_SOULWOOD_DOOR;
        static final Supplier<? extends Block> SOULWOOD_BOARD_DOOR = MalumBlocks.SOULWOOD_BOARDS_DOOR;
        static final Supplier<? extends Block> BOLTED_SOULWOOD_BOARD_DOOR = MalumBlocks.BOLTED_SOULWOOD_BOARDS_DOOR;

        static final Supplier<BlockSetType> RUNEWOOD_SET = () -> ((DoorBlock) RUNEWOOD_DOOR.get()).type();

        static final Supplier<BlockSetType> SOULWOOD_SET = () -> ((DoorBlock) SOULWOOD_DOOR.get()).type();
    }

    record WoodConfig(Supplier<BlockSetType> type, String woodName, Supplier<? extends Block> baseDoor, boolean discrete, boolean normal, boolean indiscrete, boolean transit, boolean pet) {}

    private static final List<WoodConfig> CONFIGS = List.of(
            new WoodConfig(SafeAccess.RUNEWOOD_SET, "runewood", SafeAccess.RUNEWOOD_DOOR, true, false, true, true, true),
            new WoodConfig(SafeAccess.RUNEWOOD_SET, "bolted_runewood", SafeAccess.BOLTED_RUNEWOOD_DOOR, true, false, true, true, true),
            new WoodConfig(SafeAccess.RUNEWOOD_SET, "runewood_boards", SafeAccess.RUNEWOOD_BOARD_DOOR, false, true, true, true, true),
            new WoodConfig(SafeAccess.RUNEWOOD_SET, "bolted_runewood_boards", SafeAccess.BOLTED_RUNEWOOD_BOARD_DOOR, true, false, true, true, true),

            new WoodConfig(SafeAccess.SOULWOOD_SET, "soulwood", SafeAccess.SOULWOOD_DOOR, true, false, true, true, true),
            new WoodConfig(SafeAccess.SOULWOOD_SET, "bolted_soulwood", SafeAccess.BOLTED_SOULWOOD_DOOR, true, false, true, true, true),
            new WoodConfig(SafeAccess.SOULWOOD_SET, "soulwood_boards", SafeAccess.SOULWOOD_BOARD_DOOR, false, true, true, true, true),
            new WoodConfig(SafeAccess.SOULWOOD_SET, "bolted_soulwood_boards", SafeAccess.BOLTED_SOULWOOD_BOARD_DOOR, true, false, true, true, true)
    );

    static {
        for (MalumContent.WoodConfig cfg : CONFIGS) {
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