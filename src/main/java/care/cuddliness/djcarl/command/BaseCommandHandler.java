package care.cuddliness.djcarl.command;

import care.cuddliness.djcarl.command.annotation.BaseCommandComponent;
import care.cuddliness.djcarl.command.annotation.BaseCommandOption;
import care.cuddliness.djcarl.command.annotation.BaseSubCommandComponent;
import care.cuddliness.djcarl.command.data.AutoCompletableInterface;
import care.cuddliness.djcarl.command.data.BaseCommandExecutorInterface;
import care.cuddliness.djcarl.command.data.BaseCommandInterface;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class BaseCommandHandler {
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(BaseCommandHandler.class);

    private final @NotNull JDA jda;

    private final @NotNull Map<String, BaseCommand> commandsByName = new HashMap<>();
    private final @NotNull Map<Long, BaseCommand> commandsById = new HashMap<>();

    public BaseCommandHandler(@NotNull ApplicationContext applicationContext, @NotNull JDA jda, @Nullable Guild guild) {
        this.jda = jda;
        Map<Class<? extends BaseCommandInterface>, Set<BaseSubCommand>> subCommandReferences = new HashMap<>();
        for (BaseSubCommandInterface subCommand : applicationContext.getBeansOfType(BaseSubCommandInterface.class).values()) {
            BaseSubCommandComponent subCommandComponent = subCommand.getClass().getAnnotation(BaseSubCommandComponent.class);

            BaseCommandOption[] options = subCommand.getClass().getAnnotationsByType(BaseCommandOption.class);

            BaseSubCommand commandData = new BaseSubCommand(subCommand, subCommandComponent.subCommandId(),
                    subCommandComponent.subCommandGroupid(), Arrays.stream(options).toList());

            Set<BaseSubCommand> commandSet = subCommandReferences.computeIfAbsent(subCommandComponent.parent(), k -> new HashSet<>());
            commandSet.add(commandData);
        }

        for (BaseCommandInterface command : applicationContext.getBeansOfType(BaseCommandInterface.class).values()) {
            BaseCommandComponent commandComponent = command.getClass().getAnnotation(BaseCommandComponent.class);
            Map<String, BaseSubCommand> mappedSubCommands = BaseCommand.computeSubCommands(subCommandReferences.getOrDefault(command.getClass(), new HashSet<>()));

            BaseCommandOption[] options = command.getClass().getAnnotationsByType(BaseCommandOption.class);

            BaseCommand commandData = new BaseCommand(command, mappedSubCommands,Arrays.stream(options).toList(), commandComponent.name());
            this.commandsByName.put(commandData.name(), commandData);
        }

        LOGGER.info("Registered all in-memory commands: {}", this.commandsByName.values());

        this.init();
    }

    public void init() {
        List<CommandData> list = new ArrayList<>();
        commandsByName.forEach((name, command) -> {
            list.add(command.createCommandData());
        });

        jda.updateCommands()
                .addCommands(list)
                .queue(updatedCommands -> {
                    for (Command command : updatedCommands) {
                        BaseCommand cl = commandsByName.get(command.getName());
                        if (cl != null) {
                            commandsById.put(command.getIdLong(), cl);
                            LOGGER.info("Bound command {} to ID {}", command.getName(), command.getIdLong());
                        }
                    }
                });
    }
    @EventListener(SlashCommandInteractionEvent.class)
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CompletableFuture.runAsync(() -> {
            BaseCommandExecutorInterface commandExecutor = this.determineExecutor(event.getCommandIdLong(), event.getSubcommandName(), event.getSubcommandGroup());
            if (commandExecutor == null) return;

            commandExecutor.onExecute(event.getMember(), event);
        }).exceptionally(throwable -> {
            LOGGER.error("Error occurred whilst handling slash command event", throwable);
            return null;
        });
    }

    @EventListener(CommandAutoCompleteInteractionEvent.class)
    public void onSlashCommandAutoComplete(CommandAutoCompleteInteractionEvent event) {
        CompletableFuture.runAsync(() -> {
           BaseCommandExecutorInterface commandExecutor = this.determineExecutor(event.getCommandIdLong(), event.getSubcommandName(), event.getSubcommandGroup());
            if (commandExecutor == null) return;

            if (!(commandExecutor instanceof AutoCompletableInterface autoCompletable)) {
                LOGGER.info("Received autocomplete event for non-autocompletable command {}", event.getCommandString());
                return;
            }

            autoCompletable.onAutoComplete(event);
        }).exceptionally(throwable -> {
            LOGGER.error("Error occurred whilst handling autocomplete event", throwable);
            return null;
        });
    }

    private @Nullable BaseCommandExecutorInterface determineExecutor(long commandId, @Nullable String subCommandName, @Nullable String subCommandGroup) {
        String commandPath = subCommandGroup == null ? subCommandName : subCommandGroup + "/" + subCommandName;
        BaseCommand command = this.commandsById.get(commandId);
        if (command == null) {
            LOGGER.error("Command not found with ID {} and path {}", commandId, commandPath);
            return null;
        }
        if (subCommandName == null) {
            return command.command(); // BUT ensure it uses event.getOptions()
        }

        if (subCommandGroup != null) subCommandName = subCommandGroup + "/" + subCommandName;

        BaseSubCommand subCommand = command.subCommands().get(subCommandName);
        if (subCommand == null) {
            LOGGER.error("SubCommand not found with ID {} and path {}", subCommandName, commandPath);
            return null;
        }
        return subCommand.command();
    }
}