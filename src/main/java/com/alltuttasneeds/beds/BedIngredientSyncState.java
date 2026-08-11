package com.alltuttasneeds.beds;

import com.alltuttasneeds.beds.config.TBConfig;

import java.util.Set;

public final class BedIngredientSyncState {
    private static volatile Snapshot snapshot = Snapshot.local();

    private BedIngredientSyncState() {}

    public static void apply(Set<String> directApplyDisabled) {
        snapshot = new Snapshot(true, Set.copyOf(directApplyDisabled));
    }

    public static void clear() {
        snapshot = Snapshot.local();
    }

    public static boolean isDirectApplyEnabled(String suffix) {
        Snapshot current = snapshot;
        return current.synced()
                ? !current.directApplyDisabled().contains(suffix)
                : !TBConfig.directApplyDisabled.get().contains(suffix);
    }

    private record Snapshot(boolean synced, Set<String> directApplyDisabled) {
        private static Snapshot local() {
            return new Snapshot(false, Set.of());
        }
    }
}
