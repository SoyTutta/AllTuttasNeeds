package com.alltuttasneeds.doors.compat.mods.teamabnormals;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.doors.compat.CompatRegistrar;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.SecretDoorFamily;
import com.alltuttasneeds.doors.compat.WoodFamily;
import com.teamabnormals.environmental.core.other.EnvironmentalProperties;
import com.teamabnormals.environmental.core.registry.EnvironmentalBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.doors.compat.DoorVariant.*;

public final class EContent implements ModCompat {
    public static final EContent INSTANCE = new EContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "environmental");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "environmental");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("environmental:willow", "willow",
                    () -> EnvironmentalProperties.WILLOW_BLOCK_SET, EnvironmentalBlocks.WILLOW_DOOR,
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("environmental:pine", "pine",
                    () -> EnvironmentalProperties.PINE_BLOCK_SET, EnvironmentalBlocks.PINE_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("environmental:plum", "plum",
                    () -> EnvironmentalProperties.PLUM_BLOCK_SET, EnvironmentalBlocks.PLUM_DOOR,
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("environmental:wisteria", "wisteria",
                    () -> EnvironmentalProperties.WISTERIA_BLOCK_SET, EnvironmentalBlocks.WISTERIA_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    private static final List<SecretDoorFamily> SECRET_DOOR_FAMILIES = Mods.WOODWORKS.isLoaded()
            ? List.of(
            new SecretDoorFamily("willow", EnvironmentalBlocks.WILLOW_BOOKSHELF,
                    () -> EnvironmentalProperties.WILLOW_BLOCK_SET, "environmental:willow"),
            new SecretDoorFamily("pine", EnvironmentalBlocks.PINE_BOOKSHELF,
                    () -> EnvironmentalProperties.PINE_BLOCK_SET, "environmental:pine"),
            new SecretDoorFamily("plum", EnvironmentalBlocks.PLUM_BOOKSHELF,
                    () -> EnvironmentalProperties.PLUM_BLOCK_SET, "environmental:plum"),
            new SecretDoorFamily("wisteria", EnvironmentalBlocks.WISTERIA_BOOKSHELF,
                    () -> EnvironmentalProperties.WISTERIA_BLOCK_SET, "environmental:wisteria")
    )
            : List.of();

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
        CompatRegistrar.registerSecretDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, SECRET_DOOR_FAMILIES);
    }

    private EContent() {}

    @Override public Mods mod() { return Mods.ENVIRONMENTAL; }
    @Override public String namespace() { return "environmental"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
    @Override public List<SecretDoorFamily> secretDoorFamilies() { return SECRET_DOOR_FAMILIES; }
}