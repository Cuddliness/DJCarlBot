package care.cuddliness.djcarl.command.commands.dink.subcommands;

import care.cuddliness.djcarl.autoconfig.service.DinkService;
import care.cuddliness.djcarl.command.annotation.BaseSubCommandComponent;
import care.cuddliness.djcarl.command.commands.dink.DinkMainCommand;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@BaseSubCommandComponent(subCommandId = "leave", description = "Leave the dink pool", parent = DinkMainCommand.class)

public class DinkLeaveSubCommand implements BaseSubCommandInterface {
    private final DinkService dinkService;

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        if(!dinkService.removeFromDink(event.getUser())){
            event.reply("Your not in a dink pool, if you want to join use: /dink join").setEphemeral(true).queue();
        }else{
            dinkService.removeFromDink(event.getUser());
            event.reply(event.getUser().getEffectiveName() + " Left the dink pool! :wave:").queue();
        }
    }
}
