package care.cuddliness.djcarl.command.annotation;

import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface BaseCommandComponent {

    @NotNull String name();
    String description() default "Slash command";
    String group() default "default";
    Permission permission() default Permission.USE_APPLICATION_COMMANDS;


}
