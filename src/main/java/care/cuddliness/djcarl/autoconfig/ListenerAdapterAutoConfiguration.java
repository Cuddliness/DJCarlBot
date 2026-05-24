package care.cuddliness.djcarl.autoconfig;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;

@AutoConfiguration
@AutoConfigureAfter(JdaAutoConfiguration.class)
public class ListenerAdapterAutoConfiguration {

    //Automatic registers JDA's listener adapters
    public ListenerAdapterAutoConfiguration(@NotNull ListenerAdapter[] listenerAdapters, JDA jda) {
        for (ListenerAdapter listenerAdapter : listenerAdapters) {
            jda.addEventListener(listenerAdapter);
        }
    }
}
