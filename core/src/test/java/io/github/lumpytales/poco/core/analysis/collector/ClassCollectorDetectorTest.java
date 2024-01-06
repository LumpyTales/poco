package io.github.lumpytales.poco.core.analysis.collector;

import io.github.lumpytales.poco.core.analysis.metadata.ClassMetaData;
import io.github.lumpytales.poco.core.analysis.metadata.FieldMetaData;
import io.github.lumpytales.poco.core.testclasses.Order;
import io.github.lumpytales.poco.core.testclasses.Product;
import io.github.lumpytales.poco.core.testclasses.Tag;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * used to test {@link ClassCollectorDetector}
 */
class ClassCollectorDetectorTest {

    private final ClassCollectorDetector cut = new ClassCollectorDetector();

    @Test
    void When_collector_classes_are_defined_Then_expect_them_as_they_are() {
        // given
        final ClassMetaData classMetaData = new ClassMetaData(Order.class, List.of());
        final List<Class<?>> collectorClasses = List.of(String.class, Integer.class);

        // when
        final var detected = cut.detect(collectorClasses, classMetaData);

        // then
        Assertions.assertThat(detected).isEqualTo(collectorClasses);
        Assertions.assertThat(detected).isNotSameAs(collectorClasses);
    }

    @Test
    void When_collector_classes_are_undefined_Then_expect_them_from_class_meta_data() {
        // given
        final var irrelevantField = FieldMetaData.builder().build();
        final var productField = FieldMetaData.builder().field(Product.class).build();
        final var tagField = FieldMetaData.builder().field(Tag.class).build();
        final var fieldMetaData = List.of(irrelevantField, productField, tagField);

        final ClassMetaData classMetaData = new ClassMetaData(Order.class, fieldMetaData);
        final List<Class<?>> collectorClasses = List.of();

        // when
        final var result = cut.detect(collectorClasses, classMetaData);

        // then
        Assertions.assertThat(result)
                .containsExactlyInAnyOrder(tagField.getField(), productField.getField());
        Assertions.assertThat(result).isNotSameAs(collectorClasses);
    }
}
