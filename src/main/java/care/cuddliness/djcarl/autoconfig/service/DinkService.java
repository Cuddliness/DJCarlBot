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
import java.util.Random;

@Component
@RequiredArgsConstructor
@Service
public class DinkService {

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

    public boolean isInDinkPool(User user){
        return repository.existsByDiscordId(user.getIdLong());
    }

    public List<DinkPool> getAll() {
        return repository.findAll();
    }
    public boolean isOnCooldown(){
        return cooldownService.isOnCooldown("dink");
    }
    public String remainingTime(String cooldownKey){
        return cooldownService.getRemainingTimeFormatted(cooldownKey);
    }

    public void startTimeout(User user, int duration){
        DinkPool dinkPool = repository.findByDiscordId(user.getIdLong()).get();
        dinkPool.setTimeout(1);
        repository.save(dinkPool);
        cooldownService.startCooldown("dink_timeout_" + user.getName(), duration);
    }

    public boolean isOnTimeOut(User user){
        return cooldownService.isOnCooldown("dink_timeout_" + user.getName());
    }

    public void clearTimeOut(User user){
        DinkPool dinkPool = repository.getReferenceById(user.getIdLong());
        dinkPool.setTimeout(0);
        repository.save(dinkPool);
        cooldownService.clearCooldown("dink_timeout_" + user.getName());
    }

    public DinkPool randomFromDinkAndStartCooldown(){
        List<DinkPool> entries = repository.findAllEligible();
        if (entries.isEmpty()) {
            throw new IllegalStateException("No eligible drink pool entries found!");
        }
        cooldownService.startCooldown("dink", 30);
        return entries.get(new Random().nextInt(entries.size()));
    }

}
