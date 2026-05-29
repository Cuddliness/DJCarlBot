package care.cuddliness.djcarl.autoconfig.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CooldownService {


    // Store cooldown expiration times
    private final ConcurrentHashMap<String, Instant> cooldowns = new ConcurrentHashMap<>();

    // 30 minute cooldown
    private static final Duration COOLDOWN_DURATION = Duration.ofMinutes(30);

    /**
     * Starts cooldown for a key/user
     */
    public void startCooldown(String key) {
        cooldowns.put(key, Instant.now().plus(COOLDOWN_DURATION));
    }

    /**
     * Checks if cooldown is active
     */
    public boolean isOnCooldown(String key) {
        Instant expiresAt = cooldowns.get(key);

        if (expiresAt == null) {
            return false;
        }

        // Remove expired cooldown
        if (Instant.now().isAfter(expiresAt)) {
            cooldowns.remove(key);
            return false;
        }

        return true;
    }

    /**
     * Remaining cooldown time in seconds
     */
    public long getRemainingSeconds(String key) {
        Instant expiresAt = cooldowns.get(key);

        if (expiresAt == null) {
            return 0;
        }

        long seconds = Duration.between(Instant.now(), expiresAt).getSeconds();

        return Math.max(seconds, 0);
    }

    /**
     * Clear cooldown manually
     */
    public void clearCooldown(String key) {
        cooldowns.remove(key);
    }
    public String getRemainingTimeFormatted(String key) {
        Instant expiresAt = cooldowns.get(key);

        if (expiresAt == null) {
            return "0m 0s";
        }

        Duration duration = Duration.between(Instant.now(), expiresAt);

        if (duration.isNegative()) {
            cooldowns.remove(key);
            return "0m 0s";
        }

        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();

        return String.format("%02dm %02ds", minutes, seconds);
    }
}
