package com.alltuttasneeds.core.data;

import com.alltuttasneeds.registry.compat.BBContent;
import com.alltuttasneeds.registry.compat.Mods;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")

public class DataMaps extends DataMapProvider {
    public DataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    record OxidationPath(String from, String to, String waxed) {}

    private static final List<OxidationPath> PATHS = List.of(
            new OxidationPath("copper_bars_sliding_door",      "exposed_copper_bars_sliding_door",      "waxed_copper_bars_sliding_door"),
            new OxidationPath("exposed_copper_bars_sliding_door",     "weathered_copper_bars_sliding_door",    "waxed_exposed_copper_bars_sliding_door"),
            new OxidationPath("weathered_copper_bars_sliding_door",   "oxidized_copper_bars_sliding_door",     "waxed_weathered_copper_bars_sliding_door"),
            new OxidationPath("oxidized_copper_bars_sliding_door",    null,                                     "waxed_oxidized_copper_bars_sliding_door")
    );

    @Override
    protected void gather(HolderLookup.Provider provider) {
        var oxidizables = builder(NeoForgeDataMaps.OXIDIZABLES);
        var waxables    = builder(NeoForgeDataMaps.WAXABLES);

        if (!Mods.BLOCKBOX.isLoaded()) {
            return;
        }

        var blockboxLoadedCondition = new ModLoadedCondition("blockbox");

        oxidizables.conditions(blockboxLoadedCondition);
        waxables.conditions(blockboxLoadedCondition);

        for (OxidationPath path : PATHS) {
            Supplier<? extends Block> from   = BBContent.DOORS.get(path.from());
            Supplier<? extends Block> waxed  = BBContent.DOORS.get(path.waxed());

            if (from == null || waxed == null) {
                throw new IllegalStateException("Missing entries in BBContent.DOORS for path: " + path);
            }

            if (path.to() != null) {
                Supplier<? extends Block> to = BBContent.DOORS.get(path.to());
                if (to == null) {
                    throw new IllegalStateException("Missing oxidation destination block: " + path.to());
                }

                oxidizables.add(
                        from.get().builtInRegistryHolder(),
                        new Oxidizable(to.get()),
                        false
                );
            }

            waxables.add(
                    from.get().builtInRegistryHolder(),
                    new Waxable(waxed.get()),
                    false
            );
        }
    }
}