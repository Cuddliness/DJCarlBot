package care.cuddliness.djcarl;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BaseApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext baseApplication = new SpringApplicationBuilder(BaseApplication.class).web(
                WebApplicationType.NONE).run(args);

    }
}
