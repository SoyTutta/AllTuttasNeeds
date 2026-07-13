package com.alltuttasneeds.doors.compat;

import com.alltuttasneeds.doors.TDContent;
import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.doors.compat.mods.*;
import com.alltuttasneeds.doors.compat.mods.teamabnormals.*;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class CompatRegistry {

    private CompatRegistry() {}

    private record Entry(Mods mod, Supplier<ModCompat> compat) {}

    private static final List<Entry> ALL = List.of(
            new Entry(Mods.ALLTUTTASNEEDS, () -> TDContent.INSTANCE),
            new Entry(Mods.VANILLABACKPORT, () -> VBContent.INSTANCE),
            new Entry(Mods.NOMANSLAND, () -> NMLContent.INSTANCE),
            new Entry(Mods.NEWWORLD, () -> NWContent.INSTANCE),
            new Entry(Mods.ABUNDANT_ATMOSPHERE, () -> AAContent.INSTANCE),
            new Entry(Mods.CAVERNS_AND_CHASMS, () -> CACContent.INSTANCE),
            new Entry(Mods.UPGRADE_AQUATIC, () -> UAContent.INSTANCE),
            new Entry(Mods.ATMOSPHERIC, () -> ATMContent.INSTANCE),
            new Entry(Mods.ENVIRONMENTAL, () -> EContent.INSTANCE),
            new Entry(Mods.AUTUMNITY, () -> AContent.INSTANCE),
            new Entry(Mods.SPAWN, () -> SContent.INSTANCE),
            new Entry(Mods.NATURES_SPIRIT, () -> NSContent.INSTANCE),
            new Entry(Mods.BIOMESOPLENTY, () -> BoPContent.INSTANCE),
            new Entry(Mods.ENDERSCAPE, () -> ESContent.INSTANCE),
            new Entry(Mods.ARTS_AND_CRAFTS, () -> ACContent.INSTANCE),
            new Entry(Mods.MYNETHERSDELIGHT, () -> MNDContent.INSTANCE),
            new Entry(Mods.ARCHITECTS_PALETTE, () -> APContent.INSTANCE),
            new Entry(Mods.MALUM, () -> MalumContent.INSTANCE),
            new Entry(Mods.CREATE, () -> CreateContent.INSTANCE),
            new Entry(Mods.BLOCKBOX, () -> BBContent.INSTANCE),
            new Entry(Mods.WOODWORKS, () -> WContent.INSTANCE)
    );

    public static Stream<ModCompat> loaded() {
        return ALL.stream()
                .filter(entry -> entry.mod().isLoaded())
                .map(entry -> entry.compat().get());
    }
}
