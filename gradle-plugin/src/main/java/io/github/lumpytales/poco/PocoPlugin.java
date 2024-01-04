package io.github.lumpytales.poco;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * the plugin used to generate pojo collector classes
 */
public class PocoPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(PocoPlugin.class);
    }
}
