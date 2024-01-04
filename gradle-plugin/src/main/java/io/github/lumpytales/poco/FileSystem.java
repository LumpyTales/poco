package io.github.lumpytales.poco;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystem {

    public boolean exists(final Path path) {
        return Files.exists(path);
    }

    public Path writeString(final Path path, final String content) throws IOException {
        return Files.writeString(path, content);
    }
}
