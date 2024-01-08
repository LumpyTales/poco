package io.github.lumpytales.poco;

import io.github.lumpytales.poco.core.CollectorContext;
import io.github.lumpytales.poco.core.testclasses.Tag;
import jakarta.annotation.Generated;
import jakarta.annotation.Nullable;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This class contains all collector instances {@link Function} and can be used to get a specific collector for a specific class to collect!
 */
@Generated("io.github.lumpytales.poco.core.CollectorGenerator")
public final class CollectorContextImpl implements CollectorContext<Tag> {
  private final List<Class<?>> collectables = List.of(String.class, Integer.class, Long.class);

  private final Map<Class<?>, Function<Tag, ?>> collectorMap = Map.of(String.class, new StringCollector(), Integer.class, new IntegerCollector(), Long.class, new LongCollector());

  /**
   * @return the base class
   */
  @Override
  public Class<Tag> getBaseClass() {
    return Tag.class;
  }

  /**
   * @return list of classes which can be collected from base class
   */
  @Override
  public List<Class<?>> getCollectables() {
    return collectables;
  }

  /**
   * @param clazz to get the specific collector {@link Function} for
   */
  @Override
  @Nullable
  @SuppressWarnings("unchecked")
  public <C> Function<Tag, List<C>> getCollector(final Class<C> clazz) {
    return (Function<Tag, List<C>>) collectorMap.get(clazz);
  }
}
