package io.github.lumpytales.poco;

import io.github.lumpytales.poco.testclasses.Order;
import io.github.lumpytales.poco.testclasses.Product;
import io.github.lumpytales.poco.testclasses.Tag;
import jakarta.annotation.Generated;
import java.lang.Override;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class will collect all nested objects of type {@link Tag} in object of type {@link Order}
 *
 * BaseClass: {@link Order}
 * ClassToCollect: {@link Tag}
 */
@Generated("io.github.lumpytales.poco.CollectorGenerator")
public final class TagCollector implements Function<Order, List<Tag>> {
  /**
   * @return all objects of type {@link Tag} from anywhere in base class of type {@link Order}
   */
  @Override
  public List<Tag> apply(final Order pojo) {
    final List<Tag> result = new ArrayList<>();
    if(pojo == null) {
      return result;
    }
    for(Product pojoProducts : pojo.getProducts()) {
      if(pojoProducts == null) {
        continue;
      }
      for(Tag pojoProductsTags : pojoProducts.getTags()) {
        if(pojoProductsTags != null) {
          result.add(pojoProductsTags);
        }
      }
    }
    for(Product pojoProducts : pojo.getProducts()) {
      if(pojoProducts == null) {
        continue;
      }
      if(pojoProducts.getGroupTags() != null) {
        for(Tag pojoProductsGroupTags : pojoProducts.getGroupTags().values()) {
          if(pojoProductsGroupTags != null) {
            result.add(pojoProductsGroupTags);
          }
        }
      }
    }
    return result;
  }
}
