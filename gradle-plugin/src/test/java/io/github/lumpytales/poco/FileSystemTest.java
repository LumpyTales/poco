package io.github.lumpytales.poco;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * used to test {@link FileSystem}
 */
class FileSystemTest {

    private final FileSystem fs = new FileSystem();

    @Test
    @SneakyThrows
    void When_filesystem_checks_output_folder_Then_expect_nio_is_involved() {
        // given
        final var path = Mockito.mock(Path.class);

        // when
        // then
        try (var files = Mockito.mockStatic(Files.class)) {
            fs.exists(path);
            files.verify(() -> Files.exists(Mockito.any(Path.class)), Mockito.times(1));
        }
    }

    @Test
    @SneakyThrows
    void When_filesystem_writes_content_Then_expect_nio_is_involved() {
        // given
        final var content = "content";
        final var path = Mockito.mock(Path.class);

        // when
        // then
        try (var files = Mockito.mockStatic(Files.class)) {

            fs.writeString(path, content);

            files.verify(
                    () -> {
                        try {
                            Files.writeString(Mockito.any(Path.class), Mockito.anyString());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    Mockito.times(1));
        }
    }
}
