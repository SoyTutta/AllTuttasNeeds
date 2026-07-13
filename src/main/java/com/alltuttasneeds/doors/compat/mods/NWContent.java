package com.alltuttasneeds.doors.compat.mods;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.doors.compat.CompatRegistrar;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.SecretDoorFamily;
import com.alltuttasneeds.doors.compat.WoodFamily;
import com.kryptography.newworld.init.NWBlocks;
import com.kryptography.newworld.init.data.woodset.FirBlockSet;
import com.kryptography.newworld.integration.NMLIntegration;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.doors.compat.DoorVariant.*;

public final class NWContent implements ModCompat {
    public static final NWContent INSTANCE = new NWContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "newworld");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "newworld");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final Supplier<BlockSetType> FIR_SET = () -> FirBlockSet.FIR_SET;

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("newworld:fir", "fir", FIR_SET, NWBlocks.FIR_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR))
    );

    private static final List<SecretDoorFamily> SECRET_DOOR_FAMILIES = Mods.NOMANSLAND.isLoaded()
            ? List.of(new SecretDoorFamily("fir", () -> NMLIntegration.FIR_BOOKSHELF.get(), FIR_SET, "newworld:fir"))
            : List.of();

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
        CompatRegistrar.registerSecretDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, SECRET_DOOR_FAMILIES);
    }

    private NWContent() {}

    @Override public Mods mod() { return Mods.NEWWORLD; }
    @Override public String namespace() { return "newworld"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
    @Override public List<SecretDoorFamily> secretDoorFamilies() { return SECRET_DOOR_FAMILIES; }
}
