package io.github.lumpytales.poco.core.code.annotation;

import com.squareup.javapoet.AnnotationSpec;
import io.github.lumpytales.poco.core.CollectorGenerator;
import jakarta.annotation.Generated;
import jakarta.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

/**
 * class will create annotation which should be used in generated classes
 */
public class AnnotationFactory {

    /**
     * creates an annotation which should be used in the generated classes
     * @param annotation {@code null} or annotation class used to create annotation definition
     * @return annotation which should be used in the generated classes
     */
    @Nullable
    public AnnotationSpec create(
            @Nullable Class<? extends Annotation> annotation, final AnnotationType type) {
        final var annotationClass = Objects.requireNonNullElse(annotation, defaultAnnotation(type));
        return getAnnotation(annotationClass);
    }

    @Nullable
    private AnnotationSpec getAnnotation(final Class<? extends Annotation> annotationClass) {
        if (annotationClass == null) {
            return null;
        }
        final var builder = AnnotationSpec.builder(annotationClass);

        final var hasValueMethodOfTypeString =
                Arrays.stream(annotationClass.getDeclaredMethods())
                        .anyMatch(
                                method ->
                                        "value".equals(method.getName())
                                                && method.getAnnotatedReturnType()
                                                        .getType()
                                                        .getTypeName()
                                                        .contains(String.class.getName()));
        if (hasValueMethodOfTypeString) {
            builder.addMember("value", "$S", CollectorGenerator.class.getName());
        }
        return builder.build();
    }

    @Nullable
    private static Class<? extends Annotation> defaultAnnotation(final AnnotationType type) {
        if (type == AnnotationType.NULLABLE) {
            return Nullable.class;
        }
        if (type == AnnotationType.GENERATED) {
            return Generated.class;
        }
        return null;
    }
}
