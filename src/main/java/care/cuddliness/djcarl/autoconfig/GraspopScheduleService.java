package care.cuddliness.djcarl.autoconfig;

import care.cuddliness.djcarl.graspopmanager.GraspopPerformance;
import care.cuddliness.djcarl.graspopmanager.GraspopPerformanceRaw;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GraspopScheduleService {


    private final ObjectMapper objectMapper;
    private final List<GraspopPerformance> performances = new ArrayList<>();

    public GraspopScheduleService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // ---------------------------
    // LOAD ON STARTUP
    // ---------------------------
    @PostConstruct
    public void loadSchedule() {
        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("lineup.json")) {

            if (is == null) {
                throw new RuntimeException("graspop-schedule.json not found");
            }

            TypeReference<Map<String, Map<String, List<GraspopPerformanceRaw>>>> type =
                    new TypeReference<>() {};

            Map<String, Map<String, List<GraspopPerformanceRaw>>> data =
                    objectMapper.readValue(is, type);

            parse(data);

            System.out.println("Loaded performances: " + performances.size());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load schedule", e);
        }
    }

    // ---------------------------
    // PARSE JSON (FIXED)
    // ---------------------------
    private void parse(Map<String, Map<String, List<GraspopPerformanceRaw>>> data) {

        for (var dayEntry : data.entrySet()) {

            String day = dayEntry.getKey();

            for (var stageEntry : dayEntry.getValue().entrySet()) {

                String stage = stageEntry.getKey();

                for (GraspopPerformanceRaw raw : stageEntry.getValue()) {

                    performances.add(new GraspopPerformance(
                            day,
                            stage,
                            raw.getArtist(),
                            LocalTime.parse(raw.getStart()),
                            LocalTime.parse(raw.getEnd())
                    ));
                }
            }
        }
    }

    // ---------------------------
    // BASIC QUERIES
    // ---------------------------

    public List<GraspopPerformance> getAll() {
        return performances;
    }

    public List<GraspopPerformance> getByDay(String day) {
        return performances.stream()
                .filter(p -> p.getDay().equalsIgnoreCase(day))
                .toList();
    }

    public List<GraspopPerformance> getByStage(String stage) {
        return performances.stream()
                .filter(p -> p.getStage().equalsIgnoreCase(stage))
                .toList();
    }

    public List<GraspopPerformance> getByDayAndStage(String day, String stage) {
        return performances.stream()
                .filter(p -> p.getDay().equalsIgnoreCase(day))
                .filter(p -> p.getStage().equalsIgnoreCase(stage))
                .toList();
    }

    // ---------------------------
    // LIVE QUERIES
    // ---------------------------

    public List<GraspopPerformance> nowPlaying(LocalTime now) {
        return performances.stream()
                .filter(p -> p.isPlaying(now))
                .toList();
    }

    public List<GraspopPerformance> startingSoon(LocalTime now, long minutes) {
        return performances.stream()
                .filter(p -> p.startsWithin(now, minutes))
                .toList();
    }
}
