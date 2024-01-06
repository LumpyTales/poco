package io.github.lumpytales.poco;

import io.github.lumpytales.poco.core.testclasses.Tag;
import jakarta.annotation.Generated;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class will collect all nested objects of type {@link String} in object of type {@link Tag}
 *
 * BaseClass: {@link Tag}
 * ClassToCollect: {@link String}
 */
@Generated("io.github.lumpytales.poco.core.CollectorGenerator")
public final class StringCollector implements Function<Tag, List<String>> {
  /**
   * @return all objects of type {@link String} from anywhere in base class of type {@link Tag}
   */
  @Override
  public List<String> apply(final Tag pojo) {
    final List<String> result = new ArrayList<>();
    if(pojo == null) {
      return result;
    }
    String pojoKey = pojo.getKey();
    if(pojoKey != null) {
      result.add(pojoKey);
    }
    String pojoValue = pojo.getValue();
    if(pojoValue != null) {
      result.add(pojoValue);
    }
    return result;
  }
}
