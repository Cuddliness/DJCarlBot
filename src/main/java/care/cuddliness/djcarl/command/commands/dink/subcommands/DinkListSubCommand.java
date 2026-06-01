package care.cuddliness.djcarl.command.commands.dink.subcommands;

import care.cuddliness.djcarl.autoconfig.service.DinkService;
import care.cuddliness.djcarl.command.annotation.BaseSubCommandComponent;
import care.cuddliness.djcarl.command.commands.dink.DinkMainCommand;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;
import care.cuddliness.djcarl.utils.EmbedColor;
import care.cuddliness.djcarl.utils.EmbedUtil;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@BaseSubCommandComponent(subCommandId = "list", description = "List of all the people in the dink pool", parent = DinkMainCommand.class)
public class DinkListSubCommand implements BaseSubCommandInterface {
    private final DinkService dinkService;

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        if(dinkService.getAll().isEmpty()){
            event.reply("No users in the dink pool :(").queue();
            return;
        }
        EmbedUtil embedUtil = new EmbedUtil();
        embedUtil.setColor(EmbedColor.PRIMARY);
        embedUtil.setTitle("Dink pool members");
        StringBuilder list = new StringBuilder();
        dinkService.getAll().forEach(user -> {
            list.append(" - ").append("``").append(Objects.requireNonNull(event.getJDA().getUserById(user.getDiscordId()))
                    .getEffectiveName()).append("`` ").append((dinkService.isOnTimeOut(event.getJDA().getUserById(user.getDiscordId())) ? ":hourglass_flowing_sand:" : ":beers:")).append(" since ").append("<t:").
                    append(user.getCreatedAt().getEpochSecond()).append(":f>").append("\n");
        });
        embedUtil.setFooter(Emoji.fromUnicode("U+1F37B").getFormatted() + " = Active dink user | " + Emoji.fromUnicode("U+23F3").getFormatted() + " = user is in time-out");
        embedUtil.setDescription(list.toString());
        event.replyEmbeds(embedUtil.build()).queue();

    }
}
