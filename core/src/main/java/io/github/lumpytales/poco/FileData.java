package io.github.lumpytales.poco;

import lombok.Builder;
import lombok.Data;

/**
 * contains the name and the content of generated classes
 */
@Data
@Builder
public class FileData {

    /** name of the generated class */
    private final String name;

    /** content of the generated class */
    private final String content;
}
