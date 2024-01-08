package io.github.lumpytales.poco;

import io.github.lumpytales.poco.core.CollectorContext;
import io.github.lumpytales.poco.core.testclasses.Price;
import io.github.lumpytales.poco.core.testclasses.Product;
import io.github.lumpytales.poco.core.testclasses.Tag;
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
@Generated("io.github.lumpytales.poco.core.CollectorGenerator")
public final class CollectorContextImpl implements CollectorContext<Product> {
  private final List<Class<?>> collectables = List.of(Price.class, Tag.class);

  private final Map<Class<?>, Function<Product, ?>> collectorMap = Map.of(Price.class, new PriceCollector(), Tag.class, new TagCollector());

  /**
   * @return the base class
   */
  @Override
  public Class<Product> getBaseClass() {
    return Product.class;
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
  public <C> Function<Product, List<C>> getCollector(final Class<C> clazz) {
    return (Function<Product, List<C>>) collectorMap.get(clazz);
  }
}
