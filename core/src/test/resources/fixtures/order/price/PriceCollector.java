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
    if(pojo.getProducts() != null) {
      for(Product pojoProducts : pojo.getProducts()) {
        if(pojoProducts == null) {
          continue;
        }
        Price pojoProductsPrice = pojoProducts.getPrice();
        if(pojoProductsPrice != null) {
          result.add(pojoProductsPrice);
        }
      }
    }
    if(pojo.getProducts() != null) {
      for(Product pojoProducts1 : pojo.getProducts()) {
        if(pojoProducts1 == null) {
          continue;
        }
        Price pojoProducts1Price = pojoProducts1.getPrice();
        if(pojoProducts1Price != null) {
          Price pojoProducts1PriceTax = pojoProducts1Price.tax();
          if(pojoProducts1PriceTax != null) {
            result.add(pojoProducts1PriceTax);
          }
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
