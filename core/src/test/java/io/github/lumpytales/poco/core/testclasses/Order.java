package io.github.lumpytales.poco.core.testclasses;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * order test class
 */
@Getter
@Setter
public class Order {

    /** products of order */
    private final List<Product> products;

    /** total price */
    private Price total;

    /** total tax */
    private Price tax;

    /**
     * @param products products of order
     */
    public Order(final List<Product> products) {
        this.products =
                products != null ? Collections.unmodifiableList(products) : Collections.emptyList();
    }
}
