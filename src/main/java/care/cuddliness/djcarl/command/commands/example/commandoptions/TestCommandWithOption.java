package care.cuddliness.djcarl.command.commands.example.commandoptions;

import care.cuddliness.djcarl.command.annotation.BaseCommandComponent;
import care.cuddliness.djcarl.command.annotation.BaseCommandOption;
import care.cuddliness.djcarl.command.data.BaseCommandInterface;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

@BaseCommandComponent(name = "echo")
@BaseCommandOption(name = "return", description = "Return back the message from given string", type = OptionType.STRING, autoComplete = false)
public class TestCommandWithOption implements BaseCommandInterface {
    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        //Catch the option value the user puts in
        String option = event.getOption("return").getAsString();
        if (!option.isBlank()) {
            //Return the option value and reply it to the event.
            event.reply(option).queue();
        }
    }
}
