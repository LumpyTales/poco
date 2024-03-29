package io.github.lumpytales.poco.plugin.tasks;

import io.github.lumpytales.poco.core.CollectorGenerator;
import io.github.lumpytales.poco.core.CollectorGeneratorParams;
import io.github.lumpytales.poco.plugin.environment.FileSystem;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import org.gradle.api.file.Directory;

/**
 * class which will trigger the generation of the collector classes
 */
public class PocoGeneratorAction {

    /** interact with file system */
    private final FileSystem fs;

    /** the collector class generator */
    private final CollectorGenerator generator;

    /**
     * default constructor
     */
    public PocoGeneratorAction() {
        this(new FileSystem());
    }

    /**
     * used for tests
     */
    PocoGeneratorAction(final FileSystem fileSystem) {
        this.fs = fileSystem;
        this.generator = new CollectorGenerator();
    }

    /**
     * delegate to {@link CollectorGenerator#generateFor(CollectorGeneratorParams)}
     *
     * @param baseClass where to collect from
     * @param outputPackageName he package name which should be used for the collector
     * @param classesToCollect classes which should be collected.
     * @param additionalPackageOrClassNames additional package names or even full qualified class names
     * @param generateContext whether to create the collector context or only the poco-classes
     * @param generatedAnnotation annotation which should be used to mark classes as generated instead of the default one
     * @param nullableAnnotation annotation which should be used to mark fields etc. as nullable
     * @param directory where to write the generated collector classes
     * @throws IOException in case collector classes cannot be created
     */
    public void generate(
            final Class<?> baseClass,
            final String outputPackageName,
            @Nullable final List<Class<?>> classesToCollect,
            @Nullable final List<String> additionalPackageOrClassNames,
            @Nullable final Boolean generateContext,
            @Nullable final Class<? extends Annotation> generatedAnnotation,
            @Nullable final Class<? extends Annotation> nullableAnnotation,
            final Directory directory)
            throws IOException {

        final var params = new CollectorGeneratorParams(baseClass, outputPackageName);
        params.setClassesToCollect(classesToCollect);
        params.setAdditionalPackageOrClassNames(additionalPackageOrClassNames);
        params.setGenerateContext(generateContext);
        params.setGeneratedAnnotation(generatedAnnotation);
        params.setNullableAnnotation(nullableAnnotation);

        final var result = generator.generateFor(params);

        final var packageNames = outputPackageName.split("\\.");
        var dir = directory;
        for (var packageName : packageNames) {
            dir = dir.dir(packageName);
            final var dirFile = dir.getAsFile();
            if (!fs.exists(dirFile.toPath())) {
                final var mkdir = dirFile.mkdir();
                if (!mkdir) {
                    throw new IOException("Unable to create directory: " + dirFile);
                }
            }
        }
        for (var fileData : result) {
            final var file = dir.file(fileData.getName()).getAsFile();
            fs.writeString(file.toPath(), fileData.getContent());
        }
    }
}
