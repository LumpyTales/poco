package io.github.lumpytales.poco.testclasses;

import lombok.Builder;
import lombok.Getter;

/**
 * person test class
 */
@Getter
@Builder
public class Person {

    /** surname of person */
    private String surname;

    /** name of person */
    private String name;

    /** contact of person */
    private Contact contact;
}
