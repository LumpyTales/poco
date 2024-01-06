package io.github.lumpytales.poco.plugin;

import org.assertj.core.api.Assertions;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

/**
 * used to test {@link PocoPlugin}
 */
class PocoPluginTest {

    @Test
    void When_plugin_instantiated_Then_expect_poco_plugin_is_registered() {
        final var project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("io.github.lumpytales.poco.gradle-plugin");

        Assertions.assertThat(
                        project.getPluginManager()
                                .hasPlugin("io.github.lumpytales.poco.gradle-plugin"))
                .isTrue();
    }
}
