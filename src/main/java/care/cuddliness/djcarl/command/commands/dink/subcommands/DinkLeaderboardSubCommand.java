package care.cuddliness.djcarl.command.commands.dink.subcommands;

import care.cuddliness.djcarl.autoconfig.service.DinkService;
import care.cuddliness.djcarl.command.annotation.BaseSubCommandComponent;
import care.cuddliness.djcarl.command.commands.dink.DinkMainCommand;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;
import care.cuddliness.djcarl.database.entity.CarlUser;
import care.cuddliness.djcarl.database.repository.CarlUserRepository;
import care.cuddliness.djcarl.utils.EmbedColor;
import care.cuddliness.djcarl.utils.EmbedUtil;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@BaseSubCommandComponent(subCommandId = "leaderboard", description = "Get the leaderboard of dink users", parent = DinkMainCommand.class)
public class DinkLeaderboardSubCommand implements BaseSubCommandInterface {
    private final CarlUserRepository carlUserRepository;

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        List<CarlUser> carlUserList = carlUserRepository.dinkLeaderboard();

        EmbedUtil embedUtil = new EmbedUtil();
        embedUtil.setTitle("Top 5 Dink's leaderboard");
        embedUtil.setColor(EmbedColor.PRIMARY);
        StringBuilder b = new StringBuilder();
        int number = 1;
        for(CarlUser cu : carlUserList){
            b.append("***").append(number).append(".*** ").append(event.getJDA().getUserById(cu.getDiscordId()).getAsMention()).append("* ").append("(")
                    .append(cu.getDinks()).append(" Dink's").append(")*\n");
            number ++;
        }
        embedUtil.setDescription(b.toString());
        embedUtil.setThumbnail("https://cdn-icons-gif.flaticon.com/14204/14204947.gif");
        event.replyEmbeds(embedUtil.build()).queue();

    }
}
