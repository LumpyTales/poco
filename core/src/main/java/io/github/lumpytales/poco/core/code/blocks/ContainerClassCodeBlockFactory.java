package io.github.lumpytales.poco.core.code.blocks;

import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ContainerClassCodeBlockFactory {

    public CodeBlock create(
            final WrappedInBodyCodeBlock wrappedInBodyCodeBlock,
            final Class<?> wrappedIn,
            final String parentObjectName,
            final String getter,
            final Class<?> objectType,
            final String objectName) {

        final var codeBlockBuilder = CodeBlock.builder();
        if (wrappedIn.getTypeParameters().length == 1) {
            codeBlockBuilder
                    .beginControlFlow("if($N.$N() != null)", parentObjectName, getter)
                    .beginControlFlow(
                            "for($T $N : $N.$N())",
                            objectType,
                            objectName,
                            parentObjectName,
                            getter)
                    .build();
        } else if (wrappedIn.getTypeParameters().length == 2) {
            codeBlockBuilder
                    .beginControlFlow("if($N.$N() != null)", parentObjectName, getter)
                    .beginControlFlow(
                            "for($T $N : $N.$N().values())",
                            objectType,
                            objectName,
                            parentObjectName,
                            getter)
                    .build();
        } else {
            final var message =
                    String.format("Unsupported container class [%s]!", wrappedIn.getName());
            throw new UnsupportedClassException(message);
        }

        codeBlockBuilder.add(wrappedInBodyCodeBlock.get());

        codeBlockBuilder.endControlFlow();
        codeBlockBuilder.endControlFlow();

        return codeBlockBuilder.build();
    }
}
