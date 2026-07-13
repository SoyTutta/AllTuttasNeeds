package com.alltuttasneeds.doors.compat.mods.teamabnormals;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.doors.compat.CompatRegistrar;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.SecretDoorFamily;
import com.alltuttasneeds.doors.compat.WoodFamily;
import com.teamabnormals.atmospheric.core.other.AtmosphericProperties;
import com.teamabnormals.atmospheric.core.registry.AtmosphericBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.doors.compat.DoorVariant.*;

public final class ATMContent implements ModCompat {
    public static final ATMContent INSTANCE = new ATMContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "atmospheric");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "atmospheric");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("atmospheric:rosewood", "rosewood",
                    () -> AtmosphericProperties.ROSEWOOD_BLOCK_SET, AtmosphericBlocks.ROSEWOOD_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("atmospheric:morado", "morado",
                    () -> AtmosphericProperties.MORADO_BLOCK_SET, AtmosphericBlocks.MORADO_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("atmospheric:yucca", "yucca",
                    () -> AtmosphericProperties.YUCCA_BLOCK_SET, AtmosphericBlocks.YUCCA_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("atmospheric:aspen", "aspen",
                    () -> AtmosphericProperties.ASPEN_BLOCK_SET, AtmosphericBlocks.ASPEN_DOOR,
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("atmospheric:laurel", "laurel",
                    () -> AtmosphericProperties.LAUREL_BLOCK_SET, AtmosphericBlocks.LAUREL_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("atmospheric:kousa", "kousa",
                    () -> AtmosphericProperties.KOUSA_BLOCK_SET, AtmosphericBlocks.KOUSA_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("atmospheric:grimwood", "grimwood",
                    () -> AtmosphericProperties.GRIMWOOD_BLOCK_SET, AtmosphericBlocks.GRIMWOOD_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    private static final List<SecretDoorFamily> SECRET_DOOR_FAMILIES = Mods.WOODWORKS.isLoaded()
            ? List.of(
            new SecretDoorFamily("rosewood", AtmosphericBlocks.ROSEWOOD_BOOKSHELF,
                    () -> AtmosphericProperties.ROSEWOOD_BLOCK_SET, "atmospheric:rosewood"),
            new SecretDoorFamily("morado", AtmosphericBlocks.MORADO_BOOKSHELF,
                    () -> AtmosphericProperties.MORADO_BLOCK_SET, "atmospheric:morado"),
            new SecretDoorFamily("yucca", AtmosphericBlocks.YUCCA_BOOKSHELF,
                    () -> AtmosphericProperties.YUCCA_BLOCK_SET, "atmospheric:yucca"),
            new SecretDoorFamily("aspen", AtmosphericBlocks.ASPEN_BOOKSHELF,
                    () -> AtmosphericProperties.ASPEN_BLOCK_SET, "atmospheric:aspen"),
            new SecretDoorFamily("laurel", AtmosphericBlocks.LAUREL_BOOKSHELF,
                    () -> AtmosphericProperties.LAUREL_BLOCK_SET, "atmospheric:laurel"),
            new SecretDoorFamily("kousa", AtmosphericBlocks.KOUSA_BOOKSHELF,
                    () -> AtmosphericProperties.KOUSA_BLOCK_SET, "atmospheric:kousa"),
            new SecretDoorFamily("grimwood", AtmosphericBlocks.GRIMWOOD_BOOKSHELF,
                    () -> AtmosphericProperties.GRIMWOOD_BLOCK_SET, "atmospheric:grimwood")
    )
            : List.of();

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
        CompatRegistrar.registerSecretDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, SECRET_DOOR_FAMILIES);
    }

    private ATMContent() {}

    @Override public Mods mod() { return Mods.ATMOSPHERIC; }
    @Override public String namespace() { return "atmospheric"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
    @Override public List<SecretDoorFamily> secretDoorFamilies() { return SECRET_DOOR_FAMILIES; }
}