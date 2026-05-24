package care.cuddliness.djcarl.interaction.button;

import care.cuddliness.djcarl.interaction.button.data.ButtonExecutorInterface;
import org.jetbrains.annotations.NotNull;

public record BaseButton(@NotNull ButtonExecutorInterface buttonExecutorInterface, @NotNull String name) {


}
