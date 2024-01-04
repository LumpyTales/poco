package io.github.lumpytales.poco.code;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import io.github.lumpytales.poco.CollectorGenerator;
import io.github.lumpytales.poco.testclasses.Order;
import io.github.lumpytales.poco.testclasses.Product;
import jakarta.annotation.Generated;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.lang.model.element.Modifier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * used to test {@link CollectorFactory}
 */
class CollectorFactoryTest {

    private final CollectorFactory cut = new CollectorFactory();

    @Test
    void When_collector_is_generated_Then_expect_class_javadoc() {
        // given
        final var baseClassMethodFieldName = "pojo";
        final var baseClass = Order.class;
        final var classToCollect = Product.class;
        final var codeBlocks = List.<CodeBlock>of();

        // when
        final var result =
                cut.createCollector(
                        baseClassMethodFieldName, baseClass, classToCollect, codeBlocks);

        // then
        Assertions.assertThat(result)
                .extracting(typeSpec -> typeSpec.javadoc)
                .extracting(CodeBlock::toString)
                .isEqualTo(
                        """
                                This class will collect all nested objects of type {@link io.github.lumpytales.poco.testclasses.Product} in object of type {@link io.github.lumpytales.poco.testclasses.Order}

                                BaseClass: {@link io.github.lumpytales.poco.testclasses.Order}
                                ClassToCollect: {@link io.github.lumpytales.poco.testclasses.Product}""");

        Assertions.assertThat(result.modifiers).containsExactly(Modifier.PUBLIC, Modifier.FINAL);
    }

    @Test
    void When_collector_is_generated_Then_expect_specific_name() {
        // given
        final var baseClassMethodFieldName = "pojo";
        final var baseClass = Order.class;
        final var classToCollect = Product.class;
        final var codeBlocks = List.<CodeBlock>of();

        // when
        final var result =
                cut.createCollector(
                        baseClassMethodFieldName, baseClass, classToCollect, codeBlocks);

        // then
        Assertions.assertThat(result)
                .extracting(typeSpec -> typeSpec.name)
                .isEqualTo("ProductCollector");
    }

    @Test
    void When_collector_is_generated_Then_expect_implements_collector_interface() {
        // given
        final var baseClassMethodFieldName = "pojo";
        final var baseClass = Order.class;
        final var classToCollect = Product.class;
        final var codeBlocks = List.<CodeBlock>of();

        // when
        final var result =
                cut.createCollector(
                        baseClassMethodFieldName, baseClass, classToCollect, codeBlocks);

        // then
        Assertions.assertThat(result.superinterfaces)
                .containsExactly(
                        ParameterizedTypeName.get(
                                ClassName.get(Function.class),
                                TypeName.get(Order.class),
                                ParameterizedTypeName.get(
                                        ClassName.get(List.class), TypeName.get(Product.class))));
    }

    @Test
    void When_collector_is_generated_Then_expect_annotations() {
        // given
        final var baseClassMethodFieldName = "pojo";
        final var baseClass = Order.class;
        final var classToCollect = Product.class;
        final var codeBlocks = List.<CodeBlock>of();

        // when
        final var result =
                cut.createCollector(
                        baseClassMethodFieldName, baseClass, classToCollect, codeBlocks);

        // then
        Assertions.assertThat(result.annotations)
                .containsExactly(
                        AnnotationSpec.builder(Generated.class)
                                .addMember("value", "$S", CollectorGenerator.class.getName())
                                .build());
    }

    @Test
    void When_collector_is_generated_Then_expect_collect_from_method() {
        // given
        final var baseClassMethodFieldName = "pojo";
        final var baseClass = Order.class;
        final var classToCollect = Product.class;
        final var codeBlocks = List.<CodeBlock>of();

        // when
        final var result =
                cut.createCollector(
                        baseClassMethodFieldName, baseClass, classToCollect, codeBlocks);

        // then
        Assertions.assertThat(result.methodSpecs).hasSize(1);
        Assertions.assertThat(result.methodSpecs)
                .flatExtracting(methodSpec -> methodSpec.modifiers)
                .containsExactly(Modifier.PUBLIC);
        Assertions.assertThat(result.methodSpecs)
                .extracting(methodSpec -> methodSpec.name)
                .containsExactly("apply");
        Assertions.assertThat(result.methodSpecs)
                .extracting(methodSpec -> methodSpec.javadoc)
                .extracting(CodeBlock::toString)
                .containsExactly(
                        "@return all objects of type {@link"
                                + " io.github.lumpytales.poco.testclasses.Product} from anywhere in"
                                + " base class of type {@link"
                                + " io.github.lumpytales.poco.testclasses.Order}");
        Assertions.assertThat(result.methodSpecs)
                .flatExtracting(methodSpec -> methodSpec.annotations)
                .containsExactly(AnnotationSpec.builder(Override.class).build());
        Assertions.assertThat(result.methodSpecs)
                .extracting(methodSpec -> methodSpec.code)
                .containsExactly(
                        CodeBlock.builder()
                                .addStatement(
                                        "final $T result = new $T<>()",
                                        ParameterizedTypeName.get(
                                                ClassName.get(List.class),
                                                TypeName.get(Product.class)),
                                        ClassName.get(ArrayList.class))
                                .beginControlFlow("if($N == null)", baseClassMethodFieldName)
                                .addStatement("return result")
                                .endControlFlow()
                                .addStatement("return result")
                                .build());
    }
}
