package care.cuddliness.djcarl.database.Repository;

import care.cuddliness.djcarl.database.entity.CarlUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarlUserRepository extends JpaRepository<CarlUser, Long> {

    Optional<CarlUser> findByDiscordId(String discordId);

    boolean existsByDiscordId(String discordId);
}
