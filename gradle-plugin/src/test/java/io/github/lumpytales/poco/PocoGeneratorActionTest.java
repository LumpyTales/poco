package io.github.lumpytales.poco;

import io.github.lumpytales.poco.testclasses.Person;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.assertj.core.api.Assertions;
import org.gradle.api.file.Directory;
import org.gradle.api.file.RegularFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * used to test {@link PocoGeneratorAction}
 */
class PocoGeneratorActionTest {

    private Directory dir;
    private FileSystem fs;
    private PocoGeneratorAction cut;

    @BeforeEach
    void setup() {

        dir = Mockito.mock(Directory.class);
        Mockito.when(dir.getAsFile()).thenReturn(Mockito.mock(File.class));
        Mockito.when(dir.dir(Mockito.anyString())).thenReturn(dir);

        fs = Mockito.mock(FileSystem.class);
        cut = new PocoGeneratorAction(fs);
    }

    @Test
    void When_output_folder_cannot_be_created_Then_expect_exception() {
        // given
        final var baseClass = FileSystem.class;
        final var outputPackageName = "de.test";

        // when
        // then
        Assertions.assertThatThrownBy(
                        () -> cut.generate(baseClass, outputPackageName, null, null, dir))
                .isInstanceOf(IOException.class);
    }

    @Test
    void When_generation_is_triggered_Then_expect_files_are_written_to_directory()
            throws IOException {
        // given
        final var baseClass = Person.class;
        final var outputPackageName = "de.test";

        Mockito.when(fs.exists(Mockito.any())).thenReturn(true);
        final var regularFile = Mockito.mock(RegularFile.class);
        Mockito.when(dir.file(Mockito.anyString())).thenReturn(regularFile);
        final var file = Mockito.mock(File.class);
        Mockito.when(regularFile.getAsFile()).thenReturn(file);
        final var path = Mockito.mock(Path.class);
        Mockito.when(file.toPath()).thenReturn(path);

        // when
        cut.generate(baseClass, outputPackageName, null, null, dir);

        // then
        Mockito.verify(fs, Mockito.times(2)).writeString(Mockito.any(), Mockito.anyString());
    }
}
