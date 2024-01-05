package io.github.lumpytales.poco;

import io.github.lumpytales.poco.testclasses.Person;
import java.nio.file.Path;
import java.util.List;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskExecutionException;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * used to test {@link PocoGeneratorAction}
 */
class PocoGeneratorTest {

    private final PocoGeneratorAction action = Mockito.mock(PocoGeneratorAction.class);

    private final Project project = ProjectBuilder.builder().build();
    private PocoGenerator cut;

    @BeforeEach
    void setup() {
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("io.github.lumpytales.poco");

        cut = project.getTasks().create("gen", PocoGenerator.class);
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
        Assertions.assertThat(result).endsWith(Path.of("build", "generated-poco").toString());
    }

    @Test
    void When_java_plugin_not_registered_Then_expect_exception() {
        // given
        final Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("io.github.lumpytales.poco");

        final var cut = project.getTasks().create("gen", PocoGenerator.class);
        cut.getAction().set(action);
        cut.getBaseClass().set(Person.class.getName());
        cut.getOutputPackageName().set("de.funny");
        cut.getOutput().set(project.getProjectDir());

        // when / then
        Assertions.assertThatThrownBy(cut::generate).isInstanceOf(TaskExecutionException.class);
    }

    @Test
    @SneakyThrows
    void When_generation_is_executed_Then_expect_delegated_to_action() {
        // given

        // when
        cut.generate();

        // then
        Mockito.verify(action, Mockito.times(1))
                .generate(
                        Person.class,
                        "de.funny",
                        List.of(),
                        List.of(),
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

        // when
        cut.generate();

        // then
        Mockito.verify(action, Mockito.times(1))
                .generate(
                        Person.class,
                        "de.funny",
                        List.of(String.class),
                        List.of(String.class.getPackageName()),
                        false,
                        cut.getOutput().get());
    }
}
