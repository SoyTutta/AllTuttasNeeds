package com.alltuttasneeds.delights.face;

import com.alltuttasneeds.delights.config.DelightsConfig;

public final class SnowGolemFeastNameSyncState {
    private static volatile Snapshot snapshot = Snapshot.local();

    private SnowGolemFeastNameSyncState() {}

    public static void apply(boolean alwaysShowNames) {
        snapshot = new Snapshot(true, alwaysShowNames);
    }

    public static void clear() {
        snapshot = Snapshot.local();
    }

    public static boolean alwaysShowNames() {
        Snapshot current = snapshot;
        return current.synced()
                ? current.alwaysShowNames()
                : DelightsConfig.alwaysShowSnowGolemFeastNames();
    }

    private record Snapshot(boolean synced, boolean alwaysShowNames) {
        private static Snapshot local() {
            return new Snapshot(false, true);
        }
    }
}
