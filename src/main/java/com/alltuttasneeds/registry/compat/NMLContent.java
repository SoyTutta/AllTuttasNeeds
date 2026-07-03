package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.registry.compat.framework.CompatRegistrar;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.SecretDoorFamily;
import com.alltuttasneeds.registry.compat.framework.WoodFamily;
import com.farcr.nomansland.common.registry.blocks.NMLBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.registry.compat.framework.CompatRegistrar.setTypeFromDoor;
import static com.alltuttasneeds.registry.compat.framework.DoorVariant.*;

/** NoMansLand compatibility. */
public final class NMLContent implements ModCompat {
    public static final NMLContent INSTANCE = new NMLContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "nomansland");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "nomansland");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>            DOOR_ITEMS = new HashMap<>();

    private static final Supplier<? extends Block> PINE_DOOR   = NMLBlocks.PINE.door();
    private static final Supplier<? extends Block> WILLOW_DOOR = NMLBlocks.WILLOW.door();
    private static final Supplier<? extends Block> WALNUT_DOOR = NMLBlocks.WALNUT.door();
    private static final Supplier<? extends Block> MAPLE_DOOR  = NMLBlocks.MAPLE.door();

    private static final Supplier<BlockSetType> PINE_SET   = setTypeFromDoor(PINE_DOOR);
    private static final Supplier<BlockSetType> WILLOW_SET = setTypeFromDoor(WILLOW_DOOR);
    private static final Supplier<BlockSetType> WALNUT_SET = setTypeFromDoor(WALNUT_DOOR);
    private static final Supplier<BlockSetType> MAPLE_SET  = setTypeFromDoor(MAPLE_DOOR);

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("nomansland:pine",   "pine",   PINE_SET,   PINE_DOOR,   List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("nomansland:willow", "willow", WILLOW_SET, WILLOW_DOOR, List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("nomansland:walnut", "walnut", WALNUT_SET, WALNUT_DOOR, List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("nomansland:maple",  "maple",  MAPLE_SET,  MAPLE_DOOR,  List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    private static final List<SecretDoorFamily> SECRET_DOOR_FAMILIES = List.of(
            new SecretDoorFamily("pine",     NMLBlocks.PINE.bookshelf(),   PINE_SET,   "nomansland:pine"),
            new SecretDoorFamily("willow",   NMLBlocks.WILLOW.bookshelf(), WILLOW_SET, "nomansland:willow"),
            new SecretDoorFamily("walnut",   NMLBlocks.WALNUT.bookshelf(), WALNUT_SET, "nomansland:walnut"),
            new SecretDoorFamily("maple",    NMLBlocks.MAPLE.bookshelf(),  MAPLE_SET,  "nomansland:maple"),
            new SecretDoorFamily("spruce",   NMLBlocks.SPRUCE_BOOKSHELF,  () -> BlockSetType.SPRUCE,  "minecraft:spruce"),
            new SecretDoorFamily("birch",    NMLBlocks.BIRCH_BOOKSHELF,   () -> BlockSetType.BIRCH,   "minecraft:birch"),
            new SecretDoorFamily("jungle",   NMLBlocks.JUNGLE_BOOKSHELF,  () -> BlockSetType.JUNGLE,  "minecraft:jungle"),
            new SecretDoorFamily("acacia",   NMLBlocks.ACACIA_BOOKSHELF,  () -> BlockSetType.ACACIA,  "minecraft:acacia"),
            new SecretDoorFamily("dark_oak", NMLBlocks.DARK_OAK_BOOKSHELF,() -> BlockSetType.DARK_OAK,"minecraft:dark_oak"),
            new SecretDoorFamily("mangrove", NMLBlocks.MANGROVE_BOOKSHELF,() -> BlockSetType.MANGROVE,"minecraft:mangrove"),
            new SecretDoorFamily("cherry",   NMLBlocks.CHERRY_BOOKSHELF,  () -> BlockSetType.CHERRY,  "minecraft:cherry"),
            new SecretDoorFamily("bamboo",   NMLBlocks.BAMBOO_BOOKSHELF,  () -> BlockSetType.BAMBOO,  "minecraft:bamboo"),
            new SecretDoorFamily("crimson",  NMLBlocks.CRIMSON_BOOKSHELF, () -> BlockSetType.CRIMSON, "minecraft:crimson"),
            new SecretDoorFamily("warped",   NMLBlocks.WARPED_BOOKSHELF,  () -> BlockSetType.WARPED,  "minecraft:warped")
    );

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
        CompatRegistrar.registerSecretDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, SECRET_DOOR_FAMILIES);
    }

    private NMLContent() {}

    @Override public Mods mod()         { return Mods.NOMANSLAND; }
    @Override public String namespace() { return "nomansland"; }
    @Override public DeferredRegister<Block> blocks()            { return BLOCKS; }
    @Override public DeferredRegister<Item> items()              { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors()    { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems()           { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies()                   { return WOOD_FAMILIES; }
    @Override public List<SecretDoorFamily> secretDoorFamilies()       { return SECRET_DOOR_FAMILIES; }

    /** NML generates its own blockstates; ATN must not produce conflicting ones. */
    @Override public boolean requiresBlockStateGeneration()            { return false; }

    @Override public String recipeFolder(WoodFamily family) { return "wood/" + family.registryName() + "/"; }
}
