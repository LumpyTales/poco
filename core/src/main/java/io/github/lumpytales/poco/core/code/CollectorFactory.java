package io.github.lumpytales.poco.core.code;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;
import io.github.lumpytales.poco.core.CollectorContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.lang.model.element.Modifier;

/**
 * the factory will create the {@link Function} and the {@link CollectorContext} based on the given code blocks.
 */
public class CollectorFactory {
    private static final ClassName JAVA_UTIL_LIST = ClassName.get(List.class);
    private static final ClassName JAVA_UTIL_ARRAY_LIST = ClassName.get(ArrayList.class);
    private static final ClassName JAVA_UTIL_MAP = ClassName.get(Map.class);

    /**
     * Used to create a collector class
     *
     * @param baseClassMethodFieldName name of the field in the method for the base class object
     * @param baseClass where to collect the nested objects of interested from
     * @param classToCollect from the base class
     * @param codeBlocks which are necessary to collect the nested objects from the base object
     * @param annotationMap map which contains annotations which should be used in the generated classes
     * @return class specification for the {@link Function}
     */
    public TypeSpec createCollector(
            final String baseClassMethodFieldName,
            final Class<?> baseClass,
            final Class<?> classToCollect,
            final List<CodeBlock> codeBlocks,
            final Map<AnnotationType, AnnotationSpec> annotationMap) {

        final var generatedAnnotation = annotationMap.get(AnnotationType.GENERATED);

        final var classToCollectFieldName = "result";
        final var baseClassName =
                ClassName.get(baseClass.getPackageName(), baseClass.getSimpleName());
        final var classToCollectName =
                ClassName.get(classToCollect.getPackageName(), classToCollect.getSimpleName());
        final var collectorInterfaceName = ClassName.get(Function.class);
        final var listOfClassToCollects =
                ParameterizedTypeName.get(JAVA_UTIL_LIST, classToCollectName);
        final var collectorInterface =
                ParameterizedTypeName.get(
                        collectorInterfaceName, baseClassName, listOfClassToCollects);

        final var classToCollectField =
                FieldSpec.builder(listOfClassToCollects, classToCollectFieldName, Modifier.PRIVATE)
                        .build();

        final var initialize =
                CodeBlock.builder()
                        .addStatement(
                                "final $T $N = new $T<>()",
                                listOfClassToCollects,
                                classToCollectField,
                                JAVA_UTIL_ARRAY_LIST)
                        .build();

        final var ifPojoNullReturn =
                CodeBlock.builder()
                        .beginControlFlow("if($N == null)", baseClassMethodFieldName)
                        .addStatement("return $N", classToCollectField)
                        .endControlFlow()
                        .build();

        final var methodJavadoc =
                CodeBlock.builder()
                        .add(
                                "@return all objects of type {@link $T} from anywhere in base class"
                                        + " of type {@link $T}",
                                classToCollectName,
                                baseClassName)
                        .build();

        final var methodSpecBuilder =
                MethodSpec.methodBuilder("apply")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(baseClassName, baseClassMethodFieldName, Modifier.FINAL)
                        .returns(listOfClassToCollects)
                        .addJavadoc(methodJavadoc)
                        .addCode(initialize)
                        .addCode(ifPojoNullReturn);

        for (var codeBlock : codeBlocks) {
            methodSpecBuilder.addCode(codeBlock);
        }

        final var methodSpec =
                methodSpecBuilder.addStatement("return $N", classToCollectField).build();

        final var classJavadoc =
                CodeBlock.builder()
                        .add(
                                "This class will collect all nested objects of type {@link $T} in"
                                        + " object of type {@link $T}",
                                classToCollectName,
                                baseClassName)
                        .add(System.lineSeparator())
                        .add(System.lineSeparator())
                        .add("BaseClass: {@link $T}", baseClassName)
                        .add(System.lineSeparator())
                        .add("ClassToCollect: {@link $T}", classToCollectName)
                        .build();

        return TypeSpec.classBuilder(classToCollect.getSimpleName() + "Collector")
                .addAnnotation(generatedAnnotation)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodSpec)
                .addSuperinterface(collectorInterface)
                .addJavadoc(classJavadoc)
                .build();
    }

    /**
     * Used to create a collector container class
     *
     * @param collectorsPackageName name of the package where the collector classes are generated in
     * @param baseClass where to collect the nested objects of interested from
     * @param collectors the specification of the collector classes
     * @param annotationMap map which contains annotations which should be used in the generated classes
     * @return class specification for the {@link CollectorContext}
     */
    public TypeSpec createCollectorContext(
            final String collectorsPackageName,
            final Class<?> baseClass,
            final List<TypeSpec> collectors,
            final Map<AnnotationType, AnnotationSpec> annotationMap) {

        final var generatedAnnotation = annotationMap.get(AnnotationType.GENERATED);
        final var nullableAnnotation = annotationMap.get(AnnotationType.NULLABLE);

        final CodeBlock.Builder collectiblesListInitializer = CodeBlock.builder().add("List.of(");
        final CodeBlock.Builder collectorMapInitializer = CodeBlock.builder().add("Map.of(");

        for (Iterator<TypeSpec> iterator = new LinkedHashSet<>(collectors).iterator();
                iterator.hasNext(); ) {
            final var collector = iterator.next();
            final var collectorName = ClassName.get(collectorsPackageName, collector.name);
            final var classToCollectListType =
                    ((ParameterizedTypeName) collector.superinterfaces.getFirst())
                            .typeArguments.get(1);
            final var classToCollect =
                    ((ParameterizedTypeName) classToCollectListType).typeArguments.getFirst();

            collectiblesListInitializer.add("$T.class", classToCollect);
            collectorMapInitializer.add("$T.class, new $T()", classToCollect, collectorName);

            if (iterator.hasNext()) {
                collectiblesListInitializer.add(", ");
                collectorMapInitializer.add(", ");
            }
        }
        collectiblesListInitializer.add(")");
        collectorMapInitializer.add(")");

        final var rootClassType = TypeVariableName.get(baseClass);
        final var collectorContainerInterface =
                ParameterizedTypeName.get(CollectorContext.class, baseClass);

        final var genericCollector =
                ParameterizedTypeName.get(
                        ClassName.get(Function.class),
                        rootClassType,
                        WildcardTypeName.subtypeOf(Object.class));

        final var genericWildcardClass =
                ParameterizedTypeName.get(
                        ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class));

        final var listOfGenericWildcardClass =
                ParameterizedTypeName.get(JAVA_UTIL_LIST, genericWildcardClass);
        final var collectiblesFieldName = "collectibles";

        final var listOfGenericWildcardClassField =
                FieldSpec.builder(
                                listOfGenericWildcardClass,
                                collectiblesFieldName,
                                Modifier.PRIVATE,
                                Modifier.FINAL)
                        .initializer(collectiblesListInitializer.build())
                        .build();

        final var mapOfCollectors =
                ParameterizedTypeName.get(JAVA_UTIL_MAP, genericWildcardClass, genericCollector);
        final var collectorsFieldName = "collectorMap";
        final var mapOfCollectorsField =
                FieldSpec.builder(
                                mapOfCollectors,
                                collectorsFieldName,
                                Modifier.PRIVATE,
                                Modifier.FINAL)
                        .initializer(collectorMapInitializer.build())
                        .build();

        final var genericTypeForCollectorClass = TypeVariableName.get("C");
        final var genericClass =
                ParameterizedTypeName.get(ClassName.get(Class.class), genericTypeForCollectorClass);
        final var collectorReturnType =
                ParameterizedTypeName.get(
                        ClassName.get(Function.class),
                        rootClassType,
                        ParameterizedTypeName.get(JAVA_UTIL_LIST, genericTypeForCollectorClass));

        final var getBaseClassMethodJavadoc =
                CodeBlock.builder().add("@return the base class").build();
        final var getBaseClassMethod =
                MethodSpec.methodBuilder("getBaseClass")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(ParameterizedTypeName.get(Class.class, baseClass))
                        .addStatement("return $T.class", baseClass)
                        .addJavadoc(getBaseClassMethodJavadoc);

        final var getAvailableCollectiblesMethodJavadoc =
                CodeBlock.builder()
                        .add("@return list of classes which can be collected from base class")
                        .build();
        final var getAvailableCollectiblesMethod =
                MethodSpec.methodBuilder("get")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(listOfGenericWildcardClass)
                        .addStatement("return $N", collectiblesFieldName)
                        .addJavadoc(getAvailableCollectiblesMethodJavadoc);

        final var getMethodParameterName = "clazz";
        final var getCollectorMethodJavadoc =
                CodeBlock.builder()
                        .add(
                                "@param $N to get the specific collector {@link $T} for",
                                getMethodParameterName,
                                Function.class)
                        .build();
        final var getCollectorMethod =
                MethodSpec.methodBuilder("get")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addAnnotation(nullableAnnotation)
                        .addAnnotation(
                                AnnotationSpec.builder(SuppressWarnings.class)
                                        .addMember("value", "$S", "unchecked")
                                        .build())
                        .addTypeVariable(genericTypeForCollectorClass)
                        .returns(collectorReturnType)
                        .addParameter(genericClass, getMethodParameterName, Modifier.FINAL)
                        .addCode(
                                "return ($T<$T, List<C>>) $N.get($N);",
                                Function.class,
                                baseClass,
                                collectorsFieldName,
                                getMethodParameterName)
                        .addJavadoc(getCollectorMethodJavadoc);

        final var classJavadoc =
                CodeBlock.builder()
                        .add(
                                "This class contains all collector instances {@link $T} and can be"
                                    + " used to get a specific collector for a specific class to"
                                    + " collect!",
                                Function.class)
                        .build();

        return TypeSpec.classBuilder("CollectorContextImpl")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(generatedAnnotation)
                .addField(listOfGenericWildcardClassField)
                .addField(mapOfCollectorsField)
                .addMethod(getBaseClassMethod.build())
                .addMethod(getAvailableCollectiblesMethod.build())
                .addMethod(getCollectorMethod.build())
                .addSuperinterface(collectorContainerInterface)
                .addJavadoc(classJavadoc)
                .build();
    }
}
