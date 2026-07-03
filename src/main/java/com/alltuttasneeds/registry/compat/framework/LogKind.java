package com.alltuttasneeds.registry.compat.framework;

/**
 * The registry-name suffix a {@link WoodFamily}'s "log-like" block and its stripped
 * counterpart use. Minecraft has three such conventions, always {@code "<name><suffix>"}
 * and {@code "stripped_<name><suffix>"}:
 * <ul>
 *     <li>{@link #LOG} — regular wood, e.g. {@code oak_log} / {@code stripped_oak_log}.</li>
 *     <li>{@link #STEM} — nether fungi, e.g. {@code crimson_stem} / {@code stripped_crimson_stem}.</li>
 *     <li>{@link #BAMBOO} — bamboo blocks, e.g. {@code bamboo_block} / {@code stripped_bamboo_block}.</li>
 * </ul>
 * Used by {@link WoodFamily#logId()} / {@link WoodFamily#strippedLogId()} so future recipes
 * needing a family's log and stripped-log ingredients never have to special-case fungi or
 * bamboo by hand — the family just declares which convention it follows.
 */
public enum LogKind {
    LOG("_log"),
    STEM("_stem"),
    BAMBOO("_block");

    private final String suffix;

    LogKind(String suffix) {
        this.suffix = suffix;
    }

    public String suffix() {
        return suffix;
    }
}
