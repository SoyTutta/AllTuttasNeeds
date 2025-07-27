package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.blocks.PetDoorBlock;
import com.alltuttasneeds.blocks.TransitDoorBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.hibiscus.naturespirit.registration.NSBlocks;
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

public class NSContent {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "natures_spirit");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "natures_spirit");

    public static final Map<String, Supplier<? extends Block>> DOORS = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static class SafeAccess {
        // Wood Doors
        static final Supplier<? extends Block> REDWOOD_DOOR =       NSBlocks.REDWOOD.getDoor();
        static final Supplier<? extends Block> SUGI_DOOR =             NSBlocks.SUGI.getDoor();
        static final Supplier<? extends Block> WISTERIA_DOOR =     NSBlocks.WISTERIA.getDoor();
        static final Supplier<? extends Block> FIR_DOOR =               NSBlocks.FIR.getDoor();
        static final Supplier<? extends Block> WILLOW_DOOR =         NSBlocks.WILLOW.getDoor();
        static final Supplier<? extends Block> ASPEN_DOOR =           NSBlocks.ASPEN.getDoor();
        static final Supplier<? extends Block> MAPLE_DOOR =           NSBlocks.MAPLE.getDoor();
        static final Supplier<? extends Block> CYPRESS_DOOR =       NSBlocks.CYPRESS.getDoor();
        static final Supplier<? extends Block> OLIVE_DOOR =           NSBlocks.OLIVE.getDoor();
        static final Supplier<? extends Block> JOSHUA_DOOR =         NSBlocks.JOSHUA.getDoor();
        static final Supplier<? extends Block> GHAF_DOOR =             NSBlocks.GHAF.getDoor();
        static final Supplier<? extends Block> PALO_VERDE_DOOR = NSBlocks.PALO_VERDE.getDoor();
        static final Supplier<? extends Block> COCONUT_DOOR =       NSBlocks.COCONUT.getDoor();
        static final Supplier<? extends Block> CEDAR_DOOR =           NSBlocks.CEDAR.getDoor();
        static final Supplier<? extends Block> LARCH_DOOR =           NSBlocks.LARCH.getDoor();
        static final Supplier<? extends Block> MAHOGANY_DOOR =     NSBlocks.MAHOGANY.getDoor();
        static final Supplier<? extends Block> SAXAUL_DOOR =         NSBlocks.SAXAUL.getDoor();

        // BlockSetTypes
        static final Supplier<BlockSetType> REDWOOD_SET = () -> ((DoorBlock) REDWOOD_DOOR.get()).type();
        static final Supplier<BlockSetType> SUGI_SET = () -> ((DoorBlock) SUGI_DOOR.get()).type();
        static final Supplier<BlockSetType> WISTERIA_SET = () -> ((DoorBlock) WISTERIA_DOOR.get()).type();
        static final Supplier<BlockSetType> FIR_SET = () -> ((DoorBlock) FIR_DOOR.get()).type();
        static final Supplier<BlockSetType> WILLOW_SET = () -> ((DoorBlock) WILLOW_DOOR.get()).type();
        static final Supplier<BlockSetType> ASPEN_SET = () -> ((DoorBlock) ASPEN_DOOR.get()).type();
        static final Supplier<BlockSetType> MAPLE_SET = () -> ((DoorBlock) MAPLE_DOOR.get()).type();
        static final Supplier<BlockSetType> CYPRESS_SET = () -> ((DoorBlock) CYPRESS_DOOR.get()).type();
        static final Supplier<BlockSetType> OLIVE_SET = () -> ((DoorBlock) OLIVE_DOOR.get()).type();
        static final Supplier<BlockSetType> JOSHUA_SET = () -> ((DoorBlock) JOSHUA_DOOR.get()).type();
        static final Supplier<BlockSetType> GHAF_SET = () -> ((DoorBlock) GHAF_DOOR.get()).type();
        static final Supplier<BlockSetType> PALO_VERDE_SET = () -> ((DoorBlock) PALO_VERDE_DOOR.get()).type();
        static final Supplier<BlockSetType> COCONUT_SET = () -> ((DoorBlock) COCONUT_DOOR.get()).type();
        static final Supplier<BlockSetType> CEDAR_SET = () -> ((DoorBlock) CEDAR_DOOR.get()).type();
        static final Supplier<BlockSetType> LARCH_SET = () -> ((DoorBlock) LARCH_DOOR.get()).type();
        static final Supplier<BlockSetType> MAHOGANY_SET = () -> ((DoorBlock) MAHOGANY_DOOR.get()).type();
        static final Supplier<BlockSetType> SAXAUL_SET = () -> ((DoorBlock) SAXAUL_DOOR.get()).type();
    }

    record WoodConfig(Supplier<BlockSetType> type, String woodName, Supplier<? extends Block> baseDoor, boolean discrete, boolean normal, boolean indiscrete, boolean transit, boolean pet) {}

    private static final List<WoodConfig> CONFIGS = List.of(
            new WoodConfig(SafeAccess.ASPEN_SET,      "aspen",      SafeAccess.ASPEN_DOOR,      true,  false, true,  true, true),
            new WoodConfig(SafeAccess.CEDAR_SET,      "cedar",      SafeAccess.CEDAR_DOOR,      false, true,  true,  true, true),
            new WoodConfig(SafeAccess.COCONUT_SET,    "coconut",    SafeAccess.COCONUT_DOOR,    true,  true,  false, true, true),
            new WoodConfig(SafeAccess.CYPRESS_SET,    "cypress",    SafeAccess.CYPRESS_DOOR,    true,  false, true,  true, true),
            new WoodConfig(SafeAccess.FIR_SET,        "fir",        SafeAccess.FIR_DOOR,        true,  false, true,  true, true),
            new WoodConfig(SafeAccess.GHAF_SET,       "ghaf",       SafeAccess.GHAF_DOOR,       true,  true,  false, true, true),
            new WoodConfig(SafeAccess.JOSHUA_SET,     "joshua",     SafeAccess.JOSHUA_DOOR,     true,  false, true,  true, true),
            new WoodConfig(SafeAccess.LARCH_SET,      "larch",      SafeAccess.LARCH_DOOR,      false, true,  true,  true, true),
            new WoodConfig(SafeAccess.MAHOGANY_SET,   "mahogany",   SafeAccess.MAHOGANY_DOOR,   true,  true,  false, true, true),
            new WoodConfig(SafeAccess.MAPLE_SET,      "maple",      SafeAccess.MAPLE_DOOR,      true,  false, true,  true, true),
            new WoodConfig(SafeAccess.OLIVE_SET,      "olive",      SafeAccess.OLIVE_DOOR,      true,  false, true,  true, true),
            new WoodConfig(SafeAccess.PALO_VERDE_SET, "palo_verde", SafeAccess.PALO_VERDE_DOOR, false, true,  true,  true, true),
            new WoodConfig(SafeAccess.REDWOOD_SET,    "redwood",    SafeAccess.REDWOOD_DOOR,    true,  false, true,  true, true),
            new WoodConfig(SafeAccess.SAXAUL_SET,     "saxaul",     SafeAccess.SAXAUL_DOOR,     false, true,  true,  true, true),
            new WoodConfig(SafeAccess.SUGI_SET,       "sugi",       SafeAccess.SUGI_DOOR,       false, true,  true,  true, true),
            new WoodConfig(SafeAccess.WILLOW_SET,     "willow",     SafeAccess.WILLOW_DOOR,     true,  false, true,  true, true),
            new WoodConfig(SafeAccess.WISTERIA_SET,   "wisteria",   SafeAccess.WISTERIA_DOOR,   true,  false, true,  true, true)
    );

    static {
        for (NSContent.WoodConfig cfg : CONFIGS) {
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