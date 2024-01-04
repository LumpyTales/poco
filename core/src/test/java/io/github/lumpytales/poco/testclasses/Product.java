package io.github.lumpytales.poco.testclasses;

import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Product test class
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Product {
    /** product description */
    private final String description;

    /** product price */
    private final Price price;

    /** product tags */
    @Nullable private List<Tag> tags;

    /** product groupTags */
    @Nullable private Map<String, Tag> groupTags;

    /**
     * @param tags product tags
     */
    public void setTags(@Nullable final List<Tag> tags) {
        this.tags = tags != null ? Collections.unmodifiableList(tags) : Collections.emptyList();
    }

    /**
     * @param groupTags product groupTags
     */
    public void setGroupTags(@Nullable final Map<String, Tag> groupTags) {
        this.groupTags = groupTags != null ? Map.copyOf(groupTags) : Map.of();
    }
}
