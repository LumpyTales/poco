package io.github.lumpytales.poco.core.analysis.metadata;

import jakarta.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;

/**
 * Will create the details which fields (of interest) exists in the class which is currently analyzed.
 */
@Slf4j
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
            Class<?> fieldClazz = field.getType();
            Class<?> wrapperClazz = null;

            if (ParameterizedType.class.isAssignableFrom(field.getGenericType().getClass())) {

                var type = (ParameterizedType) field.getGenericType();
                if (type.getActualTypeArguments().length == 1) {
                    fieldClazz = (Class<?>) type.getActualTypeArguments()[0];
                } else if (type.getActualTypeArguments().length == 2) {
                    fieldClazz = (Class<?>) type.getActualTypeArguments()[1];
                } else {
                    log.warn(
                            "Skipping field [{}] as we can't collect meta data for such types [{}]",
                            field.getName(),
                            field.getType());
                    continue;
                }
                wrapperClazz = field.getType();
            }

            // in case of java-packages we shouldn't dive deeply into the structure
            if (baseClass.getPackageName().startsWith("java")) {
                continue;
            }

            // primitive collectors are not supported
            final var isComponentPrimitive =
                    fieldClazz.componentType() != null && fieldClazz.componentType().isPrimitive();
            if (fieldClazz.isPrimitive() || isComponentPrimitive) {
                log.warn(
                        "Skipping field [{}] as primitive fields are not supported!",
                        field.getName());
                continue;
            }

            if (Enum.class.isAssignableFrom(fieldClazz)) {
                log.warn("Skipping field [{}] as enum fields are not supported!", field.getName());
                continue;
            }

            // if not in the package of interest -> skip
            final var relevantPackage =
                    packageNamePrefixes.stream()
                            .filter(findRelevantPackageFor(fieldClazz))
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
                                instanceof ParameterizedType getterReturnParametrizedType
                        && wrapperClazz
                                .getTypeName()
                                .equals(getterReturnParametrizedType.getRawType().getTypeName())) {
                    if (getterReturnParametrizedType.getActualTypeArguments().length == 1) {
                        getterReturnType = getterReturnParametrizedType.getActualTypeArguments()[0];
                    } else if (getterReturnParametrizedType.getActualTypeArguments().length == 2) {
                        getterReturnType = getterReturnParametrizedType.getActualTypeArguments()[1];
                    } else {
                        log.warn(
                                "Skipping field [{}] as we can't detect the getter [{}]",
                                field.getName(),
                                getterReturnType.getTypeName());
                        continue;
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
