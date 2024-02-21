package io.github.lumpytales.poco.core;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;
import io.github.lumpytales.poco.core.analysis.collector.ClassCollectorDetector;
import io.github.lumpytales.poco.core.analysis.hierachy.ClassFieldHierarchyGenerator;
import io.github.lumpytales.poco.core.analysis.metadata.ClassMetaDataProvider;
import io.github.lumpytales.poco.core.analysis.path.FieldPathGenerator;
import io.github.lumpytales.poco.core.code.CollectorFactory;
import io.github.lumpytales.poco.core.code.annotation.AnnotationFactory;
import io.github.lumpytales.poco.core.code.annotation.AnnotationType;
import io.github.lumpytales.poco.core.code.blocks.CollectorCodeBlockFactory;
import io.github.lumpytales.poco.core.config.CollectorGeneratorConfig;
import io.github.lumpytales.poco.core.file.TypeSpecConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The {@link CollectorGenerator} will create {@link java.util.function.Function} classes for all, or a specified set of,
 * nested objects within the {@code rootClazz}.
 * <br/>
 * Beside that a {@link CollectorContext} will be created which contains the initialized {@link java.util.function.Function} classes
 * and can be used to get the right {@link java.util.function.Function} for the class of interest
 * (which should be collected from the base class)
 */
public class CollectorGenerator {

    private final ClassMetaDataProvider classMetaDataProvider;
    private final ClassFieldHierarchyGenerator classFieldHierarchyGenerator;
    private final FieldPathGenerator fieldPathGenerator;
    private final CollectorCodeBlockFactory collectorCodeBlockFactory;
    private final CollectorFactory collectorFactory;
    private final ClassCollectorDetector classCollectorDetector;
    private final TypeSpecConverter typeSpecConverter;
    private final AnnotationFactory annotationFactory;

    /**
     * initializes the {@link CollectorGenerator}
     */
    public CollectorGenerator() {
        this(new CollectorGeneratorConfig());
    }

    /**
     * used for tests
     * @param config for tests
     */
    CollectorGenerator(final CollectorGeneratorConfig config) {
        this.classMetaDataProvider = config.getClassMetaDataProvider();
        this.classFieldHierarchyGenerator = config.getClassFieldHierarchyGenerator();
        this.fieldPathGenerator = config.getFieldPathGenerator();
        this.collectorCodeBlockFactory = config.getCollectorCodeBlockFactory();
        this.collectorFactory = config.getCollectorFactory();
        this.classCollectorDetector = config.getClassCollectorDetector();
        this.typeSpecConverter = config.getTypeSpecConverter();
        this.annotationFactory = config.getAnnotationFactory();
    }

    /**
     * generates the {@link java.util.function.Function} classes
     *
     * @param params contains details how and where to generate the collector classes
     * @throws IOException in case the java-files can not be written to the {@code outputFolder}
     * @return a list of string containing the generated source-code
     */
    public List<FileData> generateFor(final CollectorGeneratorParams params) throws IOException {

        final var baseClass = params.getBaseClass();
        final var classesToCollect = params.getClassesToCollect();
        final var outputPackageName = params.getOutputPackageName();
        final var generatedAnnotation =
                annotationFactory.create(params.getGeneratedAnnotation(), AnnotationType.GENERATED);
        final var nullableAnnotation =
                annotationFactory.create(params.getNullableAnnotation(), AnnotationType.NULLABLE);
        final Map<AnnotationType, AnnotationSpec> annotationMap =
                new HashMap<>() {
                    {
                        put(AnnotationType.GENERATED, generatedAnnotation);
                        put(AnnotationType.NULLABLE, nullableAnnotation);
                    }
                };

        // create the class metadata which contains the information which (nested) fields of
        // package exists in the base class
        final var classMetaData =
                classMetaDataProvider.create(baseClass, params.getAdditionalPackageOrClassNames());
        // we need a class hierarchy for the fields we collected, to be able to create the path from
        // base class to target field which should be collected
        final var classHierarchy = classFieldHierarchyGenerator.createFrom(classMetaData);

        // just decide which collector classes to create, all the package or only the requested
        // ones
        final var classesToCreateCollector =
                classCollectorDetector.detect(classesToCollect, classMetaData);

        final var collectors = new ArrayList<TypeSpec>();
        for (Class<?> collectorClass : classesToCreateCollector) {
            // create all the paths to the types which should be collected always starting at the
            // root
            // e.g. [["root" -> "nested" -> "classToCollect"], ["root" -> "nested" -> "nested" ->
            // "classToCollect"]]
            final var paths = fieldPathGenerator.createPathsTo(classHierarchy, collectorClass);

            // based on the paths details we can generate the content of the collector class method
            final var codeBlocks = collectorCodeBlockFactory.create(paths);
            if (codeBlocks.isEmpty()) {
                // no content for class - so collector is not necessary
                // usually when the class has no nested classes of same package
                continue;
            }
            // create the class with the generate content for collector method
            final var collector =
                    collectorFactory.createCollector(
                            collectorCodeBlockFactory.getBaseClassObjectName(),
                            baseClass,
                            collectorClass,
                            codeBlocks,
                            annotationMap);
            collectors.add(collector);
        }

        final Set<FileData> collectorsCode = new HashSet<>();
        for (TypeSpec collector : collectors) {
            // convert to collector file
            final var collectorCode = typeSpecConverter.convert(outputPackageName, collector);
            collectorsCode.add(collectorCode);
        }

        // only generate collector container if we have collectors
        if (!collectorsCode.isEmpty() && Boolean.TRUE.equals(params.getGenerateContext())) {
            // create a collector container which allows to get an instance of a collector for a
            // specific class to collect
            final var collectorContainer =
                    collectorFactory.createCollectorContext(
                            outputPackageName, baseClass, collectors, annotationMap);
            final var collectorContainerCode =
                    typeSpecConverter.convert(outputPackageName, collectorContainer);
            collectorsCode.add(collectorContainerCode);
        }

        return new ArrayList<>(collectorsCode);
    }
}
