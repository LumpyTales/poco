package io.github.lumpytales.poco.analysis.hierachy;

import io.github.lumpytales.poco.analysis.metadata.ClassMetaData;
import io.github.lumpytales.poco.analysis.metadata.FieldMetaData;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to create the {@link ClassFieldHierarchy} based on the {@link ClassMetaData}
 */
public class ClassFieldHierarchyGenerator {

    /**
     * Create the class field hierarchy
     *
     * @param classMetaData to use to create {@link ClassFieldHierarchy}
     * @return the hierarchy
     */
    public ClassFieldHierarchy createFrom(final ClassMetaData classMetaData) {
        final List<Class<?>> handledClasses = new ArrayList<>();
        final var children =
                classMetaData.fieldMetaData().stream()
                        .map(
                                fieldMetaData ->
                                        createHierarchyChild(
                                                fieldMetaData,
                                                classMetaData.fieldMetaData(),
                                                handledClasses))
                        .collect(Collectors.toList());

        children.removeIf(
                child -> !child.fieldMetaData().getParent().equals(classMetaData.baseClass()));
        return new ClassFieldHierarchy(classMetaData.baseClass(), children);
    }

    private ClassFieldHierarchyChild createHierarchyChild(
            FieldMetaData fieldMetaData,
            List<FieldMetaData> fieldList,
            List<Class<?>> handledClasses) {
        final var currentClass = fieldMetaData.getField();

        // avoid stack overflow in case a class references itself
        final var classContainsItself =
                Arrays.stream(currentClass.getDeclaredFields())
                        .map(Field::getType)
                        .filter(currentClass::equals)
                        .findAny();
        if (classContainsItself.isPresent() && handledClasses.contains(currentClass)) {
            return new ClassFieldHierarchyChild(fieldMetaData, Collections.emptyList());
        }
        handledClasses.add(currentClass);

        // get all the children of the root class
        final var childrenFields =
                fieldList.stream()
                        .filter(classField -> classField.getParent().equals(currentClass))
                        .toList();

        // create class hierarchy from children recursively
        final var children =
                childrenFields.stream()
                        .map(child -> createHierarchyChild(child, fieldList, handledClasses))
                        .collect(Collectors.toList());

        return new ClassFieldHierarchyChild(fieldMetaData, children);
    }
}
