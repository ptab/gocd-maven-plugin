package me.taborda.gocd.maven;


import java.util.Arrays;
import java.util.stream.Stream;

import com.thoughtworks.go.plugin.api.task.TaskConfig;

public enum Flags {
    BATCH("Batch", "-B", true),
    UPDATE("Update", "-U", false),
    QUIET("Quiet", "-q", false),
    DEBUG("Debug", "-X", false),
    OFFLINE("Offline", "-o", false);

    private final String property;
    private final String flag;
    private final boolean activeByDefault;

    Flags(String property, String flag, boolean activeByDefault) {
        this.property = property;
        this.flag = flag;
        this.activeByDefault = activeByDefault;
    }

    public String getProperty() {
        return property;
    }

    public boolean isActiveByDefault() {
        return activeByDefault;
    }

    public String getFlag(TaskConfig tc) {
        return isActive(tc) ? flag : "";
    }

    private boolean isActive(TaskConfig tc) {
        return "true".equalsIgnoreCase(tc.getValue(property));
    }

    public static Stream<Flags> all() {
        return Arrays.asList(values()).stream();
    }

    public static Stream<Flags> active(TaskConfig tc) {
        return all().filter(f -> f.isActive(tc));
    }

}
