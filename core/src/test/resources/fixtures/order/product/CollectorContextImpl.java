package io.github.lumpytales.poco;

import io.github.lumpytales.poco.core.CollectorContext;
import io.github.lumpytales.poco.core.testclasses.Order;
import io.github.lumpytales.poco.core.testclasses.Product;
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
public final class CollectorContextImpl implements CollectorContext<Order> {
  private final List<Class<?>> collectibles = List.of(Product.class);

  private final Map<Class<?>, Function<Order, ?>> collectorMap = Map.of(Product.class, new ProductCollector());

  /**
   * @return the base class
   */
  @Override
  public Class<Order> getBaseClass() {
    return Order.class;
  }

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
  public <C> Function<Order, List<C>> get(final Class<C> clazz) {
    return (Function<Order, List<C>>) collectorMap.get(clazz);
  }
}
