package care.cuddliness.djcarl.command.data;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public interface BaseCommandExecutorInterface {

    void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event);
}
