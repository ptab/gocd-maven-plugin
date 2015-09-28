package me.taborda.gocd.maven.plugin;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.Console;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;

public class MavenTaskExecutor implements TaskExecutor {

    private static final Logger LOGGER = Logger.getLoggerFor(MavenTaskExecutor.class);

    @Override
    public ExecutionResult execute(TaskConfig tc, TaskExecutionContext tec) {
        Console console = tec.console();

        ProcessBuilder mvn = createMavenCommand(tc, tec);

        try {
            Process process = mvn.start();
            console.readErrorOf(process.getErrorStream());
            console.readOutputOf(process.getInputStream());

            int exitCode = process.waitFor();
            process.destroy();
            if (exitCode != 0) {
                return ExecutionResult.failure("Build failure");
            }
        } catch (Exception t) {
            console.printLine(t.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            console.printLine(sw.toString());
            return ExecutionResult.failure("Build failure: " + t.getMessage(), t);
        }

        return ExecutionResult.success("Build success");
    }

    private ProcessBuilder createMavenCommand(TaskConfig tc, TaskExecutionContext tec) {
        List<String> command = new ArrayList<>();

        Map<String, String> env = tec.environment().asMap();
        if (env.containsKey("M2_HOME")) {
            String m2Home = env.get("M2_HOME") + "/bin";
            command.add(new File(m2Home, "mvn").getPath());
        } else {
            command.add("mvn");
        }

        MavenFlag.all().forEach(f -> command.add(f.getFlag(tc)));
        MavenCommand.all().forEach(c -> command.addAll(c.getCommands(tc)));
        LOGGER.info("Building command: " + command);

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.environment().putAll(tec.environment().asMap());
        builder.directory(new File(tec.workingDir()));
        return builder;
    }

}
