package io.github.lumpytales.poco.core.config;

import io.github.lumpytales.poco.core.analysis.collector.ClassCollectorDetector;
import io.github.lumpytales.poco.core.analysis.hierachy.ClassFieldHierarchyGenerator;
import io.github.lumpytales.poco.core.analysis.metadata.ClassMetaDataProvider;
import io.github.lumpytales.poco.core.analysis.path.FieldPathGenerator;
import io.github.lumpytales.poco.core.code.CollectorFactory;
import io.github.lumpytales.poco.core.code.annotation.AnnotationFactory;
import io.github.lumpytales.poco.core.code.blocks.CollectorCodeBlockFactory;
import io.github.lumpytales.poco.core.code.blocks.ContainerClassCodeBlockFactory;
import io.github.lumpytales.poco.core.code.blocks.FieldNameFactory;
import io.github.lumpytales.poco.core.file.TypeSpecConverter;
import lombok.Getter;

/**
 * configuration of the {@link io.github.lumpytales.poco.core.CollectorGenerator}
 */
@Getter
public class CollectorGeneratorConfig {
    private final ClassMetaDataProvider classMetaDataProvider;
    private final ClassFieldHierarchyGenerator classFieldHierarchyGenerator;
    private final FieldPathGenerator fieldPathGenerator;
    private final CollectorCodeBlockFactory collectorCodeBlockFactory;
    private final CollectorFactory collectorFactory;
    private final ClassCollectorDetector classCollectorDetector;
    private final TypeSpecConverter typeSpecConverter;
    private final AnnotationFactory annotationFactory;

    /**
     * initializes the {@link io.github.lumpytales.poco.core.CollectorGenerator} with default settings
     */
    public CollectorGeneratorConfig() {
        final var fieldNameFactory = new FieldNameFactory();
        final var containerClassCodeBlockFactory = new ContainerClassCodeBlockFactory();

        this.classMetaDataProvider = new ClassMetaDataProvider();
        this.classFieldHierarchyGenerator = new ClassFieldHierarchyGenerator();
        this.fieldPathGenerator = new FieldPathGenerator();
        this.collectorCodeBlockFactory =
                new CollectorCodeBlockFactory(fieldNameFactory, containerClassCodeBlockFactory);
        this.collectorFactory = new CollectorFactory();
        this.classCollectorDetector = new ClassCollectorDetector();
        this.typeSpecConverter = new TypeSpecConverter();
        this.annotationFactory = new AnnotationFactory();
    }
}
