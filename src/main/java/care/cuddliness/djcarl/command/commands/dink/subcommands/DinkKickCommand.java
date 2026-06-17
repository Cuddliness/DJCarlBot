package care.cuddliness.djcarl.command.commands.dink.subcommands;

import care.cuddliness.djcarl.autoconfig.service.DinkService;
import care.cuddliness.djcarl.command.annotation.BaseCommandOption;
import care.cuddliness.djcarl.command.annotation.BaseSubCommandComponent;
import care.cuddliness.djcarl.command.commands.dink.DinkMainCommand;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@BaseCommandOption(name = "user", description = "the user you want to remove from time-out", type = OptionType.USER, required = true)
@BaseCommandOption(name = "reason", description = "Reason for the person kicking?", type = OptionType.STRING)


@BaseSubCommandComponent(subCommandId = "kick", description = "Remove a user from time-out", parent = DinkMainCommand.class)
public class DinkKickCommand implements BaseSubCommandInterface {
    private final DinkService dinkService;

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        if(!sender.hasPermission(Permission.MODERATE_MEMBERS)){
            event.reply(":x: You don't have permission to run this command").setEphemeral(true).queue();
        }
        User user = event.getOption("user").getAsUser();
        String reason = "Has been kicked for ";
        if(event.getOption("reason") != null){
            reason = reason + event.getOption("reason").getAsString();
        }else{
            reason = reason + "Inactivity";
        }
        if(!dinkService.isInDinkPool(user)){
            event.reply("This user is not in the dinkpool, therefor can't be kicked").queue();
        }else{
            dinkService.removeFromDink(user);
            event.reply(user.getAsMention() + " " + reason).queue();
        }


    }
}
