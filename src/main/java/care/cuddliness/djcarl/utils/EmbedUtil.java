package care.cuddliness.djcarl.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;

public class EmbedUtil {
    private EmbedColor color;
    private EmbedBuilder builder;


    public EmbedUtil(EmbedColor color) {
        this.color = color;
        builder = new EmbedBuilder();
    }

    public EmbedUtil() {
        this.color = EmbedColor.PRIMARY;
        builder = new EmbedBuilder();
    }

    public EmbedUtil setTitle(@NotNull String title) {
        builder.setTitle(title);
        return this;
    }

    public EmbedUtil setDescription(@NotNull String description) {
        builder.setDescription(description);
        return this;
    }

    public EmbedUtil addField(String name, String value, boolean inline) {
        builder.addField(name, value, inline);
        return this;
    }

    public EmbedUtil setThumbnail(@NotNull String url) {
        builder.setThumbnail(url);
        return this;
    }

    public EmbedUtil setColor(EmbedColor color) {
        builder.setColor(Color.decode(color.getColor()));
        this.color = color;
        return this;
    }

    public EmbedUtil setFooter(String text) {
        builder.setFooter(text);
        return this;
    }

    public EmbedUtil setTimeStamp(Instant instant) {
        builder.setTimestamp(instant);
        return this;
    }

    public EmbedUtil setImage(@NotNull String url) {
        builder.setImage(url);
        return this;
    }

    public MessageEmbed build() {
        builder.setColor(Color.decode(color.getColor()));
        return builder.build();
    }


    public EmbedUtil from(MessageEmbed embed) {
        embed.getFields().forEach(field -> {
            builder.addField(field.getName(), field.getValue(), field.isInline());
        });
        if (embed.getThumbnail() != null)
            builder.setThumbnail(embed.getThumbnail().getUrl());
        builder.setColor(embed.getColor());
        if (embed.getDescription() != null)
            builder.setDescription(embed.getDescription());
        if (embed.getFooter() != null)
            builder.setFooter(embed.getFooter().getText());
        return this;
    }


}
