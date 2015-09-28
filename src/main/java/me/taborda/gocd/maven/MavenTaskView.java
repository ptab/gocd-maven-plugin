package me.taborda.gocd.maven;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.thoughtworks.go.plugin.api.task.TaskView;

public class MavenTaskView implements TaskView {

    @Override
    public String displayValue() {
        return "Maven";
    }

    @Override
    public String template() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/views/maventask.template.html")))) {
            return reader.lines().reduce("", (acc, s) -> acc + s);
        } catch (IOException e) {
            return "Failed to find template: " + e.getMessage();
        }
    }

}
