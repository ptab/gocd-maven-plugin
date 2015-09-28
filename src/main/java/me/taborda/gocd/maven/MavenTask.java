package me.taborda.gocd.maven;

import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.Task;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.thoughtworks.go.plugin.api.task.TaskView;

@Extension
public class MavenTask implements Task {

    private static final Logger LOGGER = Logger.getLoggerFor(MavenTask.class);

    @Override
    public TaskConfig config() {
        TaskConfig config = new TaskConfig();
        Configs.all().forEach(c -> config.addProperty(c.getProperty()));
        Flags.all().forEach(f -> config.addProperty(f.getProperty()).withDefault(Boolean.toString(f.isActiveByDefault())));
        return config;
    }

    @Override
    public TaskExecutor executor() {
        return new MavenTaskExecutor();
    }

    @Override
    public TaskView view() {
        return new MavenTaskView();
    }

    @Override
    public ValidationResult validate(TaskConfig tc) {
        StringBuilder builder = new StringBuilder();
        Flags.active(tc).forEach(f -> builder.append(f.getFlag(tc)).append(" "));
        Configs.all().forEach(c -> c.getCommands(tc).forEach(s -> builder.append(s).append(" ")));
        LOGGER.info("Saved configuration: mvn " + builder.toString());

        ValidationResult result = new ValidationResult();
        if (!Configs.GOALS.isActive(tc)) {
            result.addError(new ValidationError(Configs.GOALS.getProperty(), "Maven goals are required"));
        }
        return result;
    }
}
