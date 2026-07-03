package com.alltuttasneeds.registry.compat;

import com.alltuttasneeds.registry.compat.framework.CompatRegistrar;
import com.alltuttasneeds.registry.compat.framework.ModCompat;
import com.alltuttasneeds.registry.compat.framework.WoodFamily;
import com.kekecreations.arts_and_crafts.core.init.ACBlockSetType;
import com.kekecreations.arts_and_crafts.core.registry.ACBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.registry.compat.framework.DoorVariant.*;

/** Arts and Crafts door compatibility. */
public final class ACContent implements ModCompat {
    public static final ACContent INSTANCE = new ACContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "arts_and_crafts");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "arts_and_crafts");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("arts_and_crafts:cork", "cork",
                    () -> ACBlockSetType.CORK_SET, ACBlocks.CORK_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
    }

    private ACContent() {}

    @Override public Mods mod() { return Mods.ARTS_AND_CRAFTS; }
    @Override public String namespace() { return "arts_and_crafts"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
}
