package com.alltuttasneeds.registry.compat.TeamAbnormals;

import com.alltuttasneeds.blocks.SlidingDoorBlock;
import com.alltuttasneeds.registry.compat.Mods;
import com.alltuttasneeds.registry.compat.framework.*;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.registry.compat.framework.DoorVariant.*;

/** Caverns & Chasms door compatibility. */
public final class CACContent implements ModCompat {
    public static final CACContent INSTANCE = new CACContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "caverns_and_chasms");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "caverns_and_chasms");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>            DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("caverns_and_chasms:azalea", "azalea",
                    () -> CCBlocks.CCProperties.AZALEA_BLOCK_SET, CCBlocks.AZALEA_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    private static final List<SecretDoorFamily> SECRET_DOOR_FAMILIES = Mods.WOODWORKS.isLoaded()
            ? List.of(new SecretDoorFamily("azalea", CCBlocks.AZALEA_BOOKSHELF,
            () -> CCBlocks.CCProperties.AZALEA_BLOCK_SET, "caverns_and_chasms:azalea"))
            : List.of();

    private static final Supplier<BlockBehaviour.Properties> COPPER_DOOR_PROPS_BASE = () ->
            BlockBehaviour.Properties.of()
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY);

    private static final WeatheringDoorChain.Chain COPPER_CHAIN =
            WeatheringDoorChain.of("caverns_and_chasms", COPPER_DOOR_PROPS_BASE);

    private static final List<ExtraDoor> EXTRA_DOORS = buildExtraDoors();

    private static List<ExtraDoor> buildExtraDoors() {
        List<ExtraDoor> doors = new ArrayList<>(COPPER_CHAIN.doors());

        doors.add(ExtraDoor.of("silver_bars_sliding_door",
                () -> BlockSetType.IRON,
                () -> BlockBehaviour.Properties.of()
                        .strength(5.0F, 6.0F)
                        .requiresCorrectToolForDrops()
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY)
                        .mapColor(MapColor.COLOR_LIGHT_GRAY),
                SlidingDoorBlock::new,
                ExtraDoor.barsAnchor("caverns_and_chasms", "silver_bars_sliding_door"),
                DoorTag.SLIDING, DoorTag.NEEDS_STONE_TOOL));

        doors.add(ExtraDoor.of("tin_bars_sliding_door",
                () -> BlockSetType.IRON,
                () -> BlockBehaviour.Properties.of()
                        .strength(5.0F, 6.0F)
                        .requiresCorrectToolForDrops()
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY)
                        .mapColor(MapColor.TERRACOTTA_WHITE),
                SlidingDoorBlock::new,
                ExtraDoor.barsAnchor("caverns_and_chasms", "tin_bars_sliding_door"),
                DoorTag.SLIDING, DoorTag.NEEDS_STONE_TOOL));

        doors.add(ExtraDoor.of("golden_bars_sliding_door",
                () -> BlockSetType.IRON,
                () -> BlockBehaviour.Properties.of()
                        .strength(5.0F, 6.0F)
                        .requiresCorrectToolForDrops()
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY)
                        .mapColor(MapColor.GOLD),
                SlidingDoorBlock::new,
                ExtraDoor.barsAnchor("caverns_and_chasms", "golden_bars_sliding_door"),
                DoorTag.SLIDING, DoorTag.NEEDS_IRON_TOOL, DoorTag.GUARDED_BY_PIGLINS, DoorTag.PIGLIN_LOVED));

        return List.copyOf(doors);
    }

    static {
        CompatRegistrar.registerExtraDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, EXTRA_DOORS);
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
        CompatRegistrar.registerSecretDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, SECRET_DOOR_FAMILIES);
    }

    private CACContent() {}

    @Override public Mods mod()                                         { return Mods.CAVERNS_AND_CHASMS; }
    @Override public String namespace()                                 { return "caverns_and_chasms"; }
    @Override public DeferredRegister<Block> blocks()                   { return BLOCKS; }
    @Override public DeferredRegister<Item>  items()                    { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors()     { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems()            { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies()                    { return WOOD_FAMILIES; }
    @Override public List<ExtraDoor> extraDoors()                       { return EXTRA_DOORS; }
    @Override public List<SecretDoorFamily> secretDoorFamilies()        { return SECRET_DOOR_FAMILIES; }
    @Override public List<WeatheringDoorChain.Link> weatheringLinks()   { return COPPER_CHAIN.oxidationLinks(); }
}
