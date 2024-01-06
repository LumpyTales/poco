package io.github.lumpytales.poco;

import io.github.lumpytales.poco.core.testclasses.Order;
import io.github.lumpytales.poco.core.testclasses.Product;
import io.github.lumpytales.poco.core.testclasses.Tag;
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
@Generated("io.github.lumpytales.poco.core.CollectorGenerator")
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
    if(pojo.getProducts() != null) {
      for(Product pojoProducts : pojo.getProducts()) {
        if(pojoProducts == null) {
          continue;
        }
        if(pojoProducts.getTags() != null) {
          for(Tag pojoProductsTags : pojoProducts.getTags()) {
            if(pojoProductsTags != null) {
              result.add(pojoProductsTags);
            }
          }
        }
      }
    }
    if(pojo.getProducts() != null) {
      for(Product pojoProducts1 : pojo.getProducts()) {
        if(pojoProducts1 == null) {
          continue;
        }
        if(pojoProducts1.getGroupTags() != null) {
          for(Tag pojoProducts1GroupTags : pojoProducts1.getGroupTags().values()) {
            if(pojoProducts1GroupTags != null) {
              result.add(pojoProducts1GroupTags);
            }
          }
        }
      }
    }
    return result;
  }
}
