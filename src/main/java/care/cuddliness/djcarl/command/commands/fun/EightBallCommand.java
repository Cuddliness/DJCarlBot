package care.cuddliness.djcarl.command.commands.fun;

import care.cuddliness.djcarl.command.annotation.BaseCommandComponent;
import care.cuddliness.djcarl.command.annotation.BaseCommandOption;
import care.cuddliness.djcarl.command.data.BaseCommandInterface;
import care.cuddliness.djcarl.utils.EmbedColor;
import care.cuddliness.djcarl.utils.EmbedUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@BaseCommandOption(name = "question", description = "the question to get a response on", type = OptionType.STRING, required = true)
@BaseCommandComponent(name = "8ball", description = "Predict your faith by DJcarl")
public class EightBallCommand implements BaseCommandInterface {
    String[] eightBallAnswers = {
            // Positive answers
            "THE RIFFS ALIGN IN YOUR FAVOR, MORTAL",
            "AS DJCARL SHREDS, SO SHALL IT BE",
            "THE MOSH PIT OF FATE PUSHES YOU FORWARD",
            "IT IS CERTAIN, THE BREAKDOWN CONFIRMS IT",
            "ALL SIGNS POINT TO YES, LIKE A PERFECT CIRCLE PIT",
            "THE HORNS ARE UP — PROCEED WITH CONFIDENCE",
            "DJCARL'S SETLIST HAS SPOKEN: ABSOLUTELY",
            "THE DOUBLE BASS DRUM POUNDS OUT A RESOUNDING YES",
            "THE METAL GODS HAVE GRANTED YOUR REQUEST",
            "YOUR DESTINY IS AS HEAVY AS A DROP-D RIFF",
            "THE HEADBANGERS CIRCLE OPENS FOR YOU",
            "SIGNS SAY YES, NOW THROW THE HORNS",
            "THE SOLO IS IN YOUR FAVOR",
            "DJCARL DROPS THE NEEDLE — IT LANDS ON YES",
            "THE STAGE IS YOURS, THE ANSWER IS CLEAR",

            // Neutral / uncertain answers
            "THE FOG MACHINE OBSCURES THE ANSWER... ASK AGAIN",
            "EVEN DJCARL TAKES A BREAK BETWEEN SETS — TRY LATER",
            "THE BREAKDOWN IS COMING, BUT TIMING IS UNCLEAR",
            "THE CROWD IS SPLIT — CANNOT PREDICT NOW",
            "THE FEEDBACK LOOP IS STRONG, ASK ONCE MORE",
            "THE DISTORTION PEDAL MUDDLES THE SIGNAL",
            "NOT EVEN THE SETLIST KNOWS YET",
            "THE MOSH PIT IS UNDECIDED",
            "CONCENTRATE AND SCREAM IT LOUDER INTO THE VOID",
            "THE UNIVERSE IS TUNING ITS GUITAR — REPLY HAZY",
            "DJCARL IS ADJUSTING THE EQ, ANSWER PENDING",
            "BETWEEN SONGS NOW — THE ANSWER AWAITS",
            "THE SOUNDCHECK YIELDS NO CLEAR OMEN",
            "THE RIFF IS UNRESOLVED — TRY AGAIN",
            "OUTLOOK FOGGY, LIKE A VENUE FULL OF DRY ICE",

            // Negative answers
            "THE METAL GODS HAVE DECREED: NO",
            "DJCARL CUTS THE POWER — THE ANSWER IS NO",
            "THE PIT SWALLOWS THIS IDEA WHOLE",
            "NOT IN THIS LIFETIME OR THE NEXT TOUR",
            "THE STRINGS SNAP ON THAT ONE — DENIED",
            "THE CROWD BOOS YOUR QUESTION INTO OBLIVION",
            "DJCARL SCRATCHES THE RECORD — HARD NO",
            "THE AMP HAS BLOWN — YOUR FATE WITH IT",
            "THE DARK RIFF OF FATE SAYS ABSOLUTELY NOT",
            "THIS PATH LEADS ONLY TO THE WALL OF DEATH",
            "YOUR REQUEST HAS BEEN DROPPED FROM THE SETLIST",
            "THE FEEDBACK SCREAMS NO INTO YOUR SOUL",
            "EVEN SATAN'S METRONOME KEEPS TIME AGAINST YOU",
            "THE ENCORE WAS CANCELLED — AND SO WAS YOUR PLAN",
            "DJCARL UNPLUGS THE BASS — DEEP AND DEFINITIVE NO"
    };
    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        String question = event.getOption("question").getAsString();
        Random r = new Random();
        String answer = eightBallAnswers[r.nextInt(eightBallAnswers.length)];
        EmbedUtil embed = new EmbedUtil();
        embed.setColor(EmbedColor.PRIMARY);
        embed.setTitle(sender.getEffectiveName() + " Asked: ``" + question + "``");
        embed.setDescription(answer + " ~ DJCarl");
        event.replyEmbeds(embed.build()).queue();

    }
}
