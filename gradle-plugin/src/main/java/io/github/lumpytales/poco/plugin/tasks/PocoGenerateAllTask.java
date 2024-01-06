package io.github.lumpytales.poco.plugin.tasks;

import lombok.SneakyThrows;
import org.gradle.api.tasks.TaskAction;

/**
 * task will search for all tasks of type {@link PocoGeneratorTask} and will execute them one after another
 */
public class PocoGenerateAllTask extends PocoTask {

    /**
     * will execute all tasks of type {@link PocoGeneratorTask} one after another
     */
    @TaskAction
    @SneakyThrows
    protected void run() {
        final var pocoGeneratorTasks =
                getProject().getTasks().withType(PocoGeneratorTask.class).stream().toList();
        for (PocoGeneratorTask pocoGeneratorTask : pocoGeneratorTasks) {
            pocoGeneratorTask.run();
        }
    }
}
