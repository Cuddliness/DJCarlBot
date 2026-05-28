package care.cuddliness.djcarl.metalquote;

import java.util.List;

public record MetalQuote (
        int id,
        String quote,
        String author,
        String band,        // nullable — some quotes have no known band
        List<String> tags
) {
    /**
     * Compact constructor: defensive copies and basic validation.
     */
    public MetalQuote {
        if (quote == null || quote.isBlank()) {
            throw new IllegalArgumentException("Quote text must not be blank (id=" + id + ")");
        }
        tags = tags != null ? List.copyOf(tags) : List.of();
    }

    /**
     * Convenience display string for logging / REST responses.
     */
    @Override
    public String toString() {
        String attribution = (band != null)
                ? "%s (%s)".formatted(author, band)
                : author;
        return "\"%s\" — %s".formatted(quote, attribution);
    }
}
