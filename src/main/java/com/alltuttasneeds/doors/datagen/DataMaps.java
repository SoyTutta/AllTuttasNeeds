package com.alltuttasneeds.doors.datagen;

import com.alltuttasneeds.doors.compat.CompatRegistry;
import com.alltuttasneeds.doors.compat.WeatheringDoorChain;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class DataMaps extends DataMapProvider {

    public DataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        var oxidizables = builder(NeoForgeDataMaps.OXIDIZABLES);
        var waxables = builder(NeoForgeDataMaps.WAXABLES);

        CompatRegistry.loaded().forEach(compat -> {
            for (WeatheringDoorChain.Link link : compat.weatheringLinks()) {
                Supplier<? extends Block> from = compat.doors().get(link.from());
                Supplier<? extends Block> waxed = compat.doors().get(link.waxed());
                if (from == null || waxed == null) {
                    throw new IllegalStateException(
                            "Missing weathering chain entries in " + compat.namespace() + " for: " + link);
                }

                if (link.to() != null) {
                    Supplier<? extends Block> to = compat.doors().get(link.to());
                    if (to == null) {
                        throw new IllegalStateException(
                                "Missing oxidation destination block: " + compat.namespace() + ":" + link.to());
                    }
                    oxidizables.add(from.get().builtInRegistryHolder(), new Oxidizable(to.get()), false);
                }

                waxables.add(from.get().builtInRegistryHolder(), new Waxable(waxed.get()), false);
            }
        });
    }
}