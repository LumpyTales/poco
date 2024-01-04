package io.github.lumpytales.poco.analysis.metadata;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * represents details on the {@link java.lang.reflect.Field} where collector classes should be created for
 */
@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
public class FieldMetaData {
    /** the class where the {@code field} exists in */
    private final Class<?> parent;

    /** the type of the field itself */
    private final Class<?> field;

    /** field name in the {@code parent} */
    private final String fieldName;

    /** getter method name used to get the {@code field} respectively its value */
    private final String getterName;

    /** can be null; only set in case that the {@code field} is wrapped within a {@link java.util.List} or {@link java.util.Map} */
    @Nullable private final Class<?> wrappedIn;
}
