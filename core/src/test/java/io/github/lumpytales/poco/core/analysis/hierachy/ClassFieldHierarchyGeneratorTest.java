package io.github.lumpytales.poco.core.analysis.hierachy;

import io.github.lumpytales.poco.core.analysis.metadata.ClassMetaData;
import io.github.lumpytales.poco.core.analysis.metadata.FieldMetaData;
import io.github.lumpytales.poco.core.testclasses.Order;
import io.github.lumpytales.poco.core.testclasses.Price;
import io.github.lumpytales.poco.core.testclasses.Product;
import io.github.lumpytales.poco.core.testclasses.Tag;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * used to test {@link ClassFieldHierarchyGenerator}
 */
class ClassFieldHierarchyGeneratorTest {

    private final ClassFieldHierarchyGenerator cut = new ClassFieldHierarchyGenerator();

    @Test
    void When_base_class_has_two_children_Then_expect_them_on_same_level() {
        // given
        final var productField =
                FieldMetaData.builder()
                        .parent(Order.class)
                        .field(Product.class)
                        .fieldName("product")
                        .getterName("getProduct")
                        .build();
        final var tagField =
                FieldMetaData.builder()
                        .parent(Order.class)
                        .field(Tag.class)
                        .fieldName("tags")
                        .wrappedIn(List.class)
                        .getterName("getTags")
                        .build();
        final var fieldMetaData = List.of(productField, tagField);

        final ClassMetaData classMetaData = new ClassMetaData(Order.class, fieldMetaData);

        // when
        final var result = cut.createFrom(classMetaData);

        // then
        Assertions.assertThat(result.baseClass()).isEqualTo(Order.class);
        Assertions.assertThat(result.children())
                .extracting(ClassFieldHierarchyChild::fieldMetaData)
                .containsExactlyInAnyOrder(productField, tagField);
        Assertions.assertThat(result.children())
                .flatExtracting(ClassFieldHierarchyChild::children)
                .containsExactly();
    }

    @Test
    void When_base_class_has_children_with_nested_children_Then_expect_two_level() {
        // given
        final var productField =
                FieldMetaData.builder()
                        .parent(Order.class)
                        .field(Product.class)
                        .fieldName("product")
                        .getterName("getProduct")
                        .build();
        final var tagField =
                FieldMetaData.builder()
                        .parent(Product.class)
                        .field(Tag.class)
                        .fieldName("tags")
                        .wrappedIn(List.class)
                        .getterName("getTags")
                        .build();
        final var fieldMetaData = List.of(productField, tagField);

        final ClassMetaData classMetaData = new ClassMetaData(Order.class, fieldMetaData);

        // when
        final var result = cut.createFrom(classMetaData);

        // then
        Assertions.assertThat(result.baseClass()).isEqualTo(Order.class);
        Assertions.assertThat(result.children())
                .extracting(ClassFieldHierarchyChild::fieldMetaData)
                .containsExactlyInAnyOrder(productField);
        Assertions.assertThat(result.children())
                .filteredOn(child -> child.fieldMetaData().equals(productField))
                .flatExtracting(ClassFieldHierarchyChild::children)
                .extracting(ClassFieldHierarchyChild::fieldMetaData)
                .containsExactlyInAnyOrder(tagField);
    }

    @Test
    void When_base_class_has_children_that_contains_itself_Then_expect_maximum_two_levels() {
        // given
        final var priceField =
                FieldMetaData.builder()
                        .parent(Order.class)
                        .field(Price.class)
                        .fieldName("price")
                        .getterName("getPrice")
                        .build();
        final var priceInPriceField =
                FieldMetaData.builder()
                        .parent(Price.class)
                        .field(Price.class)
                        .fieldName("tax")
                        .getterName("getTax")
                        .build();
        final var fieldMetaData = List.of(priceField, priceInPriceField);

        final ClassMetaData classMetaData = new ClassMetaData(Order.class, fieldMetaData);

        // when
        final var result = cut.createFrom(classMetaData);

        // then
        Assertions.assertThat(result.baseClass()).isEqualTo(Order.class);
        Assertions.assertThat(result.children())
                .extracting(ClassFieldHierarchyChild::fieldMetaData)
                .containsExactlyInAnyOrder(priceField);
        Assertions.assertThat(result.children())
                .filteredOn(child -> child.fieldMetaData().equals(priceField))
                .flatExtracting(ClassFieldHierarchyChild::children)
                .extracting(ClassFieldHierarchyChild::fieldMetaData)
                .containsExactlyInAnyOrder(priceInPriceField);
    }
}
