package care.cuddliness.djcarl.database;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;

@Configuration
@EnableJpaAuditing
public class DatabaseConfig {

    static {
        new File("data").mkdirs();
    }
}
