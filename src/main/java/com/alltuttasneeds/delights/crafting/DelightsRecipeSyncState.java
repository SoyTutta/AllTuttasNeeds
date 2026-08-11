package com.alltuttasneeds.delights.crafting;

import com.alltuttasneeds.core.Mods;
import com.alltuttasneeds.delights.config.DelightsConfig;

public final class DelightsRecipeSyncState {
    private static volatile Snapshot snapshot = new Snapshot(false, false);

    private DelightsRecipeSyncState() {}

    public static void apply(boolean useCheeseWedges) {
        snapshot = new Snapshot(true, useCheeseWedges);
    }

    public static void clear() {
        snapshot = new Snapshot(false, false);
    }

    public static boolean useCheeseWedges() {
        Snapshot current = snapshot;
        return current.synced()
                ? current.useCheeseWedges()
                : DelightsConfig.isModuleEnabled()
                && DelightsConfig.useCheeseWedges()
                && Mods.BREWIN_AND_CHEWIN.isLoaded();
    }

    private record Snapshot(boolean synced, boolean useCheeseWedges) {}
}
