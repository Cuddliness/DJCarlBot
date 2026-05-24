package care.cuddliness.djcarl.command.annotation;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
@Repeatable(CommandOptions.class)
public @interface BaseCommandOption {

    @NotNull String name();

    @NotNull String description();

    @NotNull OptionType type();

    boolean required() default false;

    boolean autoComplete() default false;


}
