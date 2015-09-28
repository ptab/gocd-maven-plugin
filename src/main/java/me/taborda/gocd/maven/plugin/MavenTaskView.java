package me.taborda.gocd.maven.plugin;

import java.io.IOException;

import com.thoughtworks.go.plugin.api.task.TaskView;

/**
 *
 * @author ruckc
 */
public class MavenTaskView implements TaskView {

    @Override
    public String displayValue() {
        return "Maven";
    }

    @Override
    public String template() {
        try {
            return IOUtils.toString(getClass().getResourceAsStream("/views/maventask.template.html"));
        } catch (IOException e) {
            return "Failed to find template: " + e.getMessage();
        }
    }

}
