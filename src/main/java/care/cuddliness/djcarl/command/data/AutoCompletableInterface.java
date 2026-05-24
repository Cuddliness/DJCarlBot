package care.cuddliness.djcarl.command.data;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;

public interface AutoCompletableInterface {

    void onAutoComplete(CommandAutoCompleteInteractionEvent event);

}
