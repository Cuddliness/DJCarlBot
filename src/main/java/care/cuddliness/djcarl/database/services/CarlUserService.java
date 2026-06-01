package care.cuddliness.djcarl.database.services;

import care.cuddliness.djcarl.database.repository.CarlUserRepository;
import care.cuddliness.djcarl.database.entity.CarlUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarlUserService {


    private final CarlUserRepository carlUserRepository;

    /**
     * Returns the DiscordUser for the given Discord snowflake ID.
     * Creates and persists a new one automatically if they haven't interacted before.
     *
     * @param discordId Discord snowflake ID (e.g. "123456789012345678")
     */
    @Transactional
    public CarlUser getOrCreate(Long discordId) {
        return carlUserRepository.findByDiscordId(discordId)
                .orElseGet(() -> {
                    log.info("New Discord user registered: {})", discordId);
                    return carlUserRepository.save(
                            CarlUser.builder()
                                    .discordId(discordId)
                                    .build()
                    );
                });
    }

    @Transactional()
    public CarlUser getByDiscordId(Long discordId) {
        return carlUserRepository.findByDiscordId(discordId)
                .orElseThrow(() -> new IllegalArgumentException("No user found for Discord ID: " + discordId));
    }
}
