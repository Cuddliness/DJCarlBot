package care.cuddliness.djcarl.command.commands.lineup;

import care.cuddliness.djcarl.autoconfig.service.GraspopScheduleService;
import care.cuddliness.djcarl.command.annotation.BaseCommandComponent;
import care.cuddliness.djcarl.command.annotation.BaseCommandOption;
import care.cuddliness.djcarl.command.data.AutoCompletableInterface;
import care.cuddliness.djcarl.command.data.BaseCommandInterface;
import care.cuddliness.djcarl.graspopmanager.GraspopPerformance;
import care.cuddliness.djcarl.utils.EmbedColor;
import care.cuddliness.djcarl.utils.EmbedUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@BaseCommandComponent(name = "lineup", description = "Check the line-up by stage and day")
@BaseCommandOption(name = "day", description = "Return back the message from given string", type = OptionType.STRING, autoComplete = true, required = false)
@BaseCommandOption(name = "stage", description = "Return back the message from given string", type = OptionType.STRING, autoComplete = true, required = false)

public class LineupCommand  implements BaseCommandInterface, AutoCompletableInterface {

    private final GraspopScheduleService graspopScheduleService;

    public LineupCommand(GraspopScheduleService graspopScheduleService) {
        this.graspopScheduleService = graspopScheduleService;
    }

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        String day = Objects.requireNonNull(event.getOption("day")).getAsString();
        String stage = Objects.requireNonNull(event.getOption("stage")).getAsString();

        if(!day.isEmpty() && !stage.isEmpty()){
            List<GraspopPerformance> performances = graspopScheduleService.getByDayAndStage(day, stage);
            EmbedUtil util = new EmbedUtil();
            util.setColor(EmbedColor.PRIMARY);
            util.setTitle("Lineup for " + stage + " on " + day);
            performances.forEach(p -> {
                util.addField(p.getArtist(), p.getStart() + " - " + p.getEnd(), false);
            });

            event.replyEmbeds(util.build()).queue();
        }
    }

    @Override
    public void onAutoComplete(CommandAutoCompleteInteractionEvent event) {
        String[] days = new String[]{"Thursday", "Friday", "Saturday", "Sunday"};
        if (event.getFocusedOption().getName().equals("day")) {
            List<Command.Choice> options = Stream.of(days)
                    .filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                    .map(word -> new Command.Choice(word, word))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }

        String[] stages = new String[]{"South Stage", "North Stage", "Marquee", "Jupiler Stage", "Metal Dome", "Classic Rock Café"};
        if (event.getFocusedOption().getName().equals("stage")) {
            List<Command.Choice> options = Stream.of(stages)
                    .filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                    .map(word -> new Command.Choice(word, word))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();

        }
    }
}
