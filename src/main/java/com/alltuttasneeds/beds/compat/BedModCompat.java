package com.alltuttasneeds.beds.compat;

import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.MattressFamily;
import com.alltuttasneeds.beds.MattressMaterial;
import com.alltuttasneeds.core.Mods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public interface BedModCompat {

    Mods mod();

    String namespace();

    DeferredRegister<Block> blocks();
    DeferredRegister<Item> items();

    List<MattressMaterial> mattressMaterials();

    default List<CoverMaterial> coverMaterials() {
        return List.of();
    }

    default List<BlanketMaterial> blanketMaterials() {
        return List.of();
    }

    default void registerFamilies(List<CoverMaterial> allCovers, List<BlanketMaterial> allBlankets) {
    }

    default List<MattressFamily> families() {
        return List.of();
    }

    default boolean isLoaded() {
        return mod().isLoaded();
    }

    default ResourceLocation registryLocation(String name) {
        return ResourceLocation.fromNamespaceAndPath(namespace(), name);
    }

    default void registerToBus(IEventBus modEventBus) {
        blocks().register(modEventBus);
        items().register(modEventBus);
    }
}
