package io.github.lumpytales.poco.plugin;

import io.github.lumpytales.poco.plugin.tasks.PocoGenerateAllTask;
import org.assertj.core.api.Assertions;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

/**
 * used to test {@link PocoPlugin}
 */
class PocoPluginTest {

    @Test
    void When_plugin_instantiated_Then_expect_java_and_poco_plugin_is_registered() {
        // given
        final var project = ProjectBuilder.builder().build();

        // when
        project.getPluginManager().apply("io.github.lumpytales.poco.gradle-plugin");

        // then
        Assertions.assertThat(
                        project.getPluginManager()
                                .hasPlugin("io.github.lumpytales.poco.gradle-plugin"))
                .isTrue();
        Assertions.assertThat(project.getPluginManager().hasPlugin("java")).isTrue();
    }

    @Test
    void When_plugin_instantiated_Then_expect_generate_all_task_is_registered() {
        // given
        final var project = ProjectBuilder.builder().build();
        project.getPluginManager().apply(JavaPlugin.class);

        // when
        project.getPluginManager().apply("io.github.lumpytales.poco.gradle-plugin");

        // then
        Assertions.assertThat(
                        project.getTasks().withType(PocoGenerateAllTask.class).stream().toList())
                .hasSize(1);
    }
}
