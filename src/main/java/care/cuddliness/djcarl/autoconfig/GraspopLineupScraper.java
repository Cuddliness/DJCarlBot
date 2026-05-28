package care.cuddliness.djcarl.autoconfig;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GraspopLineupScraper {

    private static final Logger log = LoggerFactory.getLogger(GraspopLineupScraper.class);

    private static final String BASE_URL = "https://www.graspop.be/nl/line-up";

    /** Display name → Dutch URL slug, in day order */
    private static final Map<String, String> DAYS = new LinkedHashMap<>();
    static {
        DAYS.put("Thursday", "donderdag");
        DAYS.put("Friday",   "vrijdag");
        DAYS.put("Saturday", "zaterdag");
        DAYS.put("Sunday",   "zondag");
    }

    private static final int TIMEOUT_MS       = 15_000;
    private static final int POLITE_DELAY_MS  = 1_500;
    private static final String USER_AGENT    = "Mozilla/5.0 (compatible; GraspopDiscordBot/1.0)";

    /**
     * Matches the time suffix at the end of an act text:
     *   "Ego Kill Talent 12.45 - 13.30"
     *    ^^^^^^^^^^^^^^^^ ^^^^^   ^^^^^
     *    group 1          group 2 group 3
     */
    private static final Pattern ACT_PATTERN =
            Pattern.compile("^(.+?)\\s+(\\d{1,2}\\.\\d{2})\\s*-\\s*(\\d{1,2}\\.\\d{2})$");

    /** Configured via application.properties: graspop.lineup.output-path */
    @Value("${graspop.lineup.output-path:lineup.json}")
    private String outputPath;

    private final ObjectMapper objectMapper;

    public GraspopLineupScraper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // -------------------------------------------------------------------------
    // Startup hook
    // -------------------------------------------------------------------------

    @PostConstruct
    public void startup (){
        log.info("Graspop 2026 — starting lineup scrape...");
        try {
            Map<String, Object> lineup = scrapeFullLineup();
            saveToJson(lineup);
            log.info("Lineup saved to '{}'", outputPath);
        } catch (Exception e) {
            log.error("Lineup scrape failed", e);
        }
    }

    // -------------------------------------------------------------------------
    // Public API (also useful for manual refresh or testing)
    // -------------------------------------------------------------------------

    /**
     * Scrapes all four days and returns the full lineup as a nested map:
     *   day → stage → list of acts
     */
    public Map<String, Object> scrapeFullLineup() throws IOException {
        Map<String, Object> fullLineup = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : DAYS.entrySet()) {
            String dayName = entry.getKey();
            String url     = BASE_URL + "/" + entry.getValue() + "/schedule";

            log.info("  Fetching {} → {}", dayName, url);
            try {
                Map<String, List<Map<String, String>>> daySchedule = parseDayPage(url);
                fullLineup.put(dayName, daySchedule);
                log.info("  {} — {} stage(s) found", dayName, daySchedule.size());
            } catch (IOException e) {
                log.error("  Could not scrape {}: {}", dayName, e.getMessage());
                fullLineup.put(dayName, new LinkedHashMap<>());
            }

            sleep(POLITE_DELAY_MS);
        }

        return fullLineup;
    }

    // -------------------------------------------------------------------------
    // Page parsing
    // -------------------------------------------------------------------------

    /**
     * Parses one schedule page into a map of stage → acts.
     *
     * The page contains a sequence of:
     *   <h2>Stage Name</h2>
     *   <ul>
     *     <li><a href="...">Artist HH.MM - HH.MM</a></li>
     *     ...
     *   </ul>
     *
     * We iterate every <h2>, then walk its next sibling looking for the <ul>.
     */
    private Map<String, List<Map<String, String>>> parseDayPage(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT_MS)
                .get();

        Map<String, List<Map<String, String>>> stageMap = new LinkedHashMap<>();

        Elements stageHeaders = doc.select("h2");

        for (Element h2 : stageHeaders) {
            String stageName = h2.text().trim();
            if (stageName.isBlank()) continue;

            // The act list is the <ul> immediately after the <h2>
            Element sibling = h2.nextElementSibling();
            if (sibling == null || !sibling.tagName().equals("ul")) continue;

            List<Map<String, String>> acts = parseActList(sibling);
            if (!acts.isEmpty()) {
                stageMap.put(stageName, acts);
            }
        }

        return stageMap;
    }

    /**
     * Turns a <ul> of act <li> elements into a list of act maps.
     */
    private List<Map<String, String>> parseActList(Element ul) {
        List<Map<String, String>> acts = new ArrayList<>();

        for (Element li : ul.select("li")) {
            String rawText = li.text().trim();
            if (rawText.isBlank()) continue;

            Map<String, String> act = parseActText(rawText);
            if (act != null) {
                acts.add(act);
            } else {
                log.warn("    Skipping unparseable act: '{}'", rawText);
            }
        }

        return acts;
    }

    /**
     * Parses a raw act string such as "Ego Kill Talent 12.45 - 13.30"
     * into {"artist": "Ego Kill Talent", "start": "12:45", "end": "13:30"}.
     *
     * Returns null if the string doesn't match the expected pattern.
     */
    private Map<String, String> parseActText(String raw) {
        Matcher m = ACT_PATTERN.matcher(raw);
        if (!m.matches()) return null;

        Map<String, String> act = new LinkedHashMap<>();
        act.put("artist", m.group(1).trim());
        act.put("start",  normalizeTime(m.group(2)));   // "12.45" → "12:45", "0.00" → "00:00"
        act.put("end",    normalizeTime(m.group(3)));  // "13.30" → "13:30"
        return act;
    }

    // -------------------------------------------------------------------------
    // File I/O
    // -------------------------------------------------------------------------

    private void saveToJson(Map<String, Object> lineup) throws IOException {
        File outputFile = new File(outputPath);
        File parentDir  = outputFile.getParentFile();
        if (parentDir != null) parentDir.mkdirs();

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, lineup);
    }

    private String normalizeTime(String raw) {
        String[] parts = raw.split("\\.");
        int hour   = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return String.format("%02d:%02d", hour, minute);
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
