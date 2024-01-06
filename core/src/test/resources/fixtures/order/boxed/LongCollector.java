package io.github.lumpytales.poco;

import io.github.lumpytales.poco.core.testclasses.Tag;
import jakarta.annotation.Generated;
import java.lang.Long;
import java.lang.Override;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class will collect all nested objects of type {@link Long} in object of type {@link Tag}
 *
 * BaseClass: {@link Tag}
 * ClassToCollect: {@link Long}
 */
@Generated("io.github.lumpytales.poco.core.CollectorGenerator")
public final class LongCollector implements Function<Tag, List<Long>> {
  /**
   * @return all objects of type {@link Long} from anywhere in base class of type {@link Tag}
   */
  @Override
  public List<Long> apply(final Tag pojo) {
    final List<Long> result = new ArrayList<>();
    if(pojo == null) {
      return result;
    }
    Long pojoWeight = pojo.getWeight();
    if(pojoWeight != null) {
      result.add(pojoWeight);
    }
    return result;
  }
}
