package com.alltuttasneeds.registry;

import com.alltuttasneeds.blocks.SlidingDoorBlock;
import com.alltuttasneeds.registry.compat.Mods;
import com.alltuttasneeds.registry.compat.framework.CompatRegistrar;
import com.alltuttasneeds.registry.compat.framework.DoorTag;
import com.alltuttasneeds.registry.compat.framework.DoorVariant;
import com.alltuttasneeds.registry.compat.framework.ExtraDoor;
import com.alltuttasneeds.registry.compat.framework.LogKind;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.SecretDoorFamily;
import com.alltuttasneeds.registry.compat.framework.WoodFamily;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static com.alltuttasneeds.registry.compat.framework.DoorVariant.*;

public final class TDContent implements ModCompat {
    public static final TDContent INSTANCE = new TDContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "tuttasdoors");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "tuttasdoors");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    public static final DeferredHolder<Item, Item> ICON_ITEM;

    static {
        String iconName = "oak_discrete_door";
        Supplier<Block> iconBlock = BLOCKS.register(iconName,
                () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
        DOORS.put(iconName, iconBlock);
        ICON_ITEM = ITEMS.register(iconName, () -> new BlockItem(iconBlock.get(), new Item.Properties()));
        DOOR_ITEMS.put(iconName, ICON_ITEM);
    }

    private static final UnaryOperator<BlockBehaviour.Properties> VANILLA_NO_OCCLUSION =
            BlockBehaviour.Properties::noOcclusion;

    private static WoodFamily vanillaFamily(String wood, Supplier<BlockSetType> setType, Supplier<Block> baseDoor,
                                             boolean nonFlammable, List<DoorVariant> order) {
        return vanillaFamily(wood, setType, baseDoor, nonFlammable, order, LogKind.LOG);
    }

    private static WoodFamily vanillaFamily(String wood, Supplier<BlockSetType> setType, Supplier<Block> baseDoor,
                                             boolean nonFlammable, List<DoorVariant> order, LogKind logKind) {
        return new WoodFamily("minecraft:" + wood, wood, setType, baseDoor, order, nonFlammable,
                VANILLA_NO_OCCLUSION, null, logKind);
    }

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            vanillaFamily("oak", () -> BlockSetType.OAK, () -> Blocks.OAK_DOOR, false,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            vanillaFamily("spruce", () -> BlockSetType.SPRUCE, () -> Blocks.SPRUCE_DOOR, false,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            vanillaFamily("birch", () -> BlockSetType.BIRCH, () -> Blocks.BIRCH_DOOR, false,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            vanillaFamily("jungle", () -> BlockSetType.JUNGLE, () -> Blocks.JUNGLE_DOOR, false,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            vanillaFamily("acacia", () -> BlockSetType.ACACIA, () -> Blocks.ACACIA_DOOR, false,
                    List.of(DISCRETE, NORMAL, ORIGINAL, TRANSIT, PET, TRAPDOOR)),
            vanillaFamily("dark_oak", () -> BlockSetType.DARK_OAK, () -> Blocks.DARK_OAK_DOOR, false,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            vanillaFamily("mangrove", () -> BlockSetType.MANGROVE, () -> Blocks.MANGROVE_DOOR, false,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            vanillaFamily("cherry", () -> BlockSetType.CHERRY, () -> Blocks.CHERRY_DOOR, false,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            vanillaFamily("bamboo", () -> BlockSetType.BAMBOO, () -> Blocks.BAMBOO_DOOR, false,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR), LogKind.BAMBOO),
            vanillaFamily("crimson", () -> BlockSetType.CRIMSON, () -> Blocks.CRIMSON_DOOR, true,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR), LogKind.STEM),
            vanillaFamily("warped", () -> BlockSetType.WARPED, () -> Blocks.WARPED_DOOR, true,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR), LogKind.STEM)
    );

    private static final List<ExtraDoor> EXTRA_DOORS = List.of(
            ExtraDoor.of("iron_bars_sliding_door",
                    () -> BlockSetType.IRON,
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR).noOcclusion(),
                    SlidingDoorBlock::new,
                    ResourceLocation.fromNamespaceAndPath("minecraft", "iron_bars"),
                    DoorTag.SLIDING, DoorTag.NEEDS_STONE_TOOL)
    );

    private static final List<SecretDoorFamily> SECRET_DOOR_FAMILIES = List.of(
            new SecretDoorFamily("oak", () -> Blocks.BOOKSHELF, () -> BlockSetType.OAK, "minecraft:oak")
    );

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
        CompatRegistrar.registerExtraDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, EXTRA_DOORS);
        CompatRegistrar.registerSecretDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, SECRET_DOOR_FAMILIES);
    }

    private TDContent() {}

    @Override public Mods mod() { return Mods.ALLTUTTASNEEDS; }
    @Override public String namespace() { return "tuttasdoors"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
    @Override public List<ExtraDoor> extraDoors() { return EXTRA_DOORS; }
    @Override public List<SecretDoorFamily> secretDoorFamilies() { return SECRET_DOOR_FAMILIES; }
}
