package io.github.lumpytales.poco.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * used to test {@link CollectorGeneratorParams}
 */
class CollectorGeneratorParamsTest {

    @Test
    void When_base_class_not_set_Then_expect_exception() {
        // given
        // when
        // then
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new CollectorGeneratorParams(null, "not.allowed"));
    }

    @Test
    void When_output_package_name_not_set_Then_expect_exception() {
        // given
        // when
        // then
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new CollectorGeneratorParams(null, "not.allowed"));
    }
}
