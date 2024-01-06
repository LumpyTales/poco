package io.github.lumpytales.poco.plugin.tasks;

import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.OutputDirectory;

/**
 * general poco task
 */
public abstract class PocoTask extends DefaultTask {

    /** the directory where the classes are generated */
    @Getter(AccessLevel.PRIVATE)
    private final Directory genDirectory =
            getProject()
                    .getLayout()
                    .getBuildDirectory()
                    .dir("generated/sources/poco/src/main/java")
                    .get();

    /**
     * constructor
     */
    public PocoTask() {
        final var compileJavaTask = getProject().getTasks().findByName("compileJava");
        Objects.requireNonNull(compileJavaTask);

        this.setDependsOn(List.of(compileJavaTask));
    }

    /**
     * where to write the generated collector classes
     */
    @OutputDirectory
    public DirectoryProperty getOutput() {
        return getProject().getObjects().directoryProperty().convention(getGenDirectory());
    }

    @Override
    public String getGroup() {
        return "poco";
    }
}
