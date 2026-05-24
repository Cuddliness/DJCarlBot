package care.cuddliness.djcarl.command.annotation;

import care.cuddliness.djcarl.command.data.BaseCommandInterface;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface BaseSubCommandComponent {

    Class<? extends BaseCommandInterface> parent();

    String subCommandId();

    String subCommandGroupid() default "";

}
