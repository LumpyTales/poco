package io.github.lumpytales.poco;

import io.github.lumpytales.poco.testclasses.Order;
import io.github.lumpytales.poco.testclasses.Price;
import io.github.lumpytales.poco.testclasses.Product;
import jakarta.annotation.Generated;
import java.lang.Override;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class will collect all nested objects of type {@link Price} in object of type {@link Order}
 *
 * BaseClass: {@link Order}
 * ClassToCollect: {@link Price}
 */
@Generated("io.github.lumpytales.poco.CollectorGenerator")
public final class PriceCollector implements Function<Order, List<Price>> {
  /**
   * @return all objects of type {@link Price} from anywhere in base class of type {@link Order}
   */
  @Override
  public List<Price> apply(final Order pojo) {
    final List<Price> result = new ArrayList<>();
    if(pojo == null) {
      return result;
    }
    for(Product pojoProducts : pojo.getProducts()) {
      if(pojoProducts == null) {
        continue;
      }
      Price pojoProductsPrice = pojoProducts.getPrice();
      if(pojoProductsPrice != null) {
        result.add(pojoProductsPrice);
      }
    }
    for(Product pojoProducts : pojo.getProducts()) {
      if(pojoProducts == null) {
        continue;
      }
      Price pojoProductsPrice = pojoProducts.getPrice();
      if(pojoProductsPrice != null) {
        Price pojoProductsPriceTax = pojoProductsPrice.tax();
        if(pojoProductsPriceTax != null) {
          result.add(pojoProductsPriceTax);
        }
      }
    }
    Price pojoTotal = pojo.getTotal();
    if(pojoTotal != null) {
      result.add(pojoTotal);
    }
    Price pojoTax = pojo.getTax();
    if(pojoTax != null) {
      result.add(pojoTax);
    }
    return result;
  }
}
