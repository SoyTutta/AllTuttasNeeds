package com.alltuttasneeds.beds.compat;

import com.alltuttasneeds.beds.BlanketMaterial;
import com.alltuttasneeds.beds.CoverMaterial;
import com.alltuttasneeds.beds.TBContent;
import com.alltuttasneeds.beds.compat.mods.FarmersDelightBedContent;
import com.alltuttasneeds.core.Mods;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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
        List<BedModCompat> loadedCompats = loaded().toList();
        List<CoverMaterial> covers = loadedCompats.stream().flatMap(compat -> compat.coverMaterials().stream()).toList();
        List<BlanketMaterial> blankets = loadedCompats.stream().flatMap(compat -> compat.blanketMaterials().stream()).toList();
        validateDeluxeBlanket(blankets);
        loadedCompats.forEach(compat -> compat.registerFamilies(covers, blankets));
        TBContent.registerBlanketItemsForFamilies(
                loadedCompats.stream().flatMap(compat -> compat.families().stream()).toList());
    }

    private static void validateDeluxeBlanket(List<BlanketMaterial> blankets) {
        List<BlanketMaterial> deluxe = blankets.stream()
                .filter(BlanketMaterial::isEnabled)
                .filter(BlanketMaterial::supportsDeluxe)
                .toList();
        if (deluxe.size() <= 1) return;

        String suffixes = deluxe.stream().map(BlanketMaterial::suffix).collect(Collectors.joining(", "));
        throw new IllegalStateException(
                "Only one enabled blanket material can support Deluxe beds; found: " + suffixes);
    }
}
