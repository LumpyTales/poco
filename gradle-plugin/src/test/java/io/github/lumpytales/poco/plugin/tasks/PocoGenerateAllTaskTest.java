package io.github.lumpytales.poco.plugin.tasks;

import io.github.lumpytales.poco.plugin.testclasses.Contact;
import io.github.lumpytales.poco.plugin.testclasses.Person;
import java.util.List;
import lombok.SneakyThrows;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * used to test {@link PocoGenerateAllTask}
 */
class PocoGenerateAllTaskTest {

    private final PocoGeneratorAction action = Mockito.mock(PocoGeneratorAction.class);
    private final Project project = ProjectBuilder.builder().build();
    private PocoGenerateAllTask cut;

    @BeforeEach
    void setup() {
        project.getPluginManager().apply("io.github.lumpytales.poco.gradle-plugin");

        cut = project.getTasks().create("genAll", PocoGenerateAllTask.class);
    }

    @Test
    @SneakyThrows
    void When_task_executed_Then_expect_all_generator_tasks_executed() {
        // given
        final var task = project.getTasks().create("genTask", PocoGeneratorTask.class);
        task.getAction().set(action);
        task.getBaseClass().set(Person.class.getName());
        task.getOutputPackageName().set("de.funny.person");
        task.getOutput().set(project.getProjectDir());

        final var anotherTask =
                project.getTasks().create("genAnotherTask", PocoGeneratorTask.class);
        anotherTask.getAction().set(action);
        anotherTask.getBaseClass().set(Contact.class.getName());
        anotherTask.getOutputPackageName().set("de.funny.contact");
        anotherTask.getOutput().set(project.getProjectDir());

        // when
        cut.run();

        // then
        Mockito.verify(action, Mockito.times(1))
                .generate(
                        Person.class,
                        "de.funny.person",
                        List.of(),
                        List.of(),
                        null,
                        null,
                        null,
                        task.getOutput().get());
        Mockito.verify(action, Mockito.times(1))
                .generate(
                        Contact.class,
                        "de.funny.contact",
                        List.of(),
                        List.of(),
                        null,
                        null,
                        null,
                        task.getOutput().get());
    }
}
