package com.alltuttasneeds.doors.compat;

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
