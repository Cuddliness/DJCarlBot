package care.cuddliness.djcarl.autoconfig;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.security.auth.login.LoginException;

@AutoConfiguration
@RequiredArgsConstructor
public class JdaAutoConfiguration {

    //Auto configure the JDA bot builder with credentials stored in the .env file
    @Bean
    @ConditionalOnMissingBean
    public JDA jda() throws LoginException, InterruptedException {
        return JDABuilder.createDefault(System.getenv("BOT_TOKEN"))
                .setStatus(OnlineStatus.ONLINE)
                .disableCache(CacheFlag.MEMBER_OVERRIDES)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MODERATION,
                        GatewayIntent.MESSAGE_CONTENT).build()
                .awaitReady();

    }
}
