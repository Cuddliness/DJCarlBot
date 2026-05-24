package care.cuddliness.djcarl.command;

import care.cuddliness.djcarl.command.annotation.BaseCommandOption;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;

import java.util.List;

public record BaseSubCommand(BaseSubCommandInterface command, String subCommandId, String SubCommandGroup,
                             List<BaseCommandOption> options) {

}
