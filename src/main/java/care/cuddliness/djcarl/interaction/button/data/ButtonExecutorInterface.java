package care.cuddliness.djcarl.interaction.button.data;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonExecutorInterface {

    void onExecute(Member clicker, ButtonInteractionEvent event);
}
