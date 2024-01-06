package io.github.lumpytales.poco;

import io.github.lumpytales.poco.core.testclasses.Order;
import io.github.lumpytales.poco.core.testclasses.Product;
import jakarta.annotation.Generated;
import java.lang.Override;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class will collect all nested objects of type {@link Product} in object of type {@link Order}
 *
 * BaseClass: {@link Order}
 * ClassToCollect: {@link Product}
 */
@Generated("io.github.lumpytales.poco.core.CollectorGenerator")
public final class ProductCollector implements Function<Order, List<Product>> {
  /**
   * @return all objects of type {@link Product} from anywhere in base class of type {@link Order}
   */
  @Override
  public List<Product> apply(final Order pojo) {
    final List<Product> result = new ArrayList<>();
    if(pojo == null) {
      return result;
    }
    if(pojo.getProducts() != null) {
      for(Product pojoProducts : pojo.getProducts()) {
        if(pojoProducts != null) {
          result.add(pojoProducts);
        }
      }
    }
    return result;
  }
}
