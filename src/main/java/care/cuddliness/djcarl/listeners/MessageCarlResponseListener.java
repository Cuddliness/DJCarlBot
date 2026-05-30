package care.cuddliness.djcarl.listeners;

import care.cuddliness.djcarl.utils.StringUtil;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.stereotype.Component;

@Component
public class MessageCarlResponseListener extends ListenerAdapter {

    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem() || event.isWebhookMessage()) {
            return;
        }
        String[] split = event.getMessage().getContentRaw().split(" ");
        for(String s : split){
            StringUtil util = new StringUtil(s.toLowerCase());
            if(util.isSimilar("DJCARL") || event.getMessage().getContentRaw().contains("DJ CARL")){
                event.getMessage().reply("THAT IS ME! DJ CARLLLLLL!!!").queue();
                return;
            }
            if(util.isSimilar("Send Nudes") || event.getMessage().getContentRaw().contains("Send Nudes")){
                event.getMessage().reply("https://dxgz8kglrqnev.cloudfront.net/wp-content/uploads/2023/10/AdobeStock_648723805-scaled.jpeg").queue();
                return;
            }
            if(util.isSimilar("DICKAAAAAH") || event.getMessage().getContentRaw().contains("DICKAAAAAH")){
                event.getMessage().reply("https://youtu.be/zU9V9QMXeyc?si=SDyfhCLqE545sJrf").queue();
                return;
            }

            if(util.isSimilar("SLAYER") || event.getMessage().getContentRaw().contains("SLAYER")){
                event.getMessage().reply("SLAYERRRRRRRR!!").queue();
                return;
            }
            if(util.isSimilar("feet") || event.getMessage().getContentRaw().toLowerCase().contains("feet")){
                event.getMessage().addReaction(Emoji.fromUnicode("\uD83E\uDDB6")).queue();
                return;
            }

        }
    }
}
