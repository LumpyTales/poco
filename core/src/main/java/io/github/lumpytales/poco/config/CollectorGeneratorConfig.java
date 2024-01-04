package io.github.lumpytales.poco.config;

import io.github.lumpytales.poco.analysis.collector.ClassCollectorDetector;
import io.github.lumpytales.poco.analysis.hierachy.ClassFieldHierarchyGenerator;
import io.github.lumpytales.poco.analysis.metadata.ClassMetaDataProvider;
import io.github.lumpytales.poco.analysis.path.FieldPathGenerator;
import io.github.lumpytales.poco.code.CodeBlockGenerator;
import io.github.lumpytales.poco.code.CollectorFactory;
import io.github.lumpytales.poco.file.TypeSpecConverter;
import lombok.Getter;

/**
 * configuration of the {@link io.github.lumpytales.poco.CollectorGenerator}
 */
@Getter
public class CollectorGeneratorConfig {
    private final ClassMetaDataProvider classFieldProvider;
    private final ClassFieldHierarchyGenerator classFieldHierarchyGenerator;
    private final FieldPathGenerator classFieldFieldPathGenerator;
    private final CodeBlockGenerator codeBlockGenerator;
    private final CollectorFactory collectorFactory;
    private final ClassCollectorDetector classCollectorDetector;
    private final TypeSpecConverter typeSpecConverter;

    /**
     * initializes the {@link io.github.lumpytales.poco.CollectorGenerator} with default settings
     */
    public CollectorGeneratorConfig() {
        this.classFieldProvider = new ClassMetaDataProvider();
        this.classFieldHierarchyGenerator = new ClassFieldHierarchyGenerator();
        this.classFieldFieldPathGenerator = new FieldPathGenerator();
        this.codeBlockGenerator = new CodeBlockGenerator();
        this.collectorFactory = new CollectorFactory();
        this.classCollectorDetector = new ClassCollectorDetector();
        this.typeSpecConverter = new TypeSpecConverter();
    }
}
