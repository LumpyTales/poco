package io.github.lumpytales.poco.code;

import com.squareup.javapoet.AnnotationSpec;
import io.github.lumpytales.poco.CollectorGenerator;
import jakarta.annotation.Generated;
import jakarta.annotation.Nullable;
import java.lang.annotation.Annotation;

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
        if (generatedAnnotation == null) {
            return getDefaultAnnotation();
        }

        try {
            final Class<?> annotationType = Class.forName(generatedAnnotation.getName());
            return AnnotationSpec.builder(annotationType).build();
        } catch (ClassNotFoundException e) {
            // do nothing, just return default annotation!
            return getDefaultAnnotation();
        }
    }

    private AnnotationSpec getDefaultAnnotation() {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", CollectorGenerator.class.getName())
                .build();
    }
}
