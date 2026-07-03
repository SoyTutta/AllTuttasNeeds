package com.alltuttasneeds.registry.compat.framework;

/**
 * Generic tag "buckets" a hand-curated {@link ExtraDoor} (sliding/bars doors that
 * don't fit the wood-family pattern) can opt into. Lets tag generation stay fully
 * data-driven even for these one-off doors.
 */
public enum DoorTag {
    SLIDING,
    NEEDS_STONE_TOOL,
    NEEDS_IRON_TOOL,
    PIGLIN_LOVED,
    GUARDED_BY_PIGLINS
}
