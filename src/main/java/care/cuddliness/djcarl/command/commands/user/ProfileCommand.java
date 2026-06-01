package care.cuddliness.djcarl.command.commands.user;

import care.cuddliness.djcarl.command.annotation.BaseCommandComponent;
import care.cuddliness.djcarl.command.data.BaseCommandInterface;
import care.cuddliness.djcarl.database.entity.CarlUser;
import care.cuddliness.djcarl.database.services.CarlUserService;
import care.cuddliness.djcarl.utils.EmbedColor;
import care.cuddliness.djcarl.utils.EmbedUtil;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@BaseCommandComponent(name = "profile", description = "profile command")
public class ProfileCommand implements BaseCommandInterface {

    private final CarlUserService carlUserService;

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        CarlUser carlUser = carlUserService.getOrCreate(sender.getIdLong());
        EmbedUtil embedUtil = new EmbedUtil();
        embedUtil.setColor(EmbedColor.PRIMARY);
        embedUtil.setTitle("Profile of: " + sender.getEffectiveName());

        embedUtil.addField(":beer: Dinks", "`" + carlUser.getDinks() + "`", false);

        embedUtil.setThumbnail(sender.getEffectiveAvatarUrl());
        event.replyEmbeds(embedUtil.build()).queue();
    }
}
