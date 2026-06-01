package care.cuddliness.djcarl.database.repository;

import care.cuddliness.djcarl.database.entity.DinkPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DinkPoolRepository extends JpaRepository<DinkPool, Long> {

    Optional<DinkPool> findByDiscordId(Long discordId);

    boolean existsByDiscordId(Long discordId);
    void deleteByDiscordId(Long discordId);
    @Query(value = "SELECT * FROM dink_pool ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<DinkPool> pickRandom();
}
