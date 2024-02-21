package io.github.lumpytales.poco.core.code.blocks;

import com.squareup.javapoet.CodeBlock;
import java.util.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContainerClassCodeBlockFactory {

    private final Set<Class<?>> CLASSES_BEHAVES_LIKE_LISTS =
            Set.of(List.class, Set.class, Queue.class, Deque.class);
    private final Set<Class<?>> CLASSES_BEHAVES_LIKE_MAPS = Set.of(Map.class);

    public CodeBlock create(
            final WrappedInBodyCodeBlock wrappedInBodyCodeBlock,
            final Class<?> wrappedIn,
            final String parentObjectName,
            final String getter,
            final Class<?> objectType,
            final String objectName) {

        final var codeBlockBuilder = CodeBlock.builder();
        if (CLASSES_BEHAVES_LIKE_LISTS.stream()
                .anyMatch(classBehavesLike -> classBehavesLike.isAssignableFrom(wrappedIn))) {
            codeBlockBuilder
                    .beginControlFlow("if($N.$N() != null)", parentObjectName, getter)
                    .beginControlFlow(
                            "for($T $N : $N.$N())",
                            objectType,
                            objectName,
                            parentObjectName,
                            getter)
                    .build();
        } else if (CLASSES_BEHAVES_LIKE_MAPS.stream()
                .anyMatch(classBehavesLike -> classBehavesLike.isAssignableFrom(wrappedIn))) {
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
