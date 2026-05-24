import org.junit.runner.RunWith;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StacyApplicationTest {

    public static void main(String[] args) {
        ConfigurableApplicationContext stacyApplication = new SpringApplicationBuilder(StacyApplicationTest.class).web(
                WebApplicationType.NONE).run(args);
    }
}
