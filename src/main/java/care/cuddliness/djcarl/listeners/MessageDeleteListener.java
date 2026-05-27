package care.cuddliness.djcarl.listeners;

import care.cuddliness.djcarl.utils.EmbedColor;
import care.cuddliness.djcarl.utils.EmbedUtil;
import care.cuddliness.djcarl.values.ChannelValue;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;

@Component
public class MessageDeleteListener extends ListenerAdapter {
    static final HashMap<Long, Message> cache = new HashMap<>();

    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem() || event.isWebhookMessage()) {
            return;
        }

        cache.put(event.getMessageIdLong(), event.getMessage());
    }
    @SubscribeEvent
    public void onMessageDelete(MessageDeleteEvent event) {
        if(System.getenv("CHANNEL_LOG") != null){
            TextChannel logChannel = event.getGuild().getChannelById(TextChannel.class, System.getenv("CHANNEL_LOG"));
            Message message = cache.get(event.getMessageIdLong());

            if (message == null) {
                return;
            }

            if (!event.getChannelType().isMessage()) {
                return;
            }

            if(event.getChannel().getName().contains("Admin")){
                return;
            }

            EmbedUtil embed = new EmbedUtil();
            embed.setColor(EmbedColor.ERROR);
            embed.setTitle("Audit: Message deleted (%s)".formatted(message.getChannel().getAsMention()));
            embed.addField("Message removed from", message.getAuthor().getAsTag(), false);
            embed.addField("Message channel", event.getChannel().getAsMention(), false);
            embed.addField("Message content ", message.getContentRaw(), false);
            embed.setTimeStamp(Instant.now());

            logChannel.sendMessageEmbeds(embed.build()).queue();

        }
    }

    @SubscribeEvent
    public void onMessageUpdate(MessageUpdateEvent event) {
        Message message = cache.get(event.getMessageIdLong());
        if (message == null) {
            return;
        }


        TextChannel channel = event.getGuild().getTextChannelById(ChannelValue.LOG.getId());
        if (null == channel) {
            return;
        }

        EmbedUtil embedBuilder = new EmbedUtil();
        embedBuilder.setColor(EmbedColor.WARNING);
        embedBuilder.setTitle("Audit: Message edited (%s)".formatted(message.getChannel().getAsMention()));
        embedBuilder.addField("Actor", "Auto log", false);
        embedBuilder.addField("User",
                "%s (%s)".formatted(message.getAuthor().getName(), message.getAuthor().getAsMention()),
                false
        );
        embedBuilder.addField("old message", "%s".formatted("`" + message.getContentRaw() + "`"), false);
        embedBuilder.addField("new message", "%s".formatted("`" + event.getMessage().getContentRaw() + "`"), false);
        embedBuilder.setTimeStamp(Instant.now());

        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
