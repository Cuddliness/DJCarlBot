package care.cuddliness.djcarl.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSave() {
        BaseUser user = new BaseUser();
        user.setUserId(new Random().nextLong());
        user.setGuildId(new Random().nextLong());
        user.setId(UUID.randomUUID());

        userRepository.save(user);

        UUID uuid = user.getId();
        Long userId = user.getUserId();
        Long guildId = user.getGuildId();

        BaseUser fetchTest = userRepository.findByUserIdAndGuildId(userId, guildId);

        Assertions.assertEquals(userId, fetchTest.getUserId());
        Assertions.assertEquals(guildId, fetchTest.getGuildId());
        Assertions.assertEquals(uuid, fetchTest.getId());


    }
}
