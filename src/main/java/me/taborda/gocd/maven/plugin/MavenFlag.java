package me.taborda.gocd.maven.plugin;


import java.util.Arrays;
import java.util.List;

import com.thoughtworks.go.plugin.api.task.TaskConfig;

public enum MavenFlag {

    BATCH("Batch", "-B", true),
    UPDATE("Update", "-U", false),
    QUIET("Quiet", "-q", false),
    DEBUG("Debug", "-X", false),
    OFFLINE("Offline", "-o", false);

    private final String property;
    private final String flag;
    private final boolean activeByDefault;

    MavenFlag(String property, String flag, boolean activeByDefault) {
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

    public static List<MavenFlag> all() {
        return Arrays.asList(values());
    }

    public String getFlag(TaskConfig tc) {
        return isActive(tc) ? flag : "";
    }

    private boolean isActive(TaskConfig tc) {
        return "true".equalsIgnoreCase(tc.getValue(property));
    }
}
