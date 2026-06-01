package care.cuddliness.djcarl.database.repository;

import care.cuddliness.djcarl.database.entity.CarlUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarlUserRepository extends JpaRepository<CarlUser, Long> {

    Optional<CarlUser> findByDiscordId(Long discordId);

    boolean existsByDiscordId(Long discordId);

    @Query(value = "SELECT * FROM discord_users ORDER BY dinks DESC LIMIT 5", nativeQuery = true)
    List<CarlUser> dinkLeaderboard();
}
