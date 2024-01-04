package io.github.lumpytales.poco.testclasses;

import lombok.Builder;
import lombok.Getter;

/**
 * contact test class
 */
@Getter
@Builder
public class Contact {

    /** email of contact */
    private String email;

    /** mobile of contact */
    private String mobile;
}
