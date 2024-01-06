package io.github.lumpytales.poco;

import io.github.lumpytales.poco.testclasses.Tag;
import jakarta.annotation.Generated;
import java.lang.Integer;
import java.lang.Override;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class will collect all nested objects of type {@link Integer} in object of type {@link Tag}
 *
 * BaseClass: {@link Tag}
 * ClassToCollect: {@link Integer}
 */
@Generated("io.github.lumpytales.poco.CollectorGenerator")
public final class IntegerCollector implements Function<Tag, List<Integer>> {
  /**
   * @return all objects of type {@link Integer} from anywhere in base class of type {@link Tag}
   */
  @Override
  public List<Integer> apply(final Tag pojo) {
    final List<Integer> result = new ArrayList<>();
    if(pojo == null) {
      return result;
    }
    Integer pojoOrder = pojo.getOrder();
    if(pojoOrder != null) {
      result.add(pojoOrder);
    }
    return result;
  }
}
