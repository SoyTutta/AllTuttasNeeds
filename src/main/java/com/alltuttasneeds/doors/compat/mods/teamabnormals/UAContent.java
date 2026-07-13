package com.alltuttasneeds.doors.compat.mods.teamabnormals;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.doors.compat.CompatRegistrar;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.SecretDoorFamily;
import com.alltuttasneeds.doors.compat.WoodFamily;
import com.teamabnormals.upgrade_aquatic.core.registry.UABlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.doors.compat.DoorVariant.*;

public final class UAContent implements ModCompat {
    public static final UAContent INSTANCE = new UAContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "upgrade_aquatic");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "upgrade_aquatic");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("upgrade_aquatic:driftwood", "driftwood",
                    () -> UABlocks.UAProperties.DRIFTWOOD_BLOCK_SET, UABlocks.DRIFTWOOD_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("upgrade_aquatic:river", "river",
                    () -> UABlocks.UAProperties.RIVER_BLOCK_SET, UABlocks.RIVER_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    private static final List<SecretDoorFamily> SECRET_DOOR_FAMILIES = Mods.WOODWORKS.isLoaded()
            ? List.of(
            new SecretDoorFamily("driftwood", UABlocks.DRIFTWOOD_BOOKSHELF,
                    () -> UABlocks.UAProperties.DRIFTWOOD_BLOCK_SET, "upgrade_aquatic:driftwood"),
            new SecretDoorFamily("river", UABlocks.RIVER_BOOKSHELF,
                    () -> UABlocks.UAProperties.RIVER_BLOCK_SET, "upgrade_aquatic:river")
    )
            : List.of();

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
        CompatRegistrar.registerSecretDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, SECRET_DOOR_FAMILIES);
    }

    private UAContent() {}

    @Override public Mods mod() { return Mods.UPGRADE_AQUATIC; }
    @Override public String namespace() { return "upgrade_aquatic"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
    @Override public List<SecretDoorFamily> secretDoorFamilies() { return SECRET_DOOR_FAMILIES; }
}