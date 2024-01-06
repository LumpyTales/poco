package io.github.lumpytales.poco.code;

import com.squareup.javapoet.CodeBlock;
import io.github.lumpytales.poco.analysis.metadata.FieldMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * class will create the code blocks based on the collected path from the base class to the class to create the collector for
 */
public class CollectorMethodBodyGenerator {

    private static final String BASE_CLASS_OBJECT_NAME = "pojo";

    private static final Pattern PATTERN_FIELD_NAME_COUNTER = Pattern.compile("\\d+$");

    /**
     * get the name of the variable of the base class object
     * @return the name of the base class used in the generated code blocks to get nested objects
     */
    public String getBaseClassObjectName() {
        return BASE_CLASS_OBJECT_NAME;
    }

    /**
     * generate code from given paths
     * @param paths used to generate the {@link CodeBlock}
     * @return generated {@link CodeBlock}
     */
    public List<CodeBlock> generate(final List<List<FieldMetaData>> paths) {
        final var codeBlocks = new ArrayList<CodeBlock>();
        final var alreadyInstantiatedFieldNames = new ArrayList<String>();
        for (List<FieldMetaData> path : paths) {
            if (path == null || path.isEmpty()) {
                continue;
            }

            final var codeBlockBuilder = CodeBlock.builder();

            final var lastField = path.getLast();
            addBlock(
                    codeBlockBuilder,
                    BASE_CLASS_OBJECT_NAME,
                    path.iterator(),
                    lastField,
                    alreadyInstantiatedFieldNames);

            final var codeBlock = codeBlockBuilder.build();
            codeBlocks.add(codeBlock);
        }

        return codeBlocks;
    }

    private static void addBlock(
            final CodeBlock.Builder codeBlockBuilder,
            final String parentObjectName,
            final Iterator<FieldMetaData> iterator,
            final FieldMetaData lastField,
            final List<String> alreadyInstantiatedFieldNames) {

        final var classField = iterator.next();
        final var objectType = classField.getField();
        final var objectName =
                createFieldName(
                        parentObjectName, classField.getFieldName(), alreadyInstantiatedFieldNames);
        final var getter = classField.getGetterName();
        final var wrappedIn = classField.getWrappedIn();

        if (wrappedIn != null && List.class.isAssignableFrom(wrappedIn)) {

            codeBlockBuilder.beginControlFlow("if($N.$N() != null)", parentObjectName, getter);
            codeBlockBuilder.beginControlFlow(
                    "for($T $N : $N.$N())", objectType, objectName, parentObjectName, getter);

            if (!iterator.hasNext()) {
                addToResult(codeBlockBuilder, objectName);
            } else {
                // recursive call
                ifNullContinue(codeBlockBuilder, objectName);
                addBlock(
                        codeBlockBuilder,
                        objectName,
                        iterator,
                        lastField,
                        alreadyInstantiatedFieldNames);
            }
            codeBlockBuilder.endControlFlow();
            codeBlockBuilder.endControlFlow();

        } else if (wrappedIn != null && Map.class.isAssignableFrom(wrappedIn)) {

            codeBlockBuilder.beginControlFlow("if($N.$N() != null)", parentObjectName, getter);
            codeBlockBuilder.beginControlFlow(
                    "for($T $N : $N.$N().values())",
                    objectType,
                    objectName,
                    parentObjectName,
                    getter);

            if (!iterator.hasNext()) {
                addToResult(codeBlockBuilder, objectName);
            } else {
                // recursive call
                ifNullContinue(codeBlockBuilder, objectName);
                addBlock(
                        codeBlockBuilder,
                        objectName,
                        iterator,
                        lastField,
                        alreadyInstantiatedFieldNames);
            }
            codeBlockBuilder.endControlFlow();
            codeBlockBuilder.endControlFlow();

        } else if (wrappedIn == null) {

            codeBlockBuilder.addStatement(
                    "$T $N = $N.$N()", objectType, objectName, parentObjectName, getter);

            if (lastField == classField && !iterator.hasNext()) {

                addToResult(codeBlockBuilder, objectName);
            }

        } else {

            addToResult(codeBlockBuilder, objectName);
        }

        if (iterator.hasNext()) { // recursive call
            codeBlockBuilder.beginControlFlow("if($N != null)", objectName);

            addBlock(
                    codeBlockBuilder,
                    objectName,
                    iterator,
                    lastField,
                    alreadyInstantiatedFieldNames);

            codeBlockBuilder.endControlFlow();
        }
    }

    private static String createFieldName(
            final String parentObjectName,
            final String fieldName,
            final List<String> alreadyInstantiatedFieldNames) {
        // pascal case
        final String name =
                parentObjectName.toLowerCase().charAt(0)
                        + parentObjectName.substring(1)
                        + fieldName.toUpperCase().charAt(0)
                        + fieldName.substring(1);

        // check if the fieldName has already been used somehow
        final var instantiatedFieldName = parentObjectName + "->" + fieldName;
        if (alreadyInstantiatedFieldNames.contains(instantiatedFieldName)) {
            final var fieldNameCounter =
                    extractFieldNameCounter(name).map(counter -> counter + 1).orElse(1);
            final var fieldNameWithCounter =
                    fieldName.substring(
                                    0,
                                    fieldName.length() - fieldNameCounter.toString().length() + 1)
                            + fieldNameCounter;
            return createFieldName(
                    parentObjectName, fieldNameWithCounter, alreadyInstantiatedFieldNames);
        }
        alreadyInstantiatedFieldNames.add(instantiatedFieldName);
        return name;
    }

    private static void addToResult(CodeBlock.Builder codeBlockBuilder, String objectName) {

        codeBlockBuilder
                .beginControlFlow("if($N != null)", objectName)
                .add("result.add($N)", objectName)
                .add(";")
                .add(System.lineSeparator())
                .endControlFlow();
    }

    private static void ifNullContinue(CodeBlock.Builder codeBlockBuilder, String objectName) {
        CodeBlock.Builder nullContinueBuilder =
                CodeBlock.builder()
                        .beginControlFlow("if($N == null)", objectName)
                        .add("continue")
                        .add(";")
                        .add(System.lineSeparator())
                        .endControlFlow();

        codeBlockBuilder.add(nullContinueBuilder.build());
    }

    public static Optional<Integer> extractFieldNameCounter(final String fieldName) {
        final var matcher = PATTERN_FIELD_NAME_COUNTER.matcher(fieldName);
        if (matcher.find()) {
            final var value = Integer.parseInt(matcher.group());
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }
}
