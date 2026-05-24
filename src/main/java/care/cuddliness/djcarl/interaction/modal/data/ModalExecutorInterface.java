package care.cuddliness.djcarl.interaction.modal.data;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface ModalExecutorInterface {

    void onExecute(Member sender, ModalInteractionEvent event);
}
