package care.cuddliness.djcarl.graspopmanager;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class GraspopPerformance {
    private final String day;
    private final String stage;
    private final String artist;
    private final LocalTime start;
    private final LocalTime end;

    public GraspopPerformance(String day, String stage, String artist,
                       LocalTime start, LocalTime end) {
        this.day = day;
        this.stage = stage;
        this.artist = artist;
        this.start = start;
        this.end = end;
    }

    public String getDay() { return day; }
    public String getStage() { return stage; }
    public String getArtist() { return artist; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }

    public boolean isPlaying(LocalTime now) {
        return !now.isBefore(start) && now.isBefore(end);
    }

    public boolean startsWithin(LocalTime now, long minutes) {
        return !start.isBefore(now) &&
                !start.isAfter(now.plusMinutes(minutes));
    }
}
