package io.github.lumpytales.poco.core.code.blocks;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class FieldNameFactory {

    private static final Pattern PATTERN_FIELD_NAME_COUNTER = Pattern.compile("\\d+$");

    public String create(
            final String parentObjectName,
            final String fieldName,
            final List<String> alreadyInstantiatedFieldNames) {
        // pascal case
        final String name =
                parentObjectName.toLowerCase().charAt(0)
                        + parentObjectName.substring(1)
                        + fieldName.toUpperCase().charAt(0)
                        + fieldName.substring(1);

        // check if the fieldName has already been used somehow
        final var instantiatedFieldName = parentObjectName + "->" + fieldName;
        if (alreadyInstantiatedFieldNames.contains(instantiatedFieldName)) {
            final var fieldNameCounter =
                    extractFieldNameCounter(name).map(counter -> counter + 1).orElse(1);
            final var fieldNameWithCounter =
                    fieldName.substring(
                                    0,
                                    fieldName.length() - fieldNameCounter.toString().length() + 1)
                            + fieldNameCounter;
            return create(parentObjectName, fieldNameWithCounter, alreadyInstantiatedFieldNames);
        }
        alreadyInstantiatedFieldNames.add(instantiatedFieldName);
        return name;
    }

    private Optional<Integer> extractFieldNameCounter(final String fieldName) {
        final var matcher = PATTERN_FIELD_NAME_COUNTER.matcher(fieldName);
        if (matcher.find()) {
            final var value = Integer.parseInt(matcher.group());
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }
}
