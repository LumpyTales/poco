package io.github.lumpytales.poco.file;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import io.github.lumpytales.poco.FileData;
import java.io.IOException;
import java.io.StringWriter;

/**
 * simply converts a {@link com.squareup.javapoet.TypeSpec} to {@link io.github.lumpytales.poco.FileData}
 */
public class TypeSpecConverter {

    /**
     * Convert the class specification to a representation which can be used to write it to the file system
     * @param outputPackageName name of the package where the classes are generated in
     * @param specification of the class to convert
     * @return the file data
     * @throws IOException in case the file content couldn't be written to {@link FileData}
     */
    public FileData convert(final String outputPackageName, final TypeSpec specification)
            throws IOException {

        try (var output = new StringWriter()) {

            final var javaClass = JavaFile.builder(outputPackageName, specification).build();
            javaClass.writeTo(output);

            final var code = output.toString();
            return FileData.builder().content(code).name(specification.name + ".java").build();
        }
    }
}
