package me.taborda.gocd.maven.plugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.thoughtworks.go.plugin.api.task.TaskConfig;

public enum MavenCommand {

    GOALS("Goals", (str) -> Collections.singletonList(str.replaceAll("\\s+", " "))),
    PROFILES("Profiles", (str) -> Collections.singletonList("-P" + str.replaceAll("\\s+", ","))),
    PROPERTIES("Properties", (str) -> Arrays.asList(str.split("(\\r?\\n)+")).stream().map(s -> "-D" + s).collect(Collectors.toList()));

    private final String property;
    private final Function<String, List<String>> commandParser;

    MavenCommand(String property, Function<String, List<String>> commandParser) {
        this.property = property;
        this.commandParser = commandParser;
    }

    public String getProperty() {
        return property;
    }

    public static List<MavenCommand> all() {
        return Arrays.asList(values());
    }

    public List<String> getCommands(TaskConfig tc) {
        return (isActive(tc)) ? commandParser.apply(tc.getValue(property)) : Collections.<String> emptyList();
    }

    public boolean isActive(TaskConfig tc) {
        String v = tc.getValue(property);
        return v != null && !v.trim().isEmpty();
    }

}
