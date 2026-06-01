package care.cuddliness.djcarl.command;

import care.cuddliness.djcarl.command.annotation.BaseCommandOption;
import care.cuddliness.djcarl.command.data.BaseCommandInterface;
import net.dv8tion.jda.api.interactions.commands.build.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public record BaseCommand(@NotNull BaseCommandInterface command, String description, String group, Map<String, BaseSubCommand> subCommands,
                          List<BaseCommandOption> options, String name) {

    public static @NotNull Map<String, BaseSubCommand> computeSubCommands(@NotNull Collection<BaseSubCommand> subCommands) {
        Map<String, BaseSubCommand> map = new HashMap<>();

        for (BaseSubCommand subCommand : subCommands) {
            if (!subCommand.subCommandId().isEmpty())
                map.put(subCommand.SubCommandGroup() + "/" + subCommand.subCommandId(), subCommand);
            else
                map.put(subCommand.subCommandId(), subCommand);
        }

        return map;
    }

    public CommandData createCommandData() {
        Map<String, BaseSubCommand> map = new HashMap<>();
        List<SubcommandData> cmddata = new ArrayList<>();
        List<SubcommandGroupData> subCommandGroups = new ArrayList<>();
        SlashCommandData commandData = Commands.slash(name, description);

        this.options.forEach(option ->
                commandData.addOption(
                        option.type(),
                        option.name(),
                        option.description(),
                        option.required(),
                        option.autoComplete()
                )
        );

        for (BaseSubCommand subCommand : subCommands.values()) {
            if (subCommandGroups.stream().noneMatch(subcommandGroupData -> subcommandGroupData.getName().equalsIgnoreCase(subCommand.SubCommandGroup()))) {
                if (!subCommand.SubCommandGroup().equalsIgnoreCase("")) {
                    subCommandGroups.add(new SubcommandGroupData(subCommand.SubCommandGroup(), "Bwah"));
                }
            }

            SubcommandData data = new SubcommandData(subCommand.subCommandId(), subCommand.description());

            for (SubcommandGroupData groupData : subCommandGroups) {
                if (groupData.getName().equalsIgnoreCase(subCommand.SubCommandGroup())) {
                    groupData.addSubcommands(data);
                }
            }
            subCommand.options().forEach(option -> data.addOption(option.type(), option.name(), option.description(),
                    option.required(), option.autoComplete()));
            if (subCommand.SubCommandGroup().equalsIgnoreCase("")) {
                cmddata.add(data);
            }
            map.putIfAbsent(subCommand.subCommandId(), subCommand);

        }
        commandData.addSubcommandGroups(subCommandGroups);
        commandData.addSubcommands(cmddata);
        this.subCommands.putAll(map);
        return commandData;
    }

}
