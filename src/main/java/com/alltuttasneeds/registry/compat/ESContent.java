package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.blocks.PetDoorBlock;
import com.alltuttasneeds.blocks.TransitDoorBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
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

public class ESContent {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "enderscape");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "enderscape");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static class SafeAccess {
        static final Supplier<BlockSetType> VEILED_SET = () -> EnderscapeBlocks.VEILED_BLOCK_SET;
        static final Supplier<? extends Block> VEILED_DOOR = EnderscapeBlocks.VEILED_DOOR;

        static final Supplier<BlockSetType> CELESTIAL_SET = () -> EnderscapeBlocks.CELESTIAL_BLOCK_SET;
        static final Supplier<? extends Block> CELESTIAL_DOOR = EnderscapeBlocks.CELESTIAL_DOOR;

        static final Supplier<BlockSetType> MURUBLIGHT_SET = () -> EnderscapeBlocks.MURUBLIGHT_BLOCK_SET;
        static final Supplier<? extends Block> MURUBLIGHT_DOOR = EnderscapeBlocks.MURUBLIGHT_DOOR;
    }

    record WoodConfig(Supplier<BlockSetType> type, String woodName, Supplier<? extends Block> baseDoor, boolean discrete, boolean normal, boolean indiscrete, boolean transit, boolean pet) {}

    private static final List<WoodConfig> CONFIGS = List.of(
            new WoodConfig(SafeAccess.VEILED_SET,     "veiled",     SafeAccess.VEILED_DOOR,     true,  false, true, true, true),
            new WoodConfig(SafeAccess.CELESTIAL_SET,  "celestial",  SafeAccess.CELESTIAL_DOOR,  false, true,  true, true, true),
            new WoodConfig(SafeAccess.MURUBLIGHT_SET, "murublight", SafeAccess.MURUBLIGHT_DOOR, false, true,  true, true, true)
    );

    static {
        for (ESContent.WoodConfig cfg : CONFIGS) {
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