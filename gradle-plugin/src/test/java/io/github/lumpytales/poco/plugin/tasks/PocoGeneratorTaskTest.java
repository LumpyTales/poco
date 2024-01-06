package io.github.lumpytales.poco.plugin.tasks;

import io.github.lumpytales.poco.plugin.testclasses.Person;
import java.nio.file.Path;
import java.util.List;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * used to test {@link PocoGeneratorAction}
 */
class PocoGeneratorTaskTest {

    private final PocoGeneratorAction action = Mockito.mock(PocoGeneratorAction.class);

    private final Project project = ProjectBuilder.builder().build();
    private PocoGeneratorTask cut;

    @BeforeEach
    void setup() {
        project.getPluginManager().apply("io.github.lumpytales.poco.gradle-plugin");

        cut = project.getTasks().create("gen", PocoGeneratorTask.class);
        cut.getAction().set(action);
        cut.getBaseClass().set(Person.class.getName());
        cut.getOutputPackageName().set("de.funny");
        cut.getOutput().set(project.getProjectDir());
    }

    @Test
    void When_gradle_group_Then_expect_poco() {
        // given
        // when
        final var result = cut.getGroup();

        // then
        Assertions.assertThat(result).isEqualTo("poco");
    }

    @Test
    void When_gradle_output_Then_expect_generated_poco() {
        // given
        // when
        final var result = cut.getOutput().get().toString();

        // then
        Assertions.assertThat(result)
                .endsWith(
                        Path.of("build", "generated", "sources", "poco", "src", "main", "java")
                                .toString());
    }

    @Test
    @SneakyThrows
    void When_generation_is_executed_Then_expect_delegated_to_action() {
        // given

        // when
        cut.run();

        // then
        Mockito.verify(action, Mockito.times(1))
                .generate(
                        Person.class,
                        "de.funny",
                        List.of(),
                        List.of(),
                        null,
                        null,
                        null,
                        cut.getOutput().get());
    }

    @Test
    @SneakyThrows
    void
            When_generation_is_executed_with_specific_classes_to_collect_Then_expect_delegated_to_action() {
        // given
        cut.getClassesToCollect().set(List.of(String.class.getName()));
        cut.getAdditionalPackageOrClassNames().set(List.of(String.class.getPackageName()));
        cut.getGenerateContext().set(false);
        cut.getGeneratedAnnotation().set(lombok.Generated.class.getName());
        cut.getNullableAnnotation().set(Nullable.class.getName());

        // when
        cut.run();

        // then
        Mockito.verify(action, Mockito.times(1))
                .generate(
                        Person.class,
                        "de.funny",
                        List.of(String.class),
                        List.of(String.class.getPackageName()),
                        false,
                        lombok.Generated.class,
                        Nullable.class,
                        cut.getOutput().get());
    }
}
