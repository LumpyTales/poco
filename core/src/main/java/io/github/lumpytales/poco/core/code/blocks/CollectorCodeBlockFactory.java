package io.github.lumpytales.poco.core.code.blocks;

import com.squareup.javapoet.CodeBlock;
import io.github.lumpytales.poco.core.analysis.metadata.FieldMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * class will create the code blocks based on the collected path from the base class to the class to create the collector for
 */
@RequiredArgsConstructor
public class CollectorCodeBlockFactory {

    private static final String BASE_CLASS_OBJECT_NAME = "pojo";

    private final FieldNameFactory fieldNameFactory;

    private final ContainerClassCodeBlockFactory containerClassCodeBlockFactory;

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
    public List<CodeBlock> create(final List<List<FieldMetaData>> paths) {
        final var codeBlocks = new ArrayList<CodeBlock>();
        final var alreadyInstantiatedFieldNames = new ArrayList<String>();
        for (List<FieldMetaData> path : paths) {
            if (path == null || path.isEmpty()) {
                continue;
            }

            final var lastField = path.getLast();

            final var codeBlock = create(path.iterator(), lastField, alreadyInstantiatedFieldNames);

            codeBlocks.add(codeBlock);
        }

        return codeBlocks;
    }

    private CodeBlock create(
            final Iterator<FieldMetaData> iterator,
            final FieldMetaData lastField,
            final List<String> alreadyInstantiatedFieldNames) {

        final var codeBlockBuilder = CodeBlock.builder();

        final var builder =
                create(
                        codeBlockBuilder,
                        BASE_CLASS_OBJECT_NAME,
                        iterator,
                        lastField,
                        alreadyInstantiatedFieldNames);

        return builder.build();
    }

    CodeBlock.Builder create(
            final CodeBlock.Builder codeBlockBuilder,
            final String parentObjectName,
            final Iterator<FieldMetaData> iterator,
            final FieldMetaData lastField,
            final List<String> alreadyInstantiatedFieldNames) {

        final var classField = iterator.next();
        final var objectType = classField.getField();
        final var objectName =
                fieldNameFactory.create(
                        parentObjectName, classField.getFieldName(), alreadyInstantiatedFieldNames);
        final var getter = classField.getGetterName();
        final var wrappedIn = classField.getWrappedIn();

        if (wrappedIn != null) {

            final var test =
                    new WrappedInBodyCodeBlock(
                            this, iterator, objectName, lastField, alreadyInstantiatedFieldNames);

            final var codeBlock =
                    containerClassCodeBlockFactory.create(
                            test, wrappedIn, parentObjectName, getter, objectType, objectName);

            codeBlockBuilder.add(codeBlock);

        } else {
            codeBlockBuilder.addStatement(
                    "$T $N = $N.$N()", objectType, objectName, parentObjectName, getter);

            if (lastField == classField && !iterator.hasNext()) {

                final var codeBlock = addToResult(objectName);
                codeBlockBuilder.add(codeBlock);
            }
        }

        if (iterator.hasNext()) { // recursive call
            codeBlockBuilder.beginControlFlow("if($N != null)", objectName);

            create(
                    codeBlockBuilder,
                    objectName,
                    iterator,
                    lastField,
                    alreadyInstantiatedFieldNames);

            codeBlockBuilder.endControlFlow();
        }
        return codeBlockBuilder;
    }

    public CodeBlock addToResult(final String objectName) {
        return CodeBlock.builder()
                .beginControlFlow("if($N != null)", objectName)
                .add("result.add($N)", objectName)
                .add(";")
                .add(System.lineSeparator())
                .endControlFlow()
                .build();
    }

    public CodeBlock ifNullContinue(final String objectName) {
        return CodeBlock.builder()
                .beginControlFlow("if($N == null)", objectName)
                .add("continue")
                .add(";")
                .add(System.lineSeparator())
                .endControlFlow()
                .build();
    }
}
