package care.cuddliness.djcarl.autoconfig.service;

import care.cuddliness.djcarl.database.repository.DinkPoolRepository;
import care.cuddliness.djcarl.database.entity.DinkPool;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Service
public class DinkService {
    @Getter
    List<User> dinkusers;

    private final DinkPoolRepository repository;
    private final CooldownService cooldownService;

    public boolean addToDink(User user){
        if (repository.existsByDiscordId(user.getIdLong())) {
            return false;
        }
        repository.save(DinkPool.builder().discordId(user.getIdLong()).build());
        return true;
    }

    @Transactional
    public boolean removeFromDink(User user){
        if (!repository.existsByDiscordId(user.getIdLong())) {
            return false;
        }
        repository.deleteByDiscordId(user.getIdLong());
        return true;
    }

    public List<DinkPool> getAll() {
        return repository.findAll();
    }
    public boolean isOnCooldown(){
        return cooldownService.isOnCooldown("dink");
    }
    public String remainingTime(){
        return cooldownService.getRemainingTimeFormatted("dink");
    }

    public DinkPool randomFromDinkAndStartCooldown(){
        Optional<DinkPool> d = repository.pickRandom();
        cooldownService.startCooldown("dink");
        return d.get();
    }

}
