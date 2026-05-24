package care.cuddliness.djcarl.utils;

import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.JDA;

public class Emoji {

    private JDA jda;

    public Emoji(JDA jda) {
        this.jda = jda;
    }

    public String emoteToMarkdown(String input) {
        String emojiname = net.dv8tion.jda.api.entities.emoji.Emoji.fromFormatted(input).getName();
        System.out.println("Name: " + emojiname);
        // Check if it's a custom emoji
        if (jda.getEmojisByName(emojiname, true).isEmpty()) {
            return EmojiParser.parseToAliases(input);
        } else {
            //Build alias string with idlong
            return ":" + jda.getEmojisByName(emojiname, true).get(0).getIdLong() + ":";
        }
    }

    public String fromMarkdown(String input) {
        String stripped = input.replace(":", "");
        //Check if stripped markdown contains long (Custom Emoji)
        try {
            if (Long.parseLong(stripped) != 0) {
                return jda.getEmojiById(Long.parseLong(stripped)).getAsMention();
            }
        } catch (NumberFormatException e) {
            return input;
        }
        return "";
    }
}
