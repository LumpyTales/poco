package io.github.lumpytales.poco.core.analysis.metadata;

import java.util.Collections;
import java.util.List;

/**
 * This structure contains details on the fields for which collectors needs to be created. The {@code baseClass} is the
 * starting point where the {@link FieldMetaData} has been collected from. The {@link FieldMetaData} represents the
 * different (nested) fields, which are part of the same package (or interested in package) as the {@code baseClass},
 * where to create collector classes for.
 *
 * @param baseClass     base class which gets scanned for the fields to create collector classes for
 * @param fieldMetaData the different fields where to create collector classes for
 */
public record ClassMetaData(Class<?> baseClass, List<FieldMetaData> fieldMetaData) {
    /**
     * Constructor
     *
     * @param baseClass     which gets scanned for the fields to create collector classes for
     * @param fieldMetaData where to create collector classes for
     */
    public ClassMetaData(final Class<?> baseClass, final List<FieldMetaData> fieldMetaData) {
        this.baseClass = baseClass;
        this.fieldMetaData =
                fieldMetaData != null ? Collections.unmodifiableList(fieldMetaData) : null;
    }
}
