package io.github.lumpytales.poco;

import io.github.lumpytales.poco.testclasses.Order;
import io.github.lumpytales.poco.testclasses.Price;
import io.github.lumpytales.poco.testclasses.Product;
import io.github.lumpytales.poco.testclasses.Tag;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CollectorGeneratorIntegrationTest {

    public static final String COLLECTOR_CONTEXT_IMPL_CLASS_NAME = "CollectorContextImpl";
    private static final String OUTPUT_PACKAGE_NAME = "io.github.lumpytales.poco";
    private final CollectorGenerator cut = new CollectorGenerator();

    private static String loadFixture(final String collectorFolder, final String collectorName)
            throws IOException {

        return Files.readString(Path.of(collectorFolder + collectorName + ".java"));
    }

    @Nested
    class OrderBasedTests {

        @Test
        void When_base_class_has_no_nested_classes_of_same_package_Then_expect_no_classes()
                throws IOException {
            // given
            final var cmd = new CollectorGeneratorParams(Tag.class, OUTPUT_PACKAGE_NAME);

            // when
            final var result = cut.generateFor(cmd);

            // then
            Assertions.assertThat(result).hasSize(0);
        }

        @Test
        void When_base_class_is_same_as_class_to_collect_from_Then_expect_no_classes()
                throws IOException {
            // given
            final var cmd = new CollectorGeneratorParams(Order.class, OUTPUT_PACKAGE_NAME);

            final List<Class<?>> classesToCollect = List.of(Order.class);
            cmd.setClassesToCollect(classesToCollect);

            // when
            final var result = cut.generateFor(cmd);

            // then
            Assertions.assertThat(result).hasSize(0);
        }

        @Test
        void When_additional_packages_requested_Then_expect_collector_classes_generated_for_that()
                throws IOException {
            // given
            final var collectorFolder = "src/test/resources/fixtures/order/boxed/";
            final var collectorContainerImplContent =
                    loadFixture(collectorFolder, COLLECTOR_CONTEXT_IMPL_CLASS_NAME);
            final var stringCollector = loadFixture(collectorFolder, "StringCollector");
            final var integerCollector = loadFixture(collectorFolder, "IntegerCollector");
            final var longCollector = loadFixture(collectorFolder, "LongCollector");

            final var cmd = new CollectorGeneratorParams(Tag.class, OUTPUT_PACKAGE_NAME);

            final var additionalPackageOrClassNames = List.of("java.lang");
            cmd.setAdditionalPackageOrClassNames(additionalPackageOrClassNames);

            // when
            final var result = cut.generateFor(cmd);

            // then
            Assertions.assertThat(result)
                    .extracting(FileData::getName)
                    .containsExactlyInAnyOrder(
                            COLLECTOR_CONTEXT_IMPL_CLASS_NAME + ".java",
                            "StringCollector.java",
                            "IntegerCollector.java",
                            "LongCollector.java");

            Assertions.assertThat(result)
                    .extracting(FileData::getContent)
                    .contains(
                            collectorContainerImplContent,
                            stringCollector,
                            integerCollector,
                            longCollector);
        }

        @Test
        void
                When_collector_context_creation_disabled_Then_expect_only_collector_classes_but_no_context()
                        throws IOException {
            // given
            final var collectorFolder = "src/test/resources/fixtures/order/";
            final var collectorContainerImplContent =
                    loadFixture(collectorFolder, COLLECTOR_CONTEXT_IMPL_CLASS_NAME);

            final var cmd = new CollectorGeneratorParams(Order.class, OUTPUT_PACKAGE_NAME);
            cmd.setGenerateContext(false);

            // when
            final var result = cut.generateFor(cmd);

            // then
            Assertions.assertThat(result)
                    .extracting(FileData::getName)
                    .doesNotContain(COLLECTOR_CONTEXT_IMPL_CLASS_NAME + ".java");

            Assertions.assertThat(result)
                    .extracting(FileData::getContent)
                    .doesNotContain(collectorContainerImplContent);
        }

        @Test
        void
                When_no_specific_collectors_selected_Then_expect_collectors_for_all_nested_classes_of_same_package()
                        throws IOException {
            // given
            final var collectorFolder = "src/test/resources/fixtures/order/";
            final var collectorContainerImplContent =
                    loadFixture(collectorFolder, COLLECTOR_CONTEXT_IMPL_CLASS_NAME);

            final var cmd = new CollectorGeneratorParams(Order.class, OUTPUT_PACKAGE_NAME);

            // when
            final var result = cut.generateFor(cmd);

            // then
            Assertions.assertThat(result)
                    .extracting(FileData::getName)
                    .containsExactlyInAnyOrder(
                            COLLECTOR_CONTEXT_IMPL_CLASS_NAME + ".java",
                            "PriceCollector.java",
                            "ProductCollector.java",
                            "TagCollector.java");

            Assertions.assertThat(result)
                    .extracting(FileData::getContent)
                    .contains(collectorContainerImplContent);
        }

        @ParameterizedTest
        @ValueSource(classes = {Product.class, Price.class, Tag.class})
        void
                When_single_collector_needed_Then_create_only_that_collector_with_corresponding_collector_container(
                        final Class<?> clazz) throws IOException {
            // given
            final var collectorFolder =
                    "src/test/resources/fixtures/order/"
                            + clazz.getSimpleName().toLowerCase(Locale.getDefault())
                            + "/";
            final var collectorName = clazz.getSimpleName() + "Collector";

            final var collectorContent = loadFixture(collectorFolder, collectorName);
            final var collectorContainerImplContent =
                    loadFixture(collectorFolder, COLLECTOR_CONTEXT_IMPL_CLASS_NAME);

            final var cmd = new CollectorGeneratorParams(Order.class, OUTPUT_PACKAGE_NAME);

            final List<Class<?>> classesToCollect = List.of(clazz);
            cmd.setClassesToCollect(classesToCollect);

            // when
            final var result = cut.generateFor(cmd);

            // then
            Assertions.assertThat(result)
                    .extracting(FileData::getName)
                    .containsExactlyInAnyOrder(
                            COLLECTOR_CONTEXT_IMPL_CLASS_NAME + ".java", collectorName + ".java");

            Assertions.assertThat(result)
                    .extracting(FileData::getContent)
                    .contains(collectorContainerImplContent);

            Assertions.assertThat(result)
                    .extracting(FileData::getContent)
                    .contains(collectorContent);
        }
    }

    @Nested
    class ProductBasedTests {

        @Test
        void
                When_no_specific_collectors_selected_Then_expect_collectors_for_all_nested_classes_of_same_package()
                        throws IOException {
            // given
            final var collectorFolder = "src/test/resources/fixtures/product/";
            final var collectorContainerImplContent =
                    loadFixture(collectorFolder, COLLECTOR_CONTEXT_IMPL_CLASS_NAME);
            final var priceCollector = loadFixture(collectorFolder, "PriceCollector");

            final var cmd = new CollectorGeneratorParams(Product.class, OUTPUT_PACKAGE_NAME);

            // when
            final var result = cut.generateFor(cmd);

            // then
            Assertions.assertThat(result)
                    .extracting(FileData::getName)
                    .containsExactlyInAnyOrder(
                            COLLECTOR_CONTEXT_IMPL_CLASS_NAME + ".java",
                            "PriceCollector.java",
                            "TagCollector.java");

            Assertions.assertThat(result)
                    .extracting(FileData::getContent)
                    .contains(collectorContainerImplContent, priceCollector);
        }
    }
}
