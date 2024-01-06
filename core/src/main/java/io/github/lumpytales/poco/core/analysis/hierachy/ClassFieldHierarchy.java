package io.github.lumpytales.poco.core.analysis.hierachy;

import java.util.Collections;
import java.util.List;

/**
 * represents the hierarchy of the classes which have to be processed. The {@link ClassFieldHierarchy} starts with the
 * base class where collectors should be created for. The {@link ClassFieldHierarchyChild} represents
 * the structure.
 *
 * @param baseClass base class where collectors should be created for
 * @param children  the structure itself
 */
public record ClassFieldHierarchy(Class<?> baseClass, List<ClassFieldHierarchyChild> children) {

    /**
     * constructor
     *
     * @param baseClass where collectors should be created for
     * @param children  as structure itself
     */
    public ClassFieldHierarchy(
            final Class<?> baseClass, final List<ClassFieldHierarchyChild> children) {
        this.baseClass = baseClass;
        this.children = children != null ? Collections.unmodifiableList(children) : null;
    }
}
