package care.cuddliness.djcarl.command.commands.lineup;

import care.cuddliness.djcarl.autoconfig.service.GraspopScheduleService;
import care.cuddliness.djcarl.command.annotation.BaseCommandComponent;
import care.cuddliness.djcarl.command.data.BaseCommandInterface;
import care.cuddliness.djcarl.graspopmanager.GraspopPerformance;
import care.cuddliness.djcarl.utils.EmbedColor;
import care.cuddliness.djcarl.utils.EmbedUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@BaseCommandComponent(name = "nowplaying", description = "shows now playing on graspop's stages")
public class NowplayingCommand implements BaseCommandInterface {
    private final GraspopScheduleService graspopScheduleService;

    public NowplayingCommand(GraspopScheduleService graspopScheduleService) {
        this.graspopScheduleService = graspopScheduleService;
    }

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        List<GraspopPerformance> performance = graspopScheduleService.nowPlaying(LocalDateTime.now());
        if(performance.isEmpty()){
            event.reply("Your either to early, to late, or just drunk. Try again later").queue();
        }else{
            EmbedUtil embed = new EmbedUtil();
            embed.setTitle("Now playing");
            embed.setColor(EmbedColor.PRIMARY);
            for(GraspopPerformance p : performance){
                embed.addField(p.getStage() + " | " + progressBar(p.getStart(), p.getEnd(), LocalTime.now()), p.getArtist(), false);
            }
            event.replyEmbeds(embed.build()).queue();
        }
    }

    public String progressBar(LocalTime startTime, LocalTime endTime, LocalTime now) {
        if (now.isBefore(startTime) || now.isAfter(endTime)) {
            return null; // Not currently playing
        }

        long total   = Duration.between(startTime, endTime).toMinutes();
        long elapsed = Duration.between(startTime, now).toMinutes();

        int barWidth  = 20;
        int filled    = (int) ((elapsed * barWidth) / total);
        int remaining = barWidth - filled;

        String bar = "▶ " + "█".repeat(filled) + "░".repeat(remaining);
        String timestamp = formatTime(startTime) + " / " + formatTime(endTime);

        return bar + "  " + timestamp;
    }

    private String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
