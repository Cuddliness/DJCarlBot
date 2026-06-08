package care.cuddliness.djcarl.command.commands.dink.subcommands;

import care.cuddliness.djcarl.command.annotation.BaseCommandOption;
import care.cuddliness.djcarl.command.annotation.BaseSubCommandComponent;
import care.cuddliness.djcarl.command.commands.dink.DinkMainCommand;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

@BaseCommandOption(name = "user", description = "the user you want to remove from time-out", type = OptionType.USER, required = true)

@BaseSubCommandComponent(subCommandId = "rmtimeout", description = "Remove a user from time-out", parent = DinkMainCommand.class)
public class DinkRemoveTimeoutSubCommand implements BaseSubCommandInterface {
    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        if(!sender.hasPermission(Permission.MODERATE_MEMBERS)){
            event.reply(":x: You don't have permission to run this command").setEphemeral(true).queue();
            return;
        }
        event.reply("Command under construction").setEphemeral(true).queue();
    }
}
