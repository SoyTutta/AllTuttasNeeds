package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.blocks.SlidingDoorBlock;
import com.alltuttasneeds.registry.compat.framework.CompatRegistrar;
import com.alltuttasneeds.registry.compat.framework.DoorTag;
import com.alltuttasneeds.registry.compat.framework.ExtraDoor;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.WeatheringDoorChain;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.blockbox.common.registry.ModBlockSets;
import vectorwing.blockbox.common.registry.ModBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/** BlockBox door compatibility */
public final class BBContent implements ModCompat {
    public static final BBContent INSTANCE = new BBContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "blockbox");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "blockbox");

    public static final Map<String, Supplier<? extends Block>> DOORS = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final Supplier<BlockBehaviour.Properties> COPPER_DOOR_PROPS_BASE = () ->
            BlockBehaviour.Properties.of()
                    .strength(3.0F, 6.0F)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .pushReaction(PushReaction.DESTROY);

    private static final WeatheringDoorChain.Chain COPPER_CHAIN =
            WeatheringDoorChain.of("blockbox", COPPER_DOOR_PROPS_BASE);

    private static final List<ExtraDoor> EXTRA_DOORS = buildExtraDoors();

    private static List<ExtraDoor> buildExtraDoors() {
        List<ExtraDoor> doors = new ArrayList<>(COPPER_CHAIN.doors());

        doors.add(ExtraDoor.of("golden_bars_sliding_door",
                ModBlockSets.GOLD,
                () -> BlockBehaviour.Properties.ofFullCopy(ModBlocks.GOLDEN_DOOR.get()).mapColor(MapColor.GOLD),
                SlidingDoorBlock::new,
                ExtraDoor.barsAnchor("blockbox", "golden_bars_sliding_door"),
                DoorTag.SLIDING, DoorTag.NEEDS_IRON_TOOL, DoorTag.GUARDED_BY_PIGLINS, DoorTag.PIGLIN_LOVED));

        return List.copyOf(doors);
    }

    static {
        CompatRegistrar.registerExtraDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, EXTRA_DOORS);
    }

    private BBContent() {}

    @Override public Mods mod() { return Mods.BLOCKBOX; }
    @Override public String namespace() { return "blockbox"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<ExtraDoor> extraDoors() { return EXTRA_DOORS; }
    @Override public List<WeatheringDoorChain.Link> weatheringLinks() { return COPPER_CHAIN.oxidationLinks(); }
}
