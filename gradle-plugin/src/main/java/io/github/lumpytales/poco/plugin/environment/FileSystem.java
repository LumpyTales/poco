package io.github.lumpytales.poco.plugin.environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * class used as wrapper for file system interactions
 */
public class FileSystem {

    /**
     * check if path exists on file system
     * @param path to check for existence
     * @return {@code true} if exists
     */
    public boolean exists(final Path path) {
        return Files.exists(path);
    }

    /**
     * Writes the content to the specified file
     * @param path where file should be written
     * @param content to write into file
     * @throws IOException if file couldn't be written
     */
    public void writeString(final Path path, final String content) throws IOException {
        Files.writeString(path, content);
    }
}
