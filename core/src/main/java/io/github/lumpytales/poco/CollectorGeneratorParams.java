package io.github.lumpytales.poco;

import jakarta.annotation.Generated;
import jakarta.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

/**
 * used to generate the collector {@link java.util.function.Function} and {@link CollectorContext} classes
 */
@Getter
public class CollectorGeneratorParams {
    /** where to collect from */
    private final Class<?> baseClass;

    /** the package name which should be used for the collector {@link java.util.function.Function} */
    private final String outputPackageName;

    /** classes which should be collected. If unset, it will generate a collector {@link java.util.function.Function} for all nested classes of the base class */
    private List<Class<?>> classesToCollect;

    /** usually only collector classes are generated for classes which exists in the same package as the base class.
     * Here we can add additional package names or even full qualified class names. */
    private List<String> additionalPackageOrClassNames;

    /** annotation which should be used to mark classes as generated */
    @Nullable private Class<? extends Annotation> generatedAnnotation;

    /** whether to create the collector context or only the poco-classes */
    private Boolean generateContext = Boolean.TRUE;

    /**
     * constructor
     * @param baseClass to collect from
     * @param outputPackageName the package name which should be used for the collector {@link java.util.function.Function}
     * @throws IllegalArgumentException in case that a parameter is unset
     */
    public CollectorGeneratorParams(final Class<?> baseClass, final String outputPackageName) {
        if (baseClass == null) {
            throw new IllegalArgumentException(
                    "Parameter baseClass and outputPackageName are mandatory!");
        }
        if (outputPackageName == null) {
            throw new IllegalArgumentException("Parameter outputPackageName is mandatory!");
        }
        this.baseClass = baseClass;
        this.outputPackageName = outputPackageName;
    }

    /**
     * Define which classes should be collected
     * @param classesToCollect {@code null} or a non-modifiable list of classes to collect
     */
    public void setClassesToCollect(@Nullable final List<Class<?>> classesToCollect) {
        this.classesToCollect =
                classesToCollect != null ? Collections.unmodifiableList(classesToCollect) : null;
    }

    /**
     * Define which additional package names or classes should be taken into account
     * @param additionalPackageOrClassNames {@code null} or a non-modifiable list of additional package or class names
     */
    public void setAdditionalPackageOrClassNames(
            @Nullable final List<String> additionalPackageOrClassNames) {
        this.additionalPackageOrClassNames =
                additionalPackageOrClassNames != null
                        ? Collections.unmodifiableList(additionalPackageOrClassNames)
                        : null;
    }

    /**
     * annotation which should be used to mark classes as generated
     * @param generatedAnnotation {@code null} or annotation to use. if {@code null} default annotation is used {@link Generated}
     */
    public void setGeneratedAnnotation(
            @Nullable final Class<? extends Annotation> generatedAnnotation) {
        this.generatedAnnotation = generatedAnnotation;
    }

    /**
     * whether to create the collector context or only the poco-classes.
     * @param generateContext {@code null} or decision to generate or not. {@code null} is treated as {@code false},
     *                                         which means that the context is generated
     */
    public void setGenerateContext(@Nullable final Boolean generateContext) {
        if (generateContext != null) {
            this.generateContext = generateContext;
        }
    }
}
