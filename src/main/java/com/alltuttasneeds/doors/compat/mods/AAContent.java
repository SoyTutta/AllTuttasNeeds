package com.alltuttasneeds.doors.compat.mods;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.doors.compat.CompatRegistrar;
import com.alltuttasneeds.doors.compat.LogKind;
import com.alltuttasneeds.doors.compat.ModCompat;
import com.alltuttasneeds.doors.compat.SecretDoorFamily;
import com.alltuttasneeds.doors.compat.WoodFamily;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sheddmer.abundant_atmosphere.common.init.AABlockSetTypes;
import net.sheddmer.abundant_atmosphere.common.init.AABlocks;
import net.sheddmer.abundant_atmosphere.common.integration.NMLIntegration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.alltuttasneeds.doors.compat.DoorVariant.*;

public final class AAContent implements ModCompat {
    public static final AAContent INSTANCE = new AAContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "abundant_atmosphere");
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(Registries.ITEM,  "abundant_atmosphere");

    public static final Map<String, Supplier<? extends Block>> DOORS      = new HashMap<>();
    public static final Map<String, Supplier<Item>>  DOOR_ITEMS = new HashMap<>();

    private static final List<WoodFamily> WOOD_FAMILIES = List.of(
            new WoodFamily("abundant_atmosphere:ashroot", "ashroot",
                    () -> AABlockSetTypes.ASHROOT, AABlocks.ASHROOT_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("abundant_atmosphere:gourdrot", "gourdrot",
                    () -> AABlockSetTypes.GOURDROT, AABlocks.GOURDROT_DOOR,
                    List.of(ORIGINAL, NORMAL, INDISCRETE, TRANSIT, PET, TRAPDOOR)),
            new WoodFamily("abundant_atmosphere:red_bamboo", "red_bamboo",
                    () -> AABlockSetTypes.RED_BAMBOO, AABlocks.RED_BAMBOO_DOOR,
                    List.of(DISCRETE, ORIGINAL, INDISCRETE, TRANSIT, PET, TRAPDOOR), LogKind.BAMBOO)
    );

    private static final List<SecretDoorFamily> SECRET_DOOR_FAMILIES = Mods.NOMANSLAND.isLoaded()
            ? List.of(
            new SecretDoorFamily("ashroot", () -> NMLIntegration.ASHROOT_BOOKSHELF.get(),
                    () -> AABlockSetTypes.ASHROOT, "abundant_atmosphere:ashroot"),
            new SecretDoorFamily("gourdrot", () -> NMLIntegration.GOURDROT_BOOKSHELF.get(),
                    () -> AABlockSetTypes.GOURDROT, "abundant_atmosphere:gourdrot"),
            new SecretDoorFamily("red_bamboo", () -> NMLIntegration.RED_BAMBOO_BOOKSHELF.get(),
                    () -> AABlockSetTypes.RED_BAMBOO, "abundant_atmosphere:red_bamboo")
    )
            : List.of();

    static {
        CompatRegistrar.registerWoodFamilies(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, WOOD_FAMILIES);
        CompatRegistrar.registerSecretDoors(BLOCKS, ITEMS, DOORS, DOOR_ITEMS, SECRET_DOOR_FAMILIES);
    }

    private AAContent() {}

    @Override public Mods mod() { return Mods.ABUNDANT_ATMOSPHERE; }
    @Override public String namespace() { return "abundant_atmosphere"; }
    @Override public DeferredRegister<Block> blocks() { return BLOCKS; }
    @Override public DeferredRegister<Item> items() { return ITEMS; }
    @Override public Map<String, Supplier<? extends Block>> doors() { return DOORS; }
    @Override public Map<String, Supplier<Item>> doorItems() { return DOOR_ITEMS; }
    @Override public List<WoodFamily> woodFamilies() { return WOOD_FAMILIES; }
    @Override public List<SecretDoorFamily> secretDoorFamilies() { return SECRET_DOOR_FAMILIES; }
    @Override public String recipeFolder(WoodFamily family) { return "wood/" + family.registryName() + "/"; }
}
