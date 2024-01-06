package io.github.lumpytales.poco.plugin;

import io.github.lumpytales.poco.plugin.tasks.PocoGenerateAllTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;

/**
 * the plugin used to generate pojo collector classes
 */
public class PocoPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        project.getPlugins().apply(JavaPlugin.class);
        project.getPlugins().apply(PocoPlugin.class);

        project.getTasks().register("generateForAll", PocoGenerateAllTask.class);
    }
}
