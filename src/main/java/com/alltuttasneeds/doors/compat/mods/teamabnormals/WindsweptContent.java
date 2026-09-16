package com.alltuttasneeds.doors.compat.mods.teamabnormals;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.doors.block.SlidingDoorBlock;
import com.alltuttasneeds.doors.compat.CompatRegistrar;
import com.alltuttasneeds.doors.compat.DoorTag;
import com.alltuttasneeds.doors.compat.ExtraDoor;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.SecretDoorFamily;
import com.alltuttasneeds.doors.compat.WoodFamily;
import com.rosemods.windswept.core.registry.WindsweptBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static com.alltuttasneeds.doors.compat.DoorVariant.*;

public final class WindsweptContent implements ModCompat {
    public static final WindsweptContent INSTANCE = new WindsweptContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "windswept");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "windswept");

    public static final Map<String, Supplier<? extends Block>> DOORS = new HashMap<>();
    public static final Map<String, Supplier<Item>> DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("windswept:holly", "holly",
                    () -> WindsweptBlocks.Properties.HOLLY_BLOCK_SET, WindsweptBlocks.HOLLY_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("windswept:pine", "pine",
                    () -> WindsweptBlocks.Properties.PINE_BLOCK_SET, WindsweptBlocks.PINE_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("windswept:chestnut", "chestnut",
                    () -> WindsweptBlocks.Properties.CHESTNUT_BLOCK_SET, WindsweptBlocks.CHESTNUT_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("windswept:gingerbread", "gingerbread",
                    () -> WindsweptBlocks.Properties.GINGERBREAD_SET, WindsweptBlocks.GINGERBREAD_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR),
                    false, UnaryOperator.identity(), "windswept:gingerbread_cookie")
                    .withLogTagId(null)
                    .withStickId("windswept:ginger_root")
    );

    private static final List<ExtraDoor> EXTRA_DOORS = List.of(
            ExtraDoor.of("icicle_bars_sliding_door",
                    () -> WindsweptBlocks.Properties.ICICLE_SET,
                    () -> BlockBehaviour.Properties.ofFullCopy(WindsweptBlocks.ICICLE_BARS.get()).noOcclusion(),
                    SlidingDoorBlock::new,
                    ExtraDoor.barsAnchor("windswept", "icicle_bars_sliding_door"),
                    DoorTag.SLIDING)
    );

    private static final List<SecretDoorFamily> SECRET_DOOR_FAMILIES = List.of(
            new SecretDoorFamily("holly", WindsweptBlocks.HOLLY_BOOKSHELF,
                    () -> WindsweptBlocks.Properties.HOLLY_BLOCK_SET, "windswept:holly"),
            new SecretDoorFamily("pine", WindsweptBlocks.PINE_BOOKSHELF,
                    () -> WindsweptBlocks.Properties.PINE_BLOCK_SET, "windswept:pine"),
            new SecretDoorFamily("chestnut", WindsweptBlocks.CHESTNUT_BOOKSHELF,
                    () -> WindsweptBlocks.Properties.CHESTNUT_BLOCK_SET, "windswept:chestnut")
    );

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
        CompatRegistrar.registerExtraDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, EXTRA_DOORS);
        CompatRegistrar.registerSecretDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, SECRET_DOOR_FAMILIES);
    }

    private WindsweptContent() {}

    @Override public Mods mod() { return Mods.WINDSWEPT; }
    @Override public String namespace() { return "windswept"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
    @Override public List<ExtraDoor> extraDoors() { return EXTRA_DOORS; }
    @Override public List<SecretDoorFamily> secretDoorFamilies() { return SECRET_DOOR_FAMILIES; }
}
