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
import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor


@BaseSubCommandComponent(subCommandId = "roll", description = "Roll a random person in the drink pool", parent = DinkMainCommand.class)
public class DinkRoleSubCommand implements BaseSubCommandInterface {
    private final DinkService dinkService;
    private final CarlUserRepository carlUserRepository;
    private final DinkPoolRepository dinkPoolRepository;

    private final CarlUserService carlUserService;
    private static final String[] DRINK_MESSAGES = {
            "Carl decided that it's %s's turn to gobble on some alcoholic beverage!",
            "The ancient spirits have spoken... %s must consume a drink of questionable origin!",
            "By the power of the riff, %s has been chosen to chug something unholy! 🤘",
            "Carl spun the bottle and it pointed directly at %s — bottoms up, warrior!",
            "The metal gods demand a sacrifice, and %s shall drink in their honor! 🤘",
            "Ozzy would be proud — %s has been summoned to drink from the chalice of chaos!",
            "Carl consulted the grimoire and it clearly states that %s must drink NOW!",
            "The circle pit stops for no one, but %s must step out and take a sip first!",
            "Legend has it that whoever Carl points at must drink... today that legend is %s!",
            "In the name of Black Sabbath and questionable decisions, %s drinks!",
            "Carl rolled a nat 1 on wisdom and chose %s to down a beverage of doom!",
            "The mosh pit has voted unanimously — %s takes the next drink! 🤘",
            "Darkness falls across the server... and %s must answer with a sip! 🎸",
            "Carl has gazed into the void, and the void pointed at %s. Drink up!",
            "The prophecy carved into Carl's guitar reads: %s shall drinketh tonight!",
            "Three things are certain in life: death, taxes, and %s drinking right now!",
            "Carl's ouija board spelled out %s's name. The spirits demand a beverage!",
            "Even Lemmy would raise a glass to this — %s is drinking! RIP to the legend. 🍺",
            "The breakdown hits hardest for %s, who must now drink through the pain! 🤘",
            "Carl has performed an ancient headbanging ritual and summoned %s to the drink altar!",
            "Thor's hammer couldn't stop it, Zeus's lightning couldn't prevent it — %s is drinking!",
            "Scientists have confirmed: the solution to all of %s's problems is one drink, starting NOW!",
            "Carl drew a pentagram on the floor, lit some candles, and %s appeared. Drink!",
            "In a world of chaos and distortion pedals, one truth remains — %s is drinking tonight!",
            "The solo shreds, the bass drops, and %s chugs. That's just how it works.",
            "Carl checked the setlist and %s is up next... for a drink! 🎶",
            "It is written in the Book of Riffs that %s shall not leave this round sober!",
            "The crowd goes wild as %s is pushed to the front of the stage to take a drink!",
            "Carl's coin flip, dice roll, and magic 8-ball all agreed: %s drinks! No further questions.",
            "Even the opening band knows — %s is drinking before the headliner comes on! 🤘"
    };
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
           Optional<DinkPool> opUser = dinkPoolRepository.findByDiscordId(sender.getIdLong());
           if(opUser.isPresent()) {
               DinkPool selfUser = opUser.get();
               if (selfUser.getTimeout() == 1) {
                   selfUser.setTimeout(0);
                   dinkPoolRepository.save(selfUser);
               }
           }
            DinkPool pool = dinkService.randomFromDinkAndStartCooldown();
            User user = event.getGuild().getJDA().getUserById(pool.getDiscordId());
            CarlUser carlUser = carlUserService.getOrCreate(sender.getUser().getIdLong());
            carlUser.setDinks(carlUser.getDinks() + 1);
            carlUserRepository.save(carlUser);
            String mention = Objects.requireNonNull(user).getAsMention();
            String randomMessage = String.format(
                    DRINK_MESSAGES[new Random().nextInt(DRINK_MESSAGES.length)],
                    mention
            );
            event.reply(randomMessage).queue();

        }

    }
}
