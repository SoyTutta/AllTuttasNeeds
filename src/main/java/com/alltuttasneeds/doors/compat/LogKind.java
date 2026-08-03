package com.alltuttasneeds.doors.compat;

public enum LogKind {
    LOG("_log", "_logs"),
    STEM("_stem", "_stems"),
    BAMBOO("_block", "_blocks");

    private final String suffix;
    private final String tagSuffix;

    LogKind(String suffix, String tagSuffix) {
        this.suffix = suffix;
        this.tagSuffix = tagSuffix;
    }

    public String suffix() {
        return suffix;
    }

    public String tagSuffix() {
        return tagSuffix;
    }
}
