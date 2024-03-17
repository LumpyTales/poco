package io.github.lumpytales.poco.core.analysis.metadata;

import io.github.lumpytales.poco.core.testclasses.Order;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * used to test {@link ClassMetaDataProvider}
 */
class ClassMetaDataProviderTest {

    private final ClassMetaDataProvider cut = new ClassMetaDataProvider();

    @Test
    void
            When_package_names_not_defined_Then_create_meta_data_only_for_same_package_as_base_class() {
        // given

        // when
        final var result = cut.create(Order.class, null);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.fieldMetaData())
                .extracting(FieldMetaData::getField)
                .extracting(Class::getSimpleName)
                .containsExactly("Product", "Price", "Price", "Tag", "Tag", "Price", "Price");
        Assertions.assertThat(result.fieldMetaData())
                .extracting(FieldMetaData::getFieldName)
                .containsExactly("products", "price", "tax", "tags", "groupTags", "total", "tax");
    }

    @Test
    void
            When_package_names_defined_Then_create_meta_data_for_same_package_as_base_class_and_additional_ones() {
        // given

        // when
        final var result = cut.create(Order.class, List.of("java.lang.String"));

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.fieldMetaData())
                .extracting(FieldMetaData::getField)
                .extracting(Class::getSimpleName)
                .containsExactly(
                        "Product", "String", "Price", "String", "Price", "Tag", "String", "String",
                        "Tag", "Price", "Price", "String");
        Assertions.assertThat(result.fieldMetaData())
                .extracting(FieldMetaData::getFieldName)
                .containsExactly(
                        "products",
                        "description",
                        "price",
                        "currency",
                        "tax",
                        "tags",
                        "key",
                        "value",
                        "groupTags",
                        "total",
                        "tax",
                        "invoiceRefs");
    }
}
