package com.alltuttasneeds.beds.compat;

import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.TBContent;
import com.alltuttasneeds.beds.compat.mods.FarmersDelightBedContent;
import com.alltuttasneeds.core.Mods;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class BedCompatRegistry {

    private BedCompatRegistry() {}

    private record Entry(Mods mod, Supplier<BedModCompat> compat) {}

    private static final List<Entry> ALL = List.of(
            new Entry(Mods.ALLTUTTASNEEDS, () -> TBContent.INSTANCE),
            new Entry(Mods.FARMERS_DELIGHT, () -> FarmersDelightBedContent.INSTANCE)
    );

    public static Stream<BedModCompat> loaded() {
        return ALL.stream()
                .filter(entry -> entry.mod().isLoaded())
                .map(entry -> entry.compat().get());
    }

    public static void registerFamilies() {
        List<CoverMaterial> covers = loaded().flatMap(compat -> compat.coverMaterials().stream()).toList();
        List<BlanketMaterial> blankets = loaded().flatMap(compat -> compat.blanketMaterials().stream()).toList();
        loaded().forEach(compat -> compat.registerFamilies(covers, blankets));
    }
}
