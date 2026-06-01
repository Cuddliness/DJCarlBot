package care.cuddliness.djcarl.command.commands.dink.subcommands;

import care.cuddliness.djcarl.autoconfig.service.CooldownService;
import care.cuddliness.djcarl.autoconfig.service.DinkService;
import care.cuddliness.djcarl.command.annotation.BaseCommandOption;
import care.cuddliness.djcarl.command.annotation.BaseSubCommandComponent;
import care.cuddliness.djcarl.command.commands.dink.DinkMainCommand;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@BaseCommandOption(name = "user", description = "the user you want to time-out", autoComplete = false, type = OptionType.USER, required = true)
@BaseCommandOption(name = "duration", description = "how long you want to put the person in time out", autoComplete = false, type = OptionType.INTEGER, required = true)

@BaseSubCommandComponent(subCommandId = "timeout", description = "Roll a random person in the drink pool", parent = DinkMainCommand.class)
public class DinkTimeoutSubCommand implements BaseSubCommandInterface {
    private final DinkService dinkService;
    private final CooldownService cooldownService;

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        User user = event.getOption("user").getAsUser();
        int duration = event.getOption("duration").getAsInt();
        if(!dinkService.isInDinkPool(user)){
            event.reply("This user is not in the dink pool, therefor can't be on timeout").setEphemeral(true).queue();
        }else if(dinkService.isOnTimeOut(user)){
            event.reply("This user is already in timeout").setEphemeral(true).queue();
        }else{
            dinkService.startTimeout(user, duration);
            event.reply("Howdy " + user.getAsMention() + ", you have been timed-out from the dink pool for: " + dinkService.remainingTime("dink_timeout_" + user.getName())).queue();
        }
    }
}
