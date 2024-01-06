package io.github.lumpytales.poco;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import lombok.AccessLevel;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.UnknownDomainObjectException;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;

/**
 * default task which will take the config and generate based upon that the collector {@link java.util.function.Function} and
 * {@link CollectorContext}
 */
public abstract class PocoGenerator extends DefaultTask {

    /** action to generate the collector classes */
    @Getter(AccessLevel.PRIVATE)
    private final PocoGeneratorAction defaultAction = new PocoGeneratorAction();

    /** the directory where the classes are generated */
    @Getter(AccessLevel.PRIVATE)
    private final Directory genDirectory =
            getProject()
                    .getLayout()
                    .getBuildDirectory()
                    .dir("generated/sources/poco/src/main/java")
                    .get();

    /** action to generate the collector classes */
    @Internal
    abstract Property<PocoGeneratorAction> getAction();

    /** fully qualified name of the class where to collect from */
    @Input
    public abstract Property<String> getBaseClass();

    /** the package name which should be used for the collector {@link java.util.function.Function} */
    @Input
    public abstract Property<String> getOutputPackageName();

    /** fully qualified name of classes which should be collected. If unset, it will generate a collector {@link java.util.function.Function} for all nested classes of the base class */
    @Input
    @Optional
    public abstract ListProperty<String> getClassesToCollect();

    /** usually only collector classes are generated for classes which exists in the same package as the base class.
     * Here we can add additional package names or even full qualified class names. */
    @Input
    @Optional
    public abstract ListProperty<String> getAdditionalPackageOrClassNames();

    /**
     * whether to create the collector context or only the poco-classes.
     */
    @Input
    @Optional
    public abstract Property<Boolean> getGenerateContext();

    /**
     * fully qualified name of annotation type which should be used to mark classes as generated.
     */
    @Input
    @Optional
    public abstract Property<String> getGeneratedAnnotation();

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

    /**
     * the action
     * @throws IOException in case collector classes cannot be created
     * @throws ClassNotFoundException in case configured classes are not available
     */
    @TaskAction
    @SuppressWarnings("unchecked")
    public void generate() throws IOException, ClassNotFoundException {
        final var classLoader = createClassLoader();

        final var baseClass = Class.forName(getBaseClass().getOrNull(), false, classLoader);
        final var classesToCollect = new ArrayList<Class<?>>();
        if (getClassesToCollect().getOrNull() != null) {
            for (var classToCollect : getClassesToCollect().get()) {
                final var toCollect = Class.forName(classToCollect, false, classLoader);
                classesToCollect.add(toCollect);
            }
        }

        Class<? extends Annotation> generatedAnnotation = null;
        if (getGeneratedAnnotation().getOrNull() != null) {
            generatedAnnotation =
                    (Class<? extends Annotation>)
                            Class.forName(getGeneratedAnnotation().get(), false, classLoader);
        }

        final var action = getAction().getOrElse(getDefaultAction());
        action.generate(
                baseClass,
                getOutputPackageName().getOrNull(),
                classesToCollect,
                getAdditionalPackageOrClassNames().getOrNull(),
                getGenerateContext().getOrNull(),
                generatedAnnotation,
                getOutput().get());
    }

    private ClassLoader createClassLoader() throws TaskExecutionException {
        final var files = new HashSet<File>();
        try {
            final var sourceSets =
                    getProject()
                            .getExtensions()
                            .getByType(JavaPluginExtension.class)
                            .getSourceSets();

            for (var sourceSet : sourceSets) {
                for (var file : sourceSet.getCompileClasspath()) {
                    files.add(file);
                }
                for (var classesDir : sourceSet.getOutput().getClassesDirs()) {
                    files.add(classesDir);
                }
            }
        } catch (UnknownDomainObjectException e) {
            throw new TaskExecutionException(this, e);
        }

        final var urls = new ArrayList<URL>();
        for (var file : files) {
            try {
                final var url = file.toURI().toURL();
                urls.add(url);
            } catch (MalformedURLException e) {
                throw new TaskExecutionException(this, e);
            }
        }

        return new URLClassLoader(urls.toArray(new URL[0]), getClass().getClassLoader());
    }
}
