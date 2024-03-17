package io.github.lumpytales.poco.core.code;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import io.github.lumpytales.poco.core.CollectorGenerator;
import io.github.lumpytales.poco.core.code.annotation.AnnotationFactory;
import io.github.lumpytales.poco.core.code.annotation.AnnotationType;
import io.github.lumpytales.poco.core.testclasses.TestAnnotation;
import jakarta.annotation.Generated;
import jakarta.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * used to test {@link AnnotationFactory}
 */
class AnnotationFactoryTest {

    private final AnnotationFactory cut = new AnnotationFactory();

    @ParameterizedTest
    @MethodSource("generatedAnnotationArguments")
    void When_specific_annotation_Then_expect_annotation(
            final Class<? extends Annotation> givenAnnotation,
            final Class<? extends Annotation> expectedAnnotation,
            final AnnotationType type) {
        // given

        // when
        final var result = cut.create(givenAnnotation, type);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.type).isEqualTo(ClassName.get(expectedAnnotation));
    }

    private static Stream<Arguments> generatedAnnotationArguments() {
        return Stream.of(
                Arguments.of(null, Generated.class, AnnotationType.GENERATED),
                Arguments.of(TestAnnotation.class, TestAnnotation.class, AnnotationType.GENERATED),
                Arguments.of(null, Nullable.class, AnnotationType.NULLABLE),
                Arguments.of(TestAnnotation.class, TestAnnotation.class, AnnotationType.NULLABLE));
    }

    @Test
    void When_default_generated_annotation_used_Then_expect_annotation_with_specific_value() {
        // given

        // when
        final var result = cut.create(null, AnnotationType.GENERATED);

        // then
        Assertions.assertThat(result)
                .isEqualTo(
                        AnnotationSpec.builder(Generated.class)
                                .addMember("value", "$S", CollectorGenerator.class.getName())
                                .build());
    }

    @ParameterizedTest
    @ValueSource(classes = {Generated.class, TestAnnotation.class})
    void When_generated_annotation_has_value_field_Then_expect_annotation_with_specific_value(
            Class<? extends Annotation> annotationClass) {
        // given

        // when
        final var result = cut.create(annotationClass, AnnotationType.GENERATED);

        // then
        Assertions.assertThat(result)
                .isEqualTo(
                        AnnotationSpec.builder(annotationClass)
                                .addMember("value", "$S", CollectorGenerator.class.getName())
                                .build());
    }
}
