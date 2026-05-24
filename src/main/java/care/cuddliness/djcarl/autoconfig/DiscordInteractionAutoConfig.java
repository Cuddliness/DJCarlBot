package care.cuddliness.djcarl.autoconfig;

import care.cuddliness.djcarl.interaction.InteractionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@RequiredArgsConstructor
public class DiscordInteractionAutoConfig {

    private final ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    public InteractionHandler discordInteractionBackend() {
        return new InteractionHandler(this.applicationContext);
    }
}
