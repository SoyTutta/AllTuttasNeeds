package com.alltuttasneeds.beds.compat.mods;

import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.MattressFamily;
import com.alltuttasneeds.beds.MattressMaterial;
import com.alltuttasneeds.beds.compat.BedModCompat;
import com.alltuttasneeds.beds.compat.BedRegistrar;
import com.alltuttasneeds.beds.config.TBConfig;
import com.alltuttasneeds.core.Mods;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public final class FarmersDelightBedContent implements BedModCompat {
    public static final FarmersDelightBedContent INSTANCE = new FarmersDelightBedContent();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "farmersdelight");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "farmersdelight");

    private static final BlockBehaviour.Properties MATTRESS_PROPERTIES = BedRegistrar.createMattressProperties();
    private static final BlockBehaviour.Properties BED_PROPERTIES = BedRegistrar.createBedProperties();

    private static final MattressMaterial CANVAS = new MattressMaterial("canvas",
            () -> TBConfig.moduleEnabled.get() && TBConfig.canvasMattressEnabled.get());
    private static final MattressMaterial STRAW = new MattressMaterial("straw",
            () -> TBConfig.moduleEnabled.get() && TBConfig.farmersDelightStrawMattressEnabled.get());

    private static final CoverMaterial CANVAS_COVER = new CoverMaterial("canvas_cover",
            () -> TBConfig.moduleEnabled.get() && TBConfig.canvasCoverEnabled.get());

    private static final List<MattressMaterial> MATERIALS = List.of(CANVAS, STRAW);
    private static final List<CoverMaterial> COVERS = List.of(CANVAS_COVER);

    private static List<MattressFamily> families = List.of();

    private FarmersDelightBedContent() {}

    @Override
    public Mods mod() {
        return Mods.FARMERS_DELIGHT;
    }

    @Override
    public String namespace() {
        return "farmersdelight";
    }

    @Override
    public DeferredRegister<Block> blocks() {
        return BLOCKS;
    }

    @Override
    public DeferredRegister<Item> items() {
        return ITEMS;
    }

    @Override
    public List<MattressMaterial> mattressMaterials() {
        return MATERIALS;
    }

    @Override
    public List<CoverMaterial> coverMaterials() {
        return COVERS;
    }

    @Override
    public void registerFamilies(List<CoverMaterial> allCovers, List<BlanketMaterial> allBlankets) {
        if (!TBConfig.moduleEnabled.get()) return;

        families = BedRegistrar.registerFamilies(
                BLOCKS, ITEMS, MATERIALS, allCovers, allBlankets, MATTRESS_PROPERTIES, BED_PROPERTIES);
    }

    @Override
    public List<MattressFamily> families() {
        return families;
    }
}
