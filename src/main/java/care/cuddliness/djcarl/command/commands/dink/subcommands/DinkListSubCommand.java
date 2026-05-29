package care.cuddliness.djcarl.command.commands.dink.subcommands;

import care.cuddliness.djcarl.autoconfig.service.DinkService;
import care.cuddliness.djcarl.command.annotation.BaseSubCommandComponent;
import care.cuddliness.djcarl.command.commands.dink.DinkMainCommand;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;
import care.cuddliness.djcarl.utils.EmbedColor;
import care.cuddliness.djcarl.utils.EmbedUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@BaseSubCommandComponent(subCommandId = "list", description = "List of all the people in the dink pool", parent = DinkMainCommand.class)
public class DinkListSubCommand implements BaseSubCommandInterface {
    private final DinkService dinkService;

    public DinkListSubCommand(DinkService dinkService) {
        this.dinkService = dinkService;
    }
    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        if(dinkService.getDinkusers().isEmpty()){
            event.reply("No users in the dink pool :(").queue();
            return;
        }
        EmbedUtil embedUtil = new EmbedUtil();
        embedUtil.setColor(EmbedColor.PRIMARY);
        embedUtil.setTitle("Dink pool members");
        StringBuilder list = new StringBuilder();
        dinkService.getDinkusers().forEach(user -> {
            list.append(" - ").append(user.getAsTag()).append("\n");
        });
        embedUtil.setDescription(list.toString());
        event.replyEmbeds(embedUtil.build()).queue();

    }
}
