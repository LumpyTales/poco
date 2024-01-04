package io.github.lumpytales.poco.testclasses;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * tag test class
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Tag {
    /** tag key */
    private final String key;

    /** tag value */
    private final String value;

    /** tag order */
    private Integer order;

    /** tag weight */
    private Long weight;
}
