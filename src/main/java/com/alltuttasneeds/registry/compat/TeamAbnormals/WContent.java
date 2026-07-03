package com.alltuttasneeds.registry.compat.TeamAbnormals;

import com.alltuttasneeds.registry.compat.Mods;
import com.alltuttasneeds.registry.compat.framework.CompatRegistrar;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.SecretDoorFamily;
import com.teamabnormals.woodworks.core.registry.WoodworksBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/** Woodworks door compatibility. */
public final class WContent implements ModCompat {
    public static final WContent INSTANCE = new WContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "woodworks");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "woodworks");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<SecretDoorFamily> SECRET_DOOR_FAMILIES = List.of(
            new SecretDoorFamily("spruce",   WoodworksBlocks.SPRUCE_BOOKSHELF,   () -> BlockSetType.SPRUCE,   "minecraft:spruce"),
            new SecretDoorFamily("birch",    WoodworksBlocks.BIRCH_BOOKSHELF,    () -> BlockSetType.BIRCH,    "minecraft:birch"),
            new SecretDoorFamily("jungle",   WoodworksBlocks.JUNGLE_BOOKSHELF,   () -> BlockSetType.JUNGLE,   "minecraft:jungle"),
            new SecretDoorFamily("acacia",   WoodworksBlocks.ACACIA_BOOKSHELF,   () -> BlockSetType.ACACIA,   "minecraft:acacia"),
            new SecretDoorFamily("dark_oak", WoodworksBlocks.DARK_OAK_BOOKSHELF, () -> BlockSetType.DARK_OAK, "minecraft:dark_oak"),
            new SecretDoorFamily("mangrove", WoodworksBlocks.MANGROVE_BOOKSHELF, () -> BlockSetType.MANGROVE, "minecraft:mangrove"),
            new SecretDoorFamily("cherry",   WoodworksBlocks.CHERRY_BOOKSHELF,   () -> BlockSetType.CHERRY,   "minecraft:cherry"),
            new SecretDoorFamily("bamboo",   WoodworksBlocks.BAMBOO_BOOKSHELF,   () -> BlockSetType.BAMBOO,   "minecraft:bamboo"),
            new SecretDoorFamily("crimson",  WoodworksBlocks.CRIMSON_BOOKSHELF,  () -> BlockSetType.CRIMSON,  "minecraft:crimson"),
            new SecretDoorFamily("warped",   WoodworksBlocks.WARPED_BOOKSHELF,   () -> BlockSetType.WARPED,   "minecraft:warped")
    );

    static {
        CompatRegistrar.registerSecretDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, SECRET_DOOR_FAMILIES);
    }

    private WContent() {}

    @Override public Mods mod() { return Mods.WOODWORKS; }
    @Override public String namespace() { return "woodworks"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<SecretDoorFamily> secretDoorFamilies() { return SECRET_DOOR_FAMILIES; }
}