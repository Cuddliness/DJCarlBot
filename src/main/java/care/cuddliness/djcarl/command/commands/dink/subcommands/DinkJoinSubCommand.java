package care.cuddliness.djcarl.command.commands.dink.subcommands;

import care.cuddliness.djcarl.autoconfig.service.CooldownService;
import care.cuddliness.djcarl.autoconfig.service.DinkService;
import care.cuddliness.djcarl.command.annotation.BaseSubCommandComponent;
import care.cuddliness.djcarl.command.commands.dink.DinkMainCommand;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@BaseSubCommandComponent(subCommandId = "join", parent = DinkMainCommand.class)
public class DinkJoinSubCommand implements BaseSubCommandInterface {
    private final DinkService dinkService;

    public DinkJoinSubCommand(DinkService dinkService) {
        this.dinkService = dinkService;
    }
    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        if(dinkService.isInDink(sender.getUser())){
            event.reply("You already joined the dink pool").setEphemeral(true).queue();
        }else{
            dinkService.addToDink(event.getUser());
            event.reply(sender.getUser().getEffectiveName() + " Joined the dink pool! :beers:").queue();
        }
    }
}
