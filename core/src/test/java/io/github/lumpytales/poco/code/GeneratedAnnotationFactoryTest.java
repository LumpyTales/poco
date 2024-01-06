package io.github.lumpytales.poco.code;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import io.github.lumpytales.poco.CollectorGenerator;
import io.github.lumpytales.poco.testclasses.Gen;
import jakarta.annotation.Generated;
import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * used to test {@link GeneratedAnnotationFactory}
 */
class GeneratedAnnotationFactoryTest {

    private final GeneratedAnnotationFactory cut = new GeneratedAnnotationFactory();

    @ParameterizedTest
    @MethodSource("generatedAnnotationArguments")
    void When_generated_annotation_Then_expect_annotation(
            final Class<? extends Annotation> givenAnnotation,
            final Class<? extends Annotation> expectedAnnotation) {
        // given

        // when
        final var result = cut.create(givenAnnotation);

        // then
        Assertions.assertThat(result.type).isEqualTo(ClassName.get(expectedAnnotation));
    }

    @Test
    void When_default_generated_annotation_Then_expect_annotation_with_specific_value() {
        // given

        // when
        final var result = cut.create(null);

        // then
        Assertions.assertThat(result)
                .isEqualTo(
                        AnnotationSpec.builder(Generated.class)
                                .addMember("value", "$S", CollectorGenerator.class.getName())
                                .build());
    }

    private static Stream<Arguments> generatedAnnotationArguments() {
        return Stream.of(Arguments.of(null, Generated.class), Arguments.of(Gen.class, Gen.class));
    }
}
