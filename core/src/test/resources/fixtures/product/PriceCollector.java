package io.github.lumpytales.poco;

import io.github.lumpytales.poco.testclasses.Price;
import io.github.lumpytales.poco.testclasses.Product;
import jakarta.annotation.Generated;
import java.lang.Override;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class will collect all nested objects of type {@link Price} in object of type {@link Product}
 *
 * BaseClass: {@link Product}
 * ClassToCollect: {@link Price}
 */
@Generated("io.github.lumpytales.poco.CollectorGenerator")
public final class PriceCollector implements Function<Product, List<Price>> {
  /**
   * @return all objects of type {@link Price} from anywhere in base class of type {@link Product}
   */
  @Override
  public List<Price> apply(final Product pojo) {
    final List<Price> result = new ArrayList<>();
    if(pojo == null) {
      return result;
    }
    Price pojoPrice = pojo.getPrice();
    if(pojoPrice != null) {
      result.add(pojoPrice);
    }
    Price pojoPrice1 = pojo.getPrice();
    if(pojoPrice1 != null) {
      Price pojoPrice1Tax = pojoPrice1.tax();
      if(pojoPrice1Tax != null) {
        result.add(pojoPrice1Tax);
      }
    }
    return result;
  }
}
