package io.github.lumpytales.poco.core.code.blocks;

import com.squareup.javapoet.CodeBlock;
import io.github.lumpytales.poco.core.analysis.metadata.FieldMetaData;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WrappedInBodyCodeBlock implements Supplier<CodeBlock> {

    private final CollectorCodeBlockFactory collectorCodeBlockFactory;
    private final Iterator<FieldMetaData> iterator;
    private final String objectName;
    private final FieldMetaData lastField;
    private final List<String> alreadyInstantiatedFieldNames;

    @Override
    public CodeBlock get() {
        final var codeBlockBuilder = CodeBlock.builder();

        if (!iterator.hasNext()) {
            final var resultCodeBlock = collectorCodeBlockFactory.addToResult(objectName);
            codeBlockBuilder.add(resultCodeBlock);
        } else {
            // recursive call
            final var ifNullContinueCodeBlock =
                    collectorCodeBlockFactory.ifNullContinue(objectName);
            codeBlockBuilder.add(ifNullContinueCodeBlock);

            collectorCodeBlockFactory.create(
                    codeBlockBuilder,
                    objectName,
                    iterator,
                    lastField,
                    alreadyInstantiatedFieldNames);
        }

        return codeBlockBuilder.build();
    }
}
