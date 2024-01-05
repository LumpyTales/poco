package io.github.lumpytales.poco.config;

import io.github.lumpytales.poco.analysis.collector.ClassCollectorDetector;
import io.github.lumpytales.poco.analysis.hierachy.ClassFieldHierarchyGenerator;
import io.github.lumpytales.poco.analysis.metadata.ClassMetaDataProvider;
import io.github.lumpytales.poco.analysis.path.FieldPathGenerator;
import io.github.lumpytales.poco.code.CollectorFactory;
import io.github.lumpytales.poco.code.CollectorMethodBodyGenerator;
import io.github.lumpytales.poco.code.GeneratedAnnotationFactory;
import io.github.lumpytales.poco.file.TypeSpecConverter;
import lombok.Getter;

/**
 * configuration of the {@link io.github.lumpytales.poco.CollectorGenerator}
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
    private final GeneratedAnnotationFactory generatedAnnotationFactory;

    /**
     * initializes the {@link io.github.lumpytales.poco.CollectorGenerator} with default settings
     */
    public CollectorGeneratorConfig() {
        this.classMetaDataProvider = new ClassMetaDataProvider();
        this.classFieldHierarchyGenerator = new ClassFieldHierarchyGenerator();
        this.fieldPathGenerator = new FieldPathGenerator();
        this.collectorMethodBodyGenerator = new CollectorMethodBodyGenerator();
        this.collectorFactory = new CollectorFactory();
        this.classCollectorDetector = new ClassCollectorDetector();
        this.typeSpecConverter = new TypeSpecConverter();
        this.generatedAnnotationFactory = new GeneratedAnnotationFactory();
    }
}
