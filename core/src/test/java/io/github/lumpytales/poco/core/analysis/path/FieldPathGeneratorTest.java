package io.github.lumpytales.poco.core.analysis.path;

import io.github.lumpytales.poco.core.analysis.hierachy.ClassFieldHierarchy;
import io.github.lumpytales.poco.core.analysis.hierachy.ClassFieldHierarchyChild;
import io.github.lumpytales.poco.core.analysis.metadata.FieldMetaData;
import io.github.lumpytales.poco.core.testclasses.Price;
import io.github.lumpytales.poco.core.testclasses.Product;
import io.github.lumpytales.poco.core.testclasses.Tag;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * used to test {@link FieldPathGenerator}
 */
class FieldPathGeneratorTest {

    private final FieldPathGenerator cut = new FieldPathGenerator();

    @Test
    void When_base_class_contains_several_fields_of_class_Then_expect_path_per_field() {
        // given
        final var tagFieldInProduct =
                FieldMetaData.builder()
                        .parent(Product.class)
                        .field(Tag.class)
                        .fieldName("tags")
                        .getterName("getTags")
                        .wrappedIn(List.class)
                        .build();
        final var hierarchyTagInProduct =
                new ClassFieldHierarchyChild(tagFieldInProduct, List.of());
        final var taxFieldInProduct =
                FieldMetaData.builder()
                        .parent(Product.class)
                        .field(Price.class)
                        .fieldName("tax")
                        .getterName("getTax")
                        .build();
        final var hierarchyTaxInProduct =
                new ClassFieldHierarchyChild(taxFieldInProduct, List.of());
        final var priceFieldInProduct =
                FieldMetaData.builder()
                        .parent(Product.class)
                        .field(Price.class)
                        .fieldName("price")
                        .getterName("getPrice")
                        .build();
        final var hierarchyPriceInProduct =
                new ClassFieldHierarchyChild(priceFieldInProduct, List.of());

        final var classFieldHierarchy =
                new ClassFieldHierarchy(
                        Product.class,
                        List.of(
                                hierarchyPriceInProduct,
                                hierarchyTagInProduct,
                                hierarchyTaxInProduct));

        // when
        final var result = cut.createPathsTo(classFieldHierarchy, Price.class);

        // then
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.get(0)).containsExactly(priceFieldInProduct);
        Assertions.assertThat(result.get(1)).containsExactly(taxFieldInProduct);
    }

    @Test
    void
            When_base_class_contains_field_of_class_and_field_contains_itself_Then_expect_path_to_field_and_path_to_field_in_field() {
        // given
        final var priceFieldInPrice =
                FieldMetaData.builder()
                        .parent(Price.class)
                        .field(Price.class)
                        .fieldName("tax")
                        .getterName("getTax")
                        .build();
        final var priceFieldInProduct =
                FieldMetaData.builder()
                        .parent(Product.class)
                        .field(Price.class)
                        .fieldName("price")
                        .getterName("getPrice")
                        .build();
        final var hierarchyPriceInPrice =
                new ClassFieldHierarchyChild(priceFieldInPrice, List.of());
        final var hierarchyPriceInProduct =
                new ClassFieldHierarchyChild(priceFieldInProduct, List.of(hierarchyPriceInPrice));

        final var classFieldHierarchy =
                new ClassFieldHierarchy(Product.class, List.of(hierarchyPriceInProduct));

        // when
        final var result = cut.createPathsTo(classFieldHierarchy, Price.class);

        // then
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.get(0)).containsExactly(priceFieldInProduct);
        Assertions.assertThat(result.get(1))
                .containsExactly(priceFieldInProduct, priceFieldInPrice);
    }
}
