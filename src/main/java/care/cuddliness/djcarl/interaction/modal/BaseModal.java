package care.cuddliness.djcarl.interaction.modal;

import care.cuddliness.djcarl.interaction.modal.annotation.ModalInput;
import care.cuddliness.djcarl.interaction.modal.data.ModalExecutorInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record BaseModal(@NotNull ModalExecutorInterface modalExecutorInterface, List<ModalInput> options,
                        @NotNull String name) {

}
