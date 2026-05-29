package care.cuddliness.djcarl.command.commands.fun;

import care.cuddliness.djcarl.autoconfig.service.MetalQuoteService;
import care.cuddliness.djcarl.command.annotation.BaseCommandComponent;
import care.cuddliness.djcarl.command.data.BaseCommandInterface;
import care.cuddliness.djcarl.metalquote.MetalQuote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@BaseCommandComponent(name = "randommetalquote")
public class RandomMetalQuoteCommand  implements BaseCommandInterface {
    private final MetalQuoteService metalQuoteService;

    public RandomMetalQuoteCommand(MetalQuoteService metalQuoteService) {
        this.metalQuoteService = metalQuoteService;
    }


    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        MetalQuote q = metalQuoteService.getRandomQuote();
        event.reply(q.quote() + " ~ " + q.author() + " (" + q.band() + ")").queue();
    }
}
