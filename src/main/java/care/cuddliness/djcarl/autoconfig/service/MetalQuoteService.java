package care.cuddliness.djcarl.autoconfig.service;
import care.cuddliness.djcarl.metalquote.MetalQuote;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
@Service
public class MetalQuoteService {

    private static final Logger log = LoggerFactory.getLogger(MetalQuoteService.class);
    private static final String QUOTES_FILE = "metal-quotes.json";

    private final ObjectMapper objectMapper;

    // In-memory store — loaded once at startup
    private List<MetalQuote> quotes = new ArrayList<>();

    public MetalQuoteService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------

    /**
     * Loads all quotes from the classpath JSON file on application startup.
     * Fails fast with a clear error message if the file is missing or malformed.
     */
    @PostConstruct
    public void loadQuotes() {
        File file = new File(QUOTES_FILE);
        log.info("Loading metal quotes from: {}", file.getAbsolutePath());

        try {
            JsonNode root = objectMapper.readTree(file);
            JsonNode quotesNode = root.path("metalQuotes");

            List<MetalQuote> loaded = new ArrayList<>();
            for (JsonNode node : quotesNode) {
                MetalQuote quote = new MetalQuote(
                        node.path("id").asInt(),
                        node.path("quote").asText(),
                        node.path("author").asText(),
                        node.path("band").isNull() ? null : node.path("band").asText(),
                        parseTags(node.path("tags"))
                );
                loaded.add(quote);
            }

            this.quotes = Collections.unmodifiableList(loaded);
            log.info("MetalQuoteService: loaded {} quotes from {}", quotes.size(), file.getAbsolutePath());

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to load metal quotes from: " + file.getAbsolutePath() +
                            "\nMake sure metal-quotes.json is in the folder where you run the application.", e);
        }
    }

    private List<String> parseTags(JsonNode tagsNode) {
        List<String> tags = new ArrayList<>();
        if (tagsNode.isArray()) {
            tagsNode.forEach(t -> tags.add(t.asText()));
        }
        return Collections.unmodifiableList(tags);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Returns every quote in the collection.
     */
    public List<MetalQuote> getAllQuotes() {
        return quotes;
    }

    /**
     * Finds a quote by its numeric ID.
     *
     * @param id the quote's unique identifier
     * @return an Optional containing the quote, or empty if not found
     */
    public Optional<MetalQuote> getById(int id) {
        return quotes.stream()
                .filter(q -> q.id() == id)
                .findFirst();
    }

    /**
     * Returns all quotes attributed to the given author (case-insensitive).
     *
     * @param author author name to search for
     * @return list of matching quotes, possibly empty
     */
    public List<MetalQuote> getByAuthor(String author) {
        String normalized = author.trim().toLowerCase();
        return quotes.stream()
                .filter(q -> q.author() != null && q.author().toLowerCase().contains(normalized))
                .collect(Collectors.toList());
    }

    /**
     * Returns all quotes associated with the given band name (case-insensitive).
     *
     * @param band band name to search for
     * @return list of matching quotes, possibly empty
     */
    public List<MetalQuote> getByBand(String band) {
        String normalized = band.trim().toLowerCase();
        return quotes.stream()
                .filter(q -> q.band() != null && q.band().toLowerCase().contains(normalized))
                .collect(Collectors.toList());
    }

    /**
     * Returns all quotes that contain the given tag (case-insensitive).
     *
     * @param tag tag to search for, e.g. "humor", "freedom"
     * @return list of matching quotes, possibly empty
     */
    public List<MetalQuote> getByTag(String tag) {
        String normalized = tag.trim().toLowerCase();
        return quotes.stream()
                .filter(q -> q.tags().stream().anyMatch(t -> t.toLowerCase().contains(normalized)))
                .collect(Collectors.toList());
    }

    /**
     * Performs a simple full-text search across quote text, author, and band.
     *
     * @param keyword the search keyword (case-insensitive)
     * @return list of matching quotes, possibly empty
     */
    public List<MetalQuote> search(String keyword) {
        String normalized = keyword.trim().toLowerCase();
        return quotes.stream()
                .filter(q ->
                        q.quote().toLowerCase().contains(normalized) ||
                                (q.author() != null && q.author().toLowerCase().contains(normalized)) ||
                                (q.band() != null && q.band().toLowerCase().contains(normalized))
                )
                .collect(Collectors.toList());
    }

    /**
     * Returns a single random quote from the collection.
     *
     * @return a randomly selected MetalQuote
     * @throws IllegalStateException if the quote list is empty
     */
    public MetalQuote getRandomQuote() {
        if (quotes.isEmpty()) {
            throw new IllegalStateException("No quotes loaded — cannot return a random quote.");
        }
        int index = ThreadLocalRandom.current().nextInt(quotes.size());
        return quotes.get(index);
    }

    /**
     * Returns the total number of loaded quotes.
     */
    public int count() {
        return quotes.size();
    }
}
