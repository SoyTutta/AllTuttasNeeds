package com.alltuttasneeds.registry.compat.framework;

import com.alltuttasneeds.registry.TDContent;
import com.alltuttasneeds.registry.compat.*;
import com.alltuttasneeds.registry.compat.TeamAbnormals.*;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The list of every mod compat in the pack.
 * <p>
 * <b>Adding support for a new mod is exactly two steps:</b>
 * <ol>
 *     <li>Write its {@code XContent} class implementing {@link ModCompat}.</li>
 *     <li>Add one line to {@link #ALL} below.</li>
 * </ol>
 * Every other system (block/item registration, tags, blockstates, item models,
 * loot tables, creative tab, lang) reads from {@link #loaded()} — no other file
 * needs to change.
 * <p>
 * <b>Why every entry is wrapped in a {@code Supplier}:</b> an {@code XContent} class
 * (e.g. {@code BoPContent}) imports classes from the mod it adds compatibility for
 * (e.g. Biomes O' Plenty). If that mod isn't installed, those classes simply don't
 * exist at runtime — and merely <i>loading</i> the {@code XContent} class (even
 * without calling anything on it) throws {@code NoClassDefFoundError}, because
 * its static initializer references the foreign mod's blocks directly.
 * <p>
 * Wrapping each entry as {@code () -> XContent.INSTANCE} means the class is only
 * loaded once {@link #loaded()} has already confirmed, via {@link Mods#isLoaded()},
 * that the mod is present. {@code Mods#isLoaded()} only does a string lookup against
 * {@code ModList} — it never touches the {@code XContent} class — so it's always
 * safe to evaluate for every entry, every time.
 * <p>
 * <b>Because of this, code outside this class should never reference an
 * {@code XContent} class directly or unconditionally — always go through
 * {@link #loaded()}</b>.
 */
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

    /** Every compat whose backing mod is currently loaded, in registration order. */
    public static Stream<ModCompat> loaded() {
        return ALL.stream()
                .filter(entry -> entry.mod().isLoaded())
                .map(entry -> entry.compat().get());
    }
}
