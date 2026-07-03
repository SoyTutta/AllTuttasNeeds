package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.registry.compat.framework.CompatRegistrar;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.WoodFamily;
import com.sammy.malum.registry.common.block.MalumBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static com.alltuttasneeds.registry.compat.framework.CompatRegistrar.setTypeFromDoor;
import static com.alltuttasneeds.registry.compat.framework.DoorVariant.*;

/** Malum door compatibility. */
public final class MalumContent implements ModCompat {
    public static final MalumContent INSTANCE = new MalumContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "malum");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "malum");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final Supplier<BlockSetType> RUNEWOOD_SET = setTypeFromDoor(MalumBlocks.RUNEWOOD_DOOR);
    private static final Supplier<BlockSetType> SOULWOOD_SET = setTypeFromDoor(MalumBlocks.SOULWOOD_DOOR);

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("malum:runewood", "runewood",
                    RUNEWOOD_SET, MalumBlocks.RUNEWOOD_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),

            new WoodFamily("malum:bolted_runewood", "bolted_runewood",
                    RUNEWOOD_SET, MalumBlocks.BOLTED_RUNEWOOD_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),

            new WoodFamily("malum:runewood_boards", "runewood_boards",
                    RUNEWOOD_SET, MalumBlocks.RUNEWOOD_BOARDS_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR),
                    false, UnaryOperator.identity(), "#malum:runewood_boards"),

            new WoodFamily("malum:bolted_runewood_boards", "bolted_runewood_boards",
                    RUNEWOOD_SET, MalumBlocks.BOLTED_RUNEWOOD_BOARDS_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR),
                    false, UnaryOperator.identity()),

            new WoodFamily("malum:soulwood", "soulwood",
                    SOULWOOD_SET, MalumBlocks.SOULWOOD_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),

            new WoodFamily("malum:bolted_soulwood", "bolted_soulwood",
                    SOULWOOD_SET, MalumBlocks.BOLTED_SOULWOOD_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),

            new WoodFamily("malum:soulwood_boards", "soulwood_boards",
                    SOULWOOD_SET, MalumBlocks.SOULWOOD_BOARDS_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR),
                    false, UnaryOperator.identity(), "#malum:soulwood_boards"),

            new WoodFamily("malum:bolted_soulwood_boards", "bolted_soulwood_boards",
                    SOULWOOD_SET, MalumBlocks.BOLTED_SOULWOOD_BOARDS_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR),
                    false, UnaryOperator.identity())
    );

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
    }

    private MalumContent() {}

    @Override public Mods mod() { return Mods.MALUM; }
    @Override public String namespace() { return "malum"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
}
