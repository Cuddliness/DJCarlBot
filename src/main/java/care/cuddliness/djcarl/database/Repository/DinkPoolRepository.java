package care.cuddliness.djcarl.database.Repository;

import care.cuddliness.djcarl.database.entity.DinkPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DinkPoolRepository extends JpaRepository<DinkPool, Long> {

    Optional<DinkPool> findByDiscordId(String discordId);

    boolean existsByDiscordId(String discordId);
    void deleteByDiscordId(String discordId);
    @Query(value = "SELECT * FROM dink_pool ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<DinkPool> pickRandom();
}
