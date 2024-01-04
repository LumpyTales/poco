package io.github.lumpytales.poco.analysis.hierachy;

import io.github.lumpytales.poco.analysis.metadata.FieldMetaData;
import java.util.Collections;
import java.util.List;

/**
 * As part of the {@link ClassFieldHierarchy} it represents the structure.
 *
 * @param fieldMetaData current field in the hierarchy
 * @param children      children of the field
 */
public record ClassFieldHierarchyChild(
        FieldMetaData fieldMetaData, List<ClassFieldHierarchyChild> children) {
    /**
     * constructor
     *
     * @param fieldMetaData current field in the hierarchy
     * @param children      children of the field
     */
    public ClassFieldHierarchyChild(
            final FieldMetaData fieldMetaData, final List<ClassFieldHierarchyChild> children) {
        this.fieldMetaData = fieldMetaData != null ? fieldMetaData.toBuilder().build() : null;
        this.children = children != null ? Collections.unmodifiableList(children) : null;
    }
}
