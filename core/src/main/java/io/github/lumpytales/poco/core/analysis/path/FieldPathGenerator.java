package io.github.lumpytales.poco.core.analysis.path;

import io.github.lumpytales.poco.core.analysis.hierachy.ClassFieldHierarchy;
import io.github.lumpytales.poco.core.analysis.hierachy.ClassFieldHierarchyChild;
import io.github.lumpytales.poco.core.analysis.metadata.FieldMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * used to create all the paths to the types which should be collected always starting at the root
 * <br/>
 * e.g. [["root" -> "nested" -> "classToCollect"], ["root" -> "nested" -> "nested" -> "classToCollect"]]
 */
public class FieldPathGenerator {

    /**
     * Create the paths to the classes which should be collected
     * @param classFieldHierarchy used to find and create the path to the {@code classToCollect}
     * @param classToCollect the class we search for in the hierarchy and create the path for
     * @return list of paths represents by {@link FieldMetaData}
     */
    public List<List<FieldMetaData>> createPathsTo(
            final ClassFieldHierarchy classFieldHierarchy, final Class<?> classToCollect) {
        final List<List<FieldMetaData>> paths = new ArrayList<>();
        final List<FieldMetaData> currentPath = new ArrayList<>();

        for (ClassFieldHierarchyChild child : classFieldHierarchy.children()) {
            findPaths(classToCollect, child, currentPath, paths);
        }

        return paths.stream()
                // remove all paths which doesn't start with the root class
                .filter(
                        fieldMetaDataList ->
                                fieldMetaDataList
                                        .getFirst()
                                        .getParent()
                                        .equals(classFieldHierarchy.baseClass()))
                .toList();
    }

    private void findPaths(
            Class<?> clazzOfInterest,
            ClassFieldHierarchyChild classHierarchy,
            List<FieldMetaData> currentPath,
            List<List<FieldMetaData>> paths) {

        final var field = classHierarchy.fieldMetaData();
        if (field.getField().equals(clazzOfInterest)) {
            currentPath.add(field);

            paths.add(new ArrayList<>(currentPath));
            currentPath.removeLast();
        }

        currentPath.add(field);

        for (ClassFieldHierarchyChild child : classHierarchy.children()) {
            findPaths(clazzOfInterest, child, currentPath, paths);
        }

        currentPath.removeLast();
    }
}
