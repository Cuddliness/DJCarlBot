package care.cuddliness.djcarl.command.commands.dink.subcommands;

import care.cuddliness.djcarl.autoconfig.service.DinkService;
import care.cuddliness.djcarl.command.annotation.BaseSubCommandComponent;
import care.cuddliness.djcarl.command.commands.dink.DinkMainCommand;
import care.cuddliness.djcarl.command.data.BaseSubCommandInterface;
import care.cuddliness.djcarl.database.repository.CarlUserRepository;
import care.cuddliness.djcarl.database.entity.CarlUser;
import care.cuddliness.djcarl.database.entity.DinkPool;
import care.cuddliness.djcarl.database.repository.DinkPoolRepository;
import care.cuddliness.djcarl.database.services.CarlUserService;
import care.cuddliness.djcarl.utils.EmbedColor;
import care.cuddliness.djcarl.utils.EmbedUtil;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor


@BaseSubCommandComponent(subCommandId = "roll", description = "Roll a random person in the drink pool", parent = DinkMainCommand.class)
public class DinkRoleSubCommand implements BaseSubCommandInterface {
    private final DinkService dinkService;
    private final CarlUserRepository carlUserRepository;
    private final DinkPoolRepository dinkPoolRepository;

    private final CarlUserService carlUserService;
    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        if(dinkService.isOnTimeOut(event.getUser())){
            event.reply("You can't roll since your on cooldown, don't forget to drink some water and rest").setEphemeral(true).queue();
            return;
        }
        if(dinkService.isOnCooldown()){
            EmbedUtil cooldownEmbed = new EmbedUtil();
            cooldownEmbed.setColor(EmbedColor.WARNING);
            cooldownEmbed.setTitle("Dink roll is on cooldown");
            cooldownEmbed.setDescription(dinkService.remainingTime("dink"));
            event.replyEmbeds(cooldownEmbed.build()).queue();
        }else{

            /*
            * Here we check if the user in the dinkpool still has cooldown status in the database but is already expired.
            * Since here above we already check if they are really on cooldown
            *
            * */
            DinkPool selfUser = dinkPoolRepository.findByDiscordId(sender.getIdLong()).get();
            if(selfUser.getTimeout() == 1){
                selfUser.setTimeout(0);
                dinkPoolRepository.save(selfUser);
            }
            DinkPool pool = dinkService.randomFromDinkAndStartCooldown();
            User user = event.getGuild().getJDA().getUserById(pool.getDiscordId());
            CarlUser carlUser = carlUserService.getOrCreate(sender.getUser().getIdLong());
            carlUser.setDinks(carlUser.getDinks() + 1);
            carlUserRepository.save(carlUser);
            event.reply("Carl decided that it's " + Objects.requireNonNull(user).getAsMention() + "'s turn to gobble on some alcoholic beverage!").queue();

        }

    }
}
