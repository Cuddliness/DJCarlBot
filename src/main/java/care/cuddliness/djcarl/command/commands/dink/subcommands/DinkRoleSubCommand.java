package care.cuddliness.djcarl.command.commands.dink.subcommands;

import care.cuddliness.djcarl.autoconfig.service.DinkService;
import care.cuddliness.djcarl.command.annotation.BaseSubCommandComponent;
import care.cuddliness.djcarl.command.commands.dink.DinkMainCommand;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;
import care.cuddliness.djcarl.utils.EmbedColor;
import care.cuddliness.djcarl.utils.EmbedUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@BaseSubCommandComponent(subCommandId = "roll", description = "Roll a random person in the drink pool", parent = DinkMainCommand.class)
public class DinkRoleSubCommand implements BaseSubCommandInterface {
    private final DinkService dinkService;

    public DinkRoleSubCommand(DinkService dinkService) {
        this.dinkService = dinkService;
    }
    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        if(dinkService.isOnCooldown()){
            EmbedUtil cooldownEmbed = new EmbedUtil();
            cooldownEmbed.setColor(EmbedColor.WARNING);
            cooldownEmbed.setTitle("Dink roll is on cooldown");
            cooldownEmbed.setDescription(dinkService.remainingTime());
            event.replyEmbeds(cooldownEmbed.build()).queue();
        }else{
            User user = dinkService.randomFromDinkAndStartCooldown();
            event.reply("Carl decided that it's " + user.getAsMention() + "'s turn to gobble on some alcoholic beverage!").queue();
        }

    }
}
