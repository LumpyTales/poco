package io.github.lumpytales.poco.core.code;

import com.squareup.javapoet.CodeBlock;
import io.github.lumpytales.poco.core.analysis.metadata.FieldMetaData;
import io.github.lumpytales.poco.core.code.blocks.CollectorCodeBlockFactory;
import io.github.lumpytales.poco.core.code.blocks.ContainerClassCodeBlockFactory;
import io.github.lumpytales.poco.core.code.blocks.FieldNameFactory;
import io.github.lumpytales.poco.core.testclasses.Order;
import io.github.lumpytales.poco.core.testclasses.Price;
import io.github.lumpytales.poco.core.testclasses.Product;
import io.github.lumpytales.poco.core.testclasses.Tag;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * used to test {@link CollectorCodeBlockFactory}
 */
class CollectorCodeBlockFactoryTest {

    // TODO split in separate testcases
    private final CollectorCodeBlockFactory cut =
            new CollectorCodeBlockFactory(
                    new FieldNameFactory(), new ContainerClassCodeBlockFactory());

    @Test
    void When_base_class_object_name_retrieved_Then_expect_it_to_be_pojo() {
        // given

        // when
        final var result = cut.getBaseClassObjectName();

        // then
        Assertions.assertThat(result).isEqualTo("pojo");
    }

    @Test
    void When_path_with_field_Then_expect_code_block_which_gets_field_object_from_path() {
        // given
        final var productField =
                FieldMetaData.builder()
                        .parent(Order.class)
                        .field(Product.class)
                        .fieldName("product")
                        .getterName("getProduct")
                        .build();
        final var path = List.of(productField);

        final var paths = List.of(path);

        // when
        final var result = cut.create(paths);

        // then
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result)
                .containsExactly(
                        CodeBlock.builder()
                                .addStatement(
                                        "$T pojoProduct = pojo.$N()",
                                        productField.getField(),
                                        productField.getGetterName())
                                .beginControlFlow("if(pojoProduct != null)")
                                .addStatement("result.add(pojoProduct)")
                                .endControlFlow()
                                .build());
    }

    @Test
    void When_paths_with_fields_Then_expect_code_blocks_which_gets_field_objects_from_paths() {
        // given
        final var priceField =
                FieldMetaData.builder()
                        .parent(Product.class)
                        .field(Price.class)
                        .fieldName("price")
                        .getterName("getPrice")
                        .build();

        final var taxField =
                FieldMetaData.builder()
                        .parent(Product.class)
                        .field(Price.class)
                        .fieldName("tax")
                        .getterName("getTax")
                        .build();

        final var skontoField =
                FieldMetaData.builder()
                        .parent(Product.class)
                        .field(Tag.class)
                        .fieldName("skonto")
                        .getterName("getSkonto")
                        .build();

        final var skontoAmountField =
                FieldMetaData.builder()
                        .parent(Tag.class)
                        .field(Price.class)
                        .fieldName("amount")
                        .getterName("getAmount")
                        .build();

        final var pricePath = List.of(priceField);
        final var taxPath = List.of(taxField);
        final var skontoAmountPath = List.of(skontoField, skontoAmountField);

        final var paths = List.of(pricePath, taxPath, skontoAmountPath);

        // when
        final var result = cut.create(paths);

        // then
        Assertions.assertThat(result).hasSize(3);
        Assertions.assertThat(result)
                .containsExactly(
                        CodeBlock.builder()
                                .addStatement(
                                        "$T pojoPrice = pojo.$N()",
                                        priceField.getField(),
                                        priceField.getGetterName())
                                .beginControlFlow("if(pojoPrice != null)")
                                .addStatement("result.add(pojoPrice)")
                                .endControlFlow()
                                .build(),
                        CodeBlock.builder()
                                .addStatement(
                                        "$T pojoTax = pojo.$N()",
                                        taxField.getField(),
                                        taxField.getGetterName())
                                .beginControlFlow("if(pojoTax != null)")
                                .addStatement("result.add(pojoTax)")
                                .endControlFlow()
                                .build(),
                        CodeBlock.builder()
                                .addStatement(
                                        "$T pojoSkonto = pojo.$N()",
                                        skontoField.getField(),
                                        skontoField.getGetterName())
                                .beginControlFlow("if(pojoSkonto != null)")
                                .addStatement(
                                        "$T pojoSkontoAmount = pojoSkonto.$N()",
                                        skontoAmountField.getField(),
                                        skontoAmountField.getGetterName())
                                .beginControlFlow("if(pojoSkontoAmount != null)")
                                .addStatement("result.add(pojoSkontoAmount)")
                                .endControlFlow()
                                .endControlFlow()
                                .build());
    }
}
