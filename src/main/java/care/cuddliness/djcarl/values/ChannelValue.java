package care.cuddliness.djcarl.values;

import jakarta.annotation.Nullable;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public enum ChannelValue {

    LOG(System.getenv("CHANNEL_LOG"));

    @Getter
    private final Long id;

    ChannelValue(String id)
    {
        this(Long.valueOf(id));
    }

    ChannelValue(Long id)
    {
        this.id = id;
    }

    @Nullable
    public MessageChannel getMessageChannel(Guild guild)
    {
        return guild.getTextChannelById(id);
    }
}
