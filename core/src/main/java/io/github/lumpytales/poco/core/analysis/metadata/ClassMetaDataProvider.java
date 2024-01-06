package io.github.lumpytales.poco.core.analysis.metadata;

import jakarta.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Will create the details which fields (of interest) exists in the class which is currently analyzed.
 */
public class ClassMetaDataProvider {

    /**
     * create the class meta data
     *
     * @param additionalClassNamesOrPackageNamePrefixes class names or package name prefixes for the classes which should additionally be recognized next to the base class package
     * @param baseClass to create meta data of the relevant fields for
     * @return the meta data of the {@code baseClass}
     */
    public ClassMetaData create(
            final Class<?> baseClass,
            @Nullable final List<String> additionalClassNamesOrPackageNamePrefixes) {
        final var packageNamePrefixes =
                createPackageNamePrefixes(baseClass, additionalClassNamesOrPackageNamePrefixes);

        final var fieldMetaData =
                createFieldMetaData(baseClass, packageNamePrefixes, new ArrayList<>());
        return new ClassMetaData(baseClass, fieldMetaData);
    }

    private Set<String> createPackageNamePrefixes(
            final Class<?> baseClass, @Nullable List<String> relevantPackageNamePrefixes) {
        final Set<String> packageNamePrefixes = new HashSet<>();
        if (relevantPackageNamePrefixes != null) {
            packageNamePrefixes.addAll(relevantPackageNamePrefixes);
        }

        packageNamePrefixes.add(baseClass.getPackageName());
        return packageNamePrefixes;
    }

    private List<FieldMetaData> createFieldMetaData(
            final Class<?> baseClass,
            final Set<String> packageNamePrefixes,
            final List<Class<?>> handledClasses) {
        if (handledClasses.contains(baseClass)) {
            return Collections.emptyList();
        }
        handledClasses.add(baseClass);

        final var result = new ArrayList<FieldMetaData>();
        final var fields = baseClass.getDeclaredFields();
        final var methods = baseClass.getDeclaredMethods();

        for (final Field field : fields) {
            // in case of a list or map the real type is wrapped in parameter type and we need to
            // get that
            var fieldClazz = field.getType();
            Class<?> wrapperClazz = null;

            // in case of java-packages we shouldn't dive deeply into the structure
            if (baseClass.getPackageName().startsWith("java")) {
                continue;
            }

            // primitive collectors are not supported
            final var isComponentPrimitive =
                    fieldClazz.componentType() != null && fieldClazz.componentType().isPrimitive();
            if (fieldClazz.isPrimitive() || isComponentPrimitive) {
                continue;
            }

            if (Enum.class.isAssignableFrom(fieldClazz)) {
                continue;
            }

            if (List.class.isAssignableFrom(fieldClazz)) {
                var type = (ParameterizedType) field.getGenericType();

                fieldClazz = (Class<?>) type.getActualTypeArguments()[0];
                wrapperClazz = List.class;
            }

            if (Map.class.isAssignableFrom(fieldClazz)) {
                var type = (ParameterizedType) field.getGenericType();

                fieldClazz = (Class<?>) type.getActualTypeArguments()[1];
                wrapperClazz = Map.class;
            }

            // if not in the package of interest -> skip
            final var currentFieldClazz = fieldClazz;
            final var relevantPackage =
                    packageNamePrefixes.stream()
                            .filter(findRelevantPackageFor(currentFieldClazz))
                            .findAny();
            if (relevantPackage.isEmpty()) {
                continue;
            }

            Method getter = null;
            // detect getter method for field
            for (final Method method : methods) {

                // in case the return-type is a list or map the real type is wrapped in parameter
                // type and we need to get that
                Type getterReturnType = method.getGenericReturnType();
                if (wrapperClazz != null
                        && getterReturnType
                                instanceof ParameterizedType getterReturnParametrizedType) {
                    if (List.class.isAssignableFrom(wrapperClazz)
                            && List.class
                                    .getTypeName()
                                    .equals(
                                            getterReturnParametrizedType
                                                    .getRawType()
                                                    .getTypeName())) {
                        getterReturnType = getterReturnParametrizedType.getActualTypeArguments()[0];
                    }
                    if (Map.class.isAssignableFrom(wrapperClazz)
                            && Map.class
                                    .getTypeName()
                                    .equals(
                                            getterReturnParametrizedType
                                                    .getRawType()
                                                    .getTypeName())) {
                        getterReturnType = getterReturnParametrizedType.getActualTypeArguments()[1];
                    }
                }

                // check if the field clazz equals the return type of the getter
                if (fieldClazz.equals(getterReturnType)
                        && method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    getter = method;
                    break;
                }
            }

            // create the field
            final var getterName = getter != null ? getter.getName() : null;
            var classField =
                    FieldMetaData.builder()
                            .field(fieldClazz)
                            .parent(baseClass)
                            .fieldName(field.getName())
                            .wrappedIn(wrapperClazz)
                            .getterName(getterName)
                            .build();

            result.add(classField);

            // recursively detect child class fields
            var fieldMetaData =
                    createFieldMetaData(fieldClazz, packageNamePrefixes, handledClasses);
            result.addAll(fieldMetaData);
        }

        return result;
    }

    private static Predicate<String> findRelevantPackageFor(final Class<?> currentFieldClazz) {
        return relevantPackageNamePrefix -> {
            try {
                final var specificClass = Class.forName(relevantPackageNamePrefix);
                return specificClass.equals(currentFieldClazz);
            } catch (ClassNotFoundException e) {
                return relevantPackageNamePrefix.startsWith(currentFieldClazz.getPackageName());
            }
        };
    }
}
