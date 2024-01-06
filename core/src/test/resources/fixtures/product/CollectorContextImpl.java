package io.github.lumpytales.poco;

import io.github.lumpytales.poco.testclasses.Price;
import io.github.lumpytales.poco.testclasses.Product;
import io.github.lumpytales.poco.testclasses.Tag;
import jakarta.annotation.Generated;
import jakarta.annotation.Nullable;
import java.lang.Class;
import java.lang.Override;
import java.lang.SuppressWarnings;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This class contains all collector instances {@link Function} and can be used to get a specific collector for a specific class to collect!
 */
@Generated("io.github.lumpytales.poco.CollectorGenerator")
public final class CollectorContextImpl implements CollectorContext<Product> {
  private final List<Class<?>> collectibles = List.of(Price.class, Tag.class);

  private final Map<Class<?>, Function<Product, ?>> collectorMap = Map.of(Price.class, new PriceCollector(), Tag.class, new TagCollector());

  /**
   * @return list of classes which can be collected from base class
   */
  @Override
  public List<Class<?>> get() {
    return collectibles;
  }

  /**
   * @param clazz to get the specific collector {@link Function} for
   */
  @Override
  @Nullable
  @SuppressWarnings("unchecked")
  public <C> Function<Product, C> get(final Class<C> clazz) {
    return (Function<Product, C>) collectorMap.get(clazz);
  }
}
