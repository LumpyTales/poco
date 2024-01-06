package io.github.lumpytales.poco.core.config;

import io.github.lumpytales.poco.core.analysis.collector.ClassCollectorDetector;
import io.github.lumpytales.poco.core.analysis.hierachy.ClassFieldHierarchyGenerator;
import io.github.lumpytales.poco.core.analysis.metadata.ClassMetaDataProvider;
import io.github.lumpytales.poco.core.analysis.path.FieldPathGenerator;
import io.github.lumpytales.poco.core.code.AnnotationFactory;
import io.github.lumpytales.poco.core.code.CollectorFactory;
import io.github.lumpytales.poco.core.code.CollectorMethodBodyGenerator;
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
    private final CollectorMethodBodyGenerator collectorMethodBodyGenerator;
    private final CollectorFactory collectorFactory;
    private final ClassCollectorDetector classCollectorDetector;
    private final TypeSpecConverter typeSpecConverter;
    private final AnnotationFactory annotationFactory;

    /**
     * initializes the {@link io.github.lumpytales.poco.core.CollectorGenerator} with default settings
     */
    public CollectorGeneratorConfig() {
        this.classMetaDataProvider = new ClassMetaDataProvider();
        this.classFieldHierarchyGenerator = new ClassFieldHierarchyGenerator();
        this.fieldPathGenerator = new FieldPathGenerator();
        this.collectorMethodBodyGenerator = new CollectorMethodBodyGenerator();
        this.collectorFactory = new CollectorFactory();
        this.classCollectorDetector = new ClassCollectorDetector();
        this.typeSpecConverter = new TypeSpecConverter();
        this.annotationFactory = new AnnotationFactory();
    }
}
