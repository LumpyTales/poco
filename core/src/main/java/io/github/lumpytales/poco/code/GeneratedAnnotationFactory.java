package io.github.lumpytales.poco.code;

import com.squareup.javapoet.AnnotationSpec;
import io.github.lumpytales.poco.CollectorGenerator;
import jakarta.annotation.Generated;
import jakarta.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

/**
 * class will create annotation which should be used to mark class as generated
 */
public class GeneratedAnnotationFactory {

    /**
     * creates an annotation which should be used to mark class as generated
     * @param generatedAnnotation {@code null} or annotation class used to create annotation definition
     * @return annotation which should be used to mark class as generated
     */
    public AnnotationSpec create(@Nullable Class<? extends Annotation> generatedAnnotation) {
        final var annotationClass =
                Objects.requireNonNullElse(generatedAnnotation, Generated.class);
        return getAnnotation(annotationClass);
    }

    private AnnotationSpec getAnnotation(final Class<? extends Annotation> annotationClass) {
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
}
