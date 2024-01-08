package io.github.lumpytales.poco.core;

import jakarta.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

/**
 * Context which allows to interact with all collector classes which have been generated for base class with type {@link B}
 * @param <B> base class to collect from
 */
public interface CollectorContext<B> {

    /**
     * get the list of classes which can be collected from base class
     * @return list of classes which can be collected from base class
     */
    List<Class<?>> get();

    /**
     * get the collector for {@code clazz}
     * @return the collector which is able to collect from base class of type {@link B} the objects of type {@link C}
     * @param <C> type which should be collected
     * @param clazz of object which should be collected
     */
    @Nullable
    <C> Function<B, List<C>> get(final Class<C> clazz);
}
