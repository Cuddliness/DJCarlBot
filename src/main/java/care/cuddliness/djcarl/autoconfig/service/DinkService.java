package care.cuddliness.djcarl.autoconfig.service;

import lombok.Getter;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DinkService {
    @Getter
    List<User> dinkusers;

    private final CooldownService cooldownService;

    public DinkService(CooldownService cooldownService) {
        this.cooldownService = cooldownService;
        dinkusers = new ArrayList<>();
    }

    public void addToDink(User user){
        dinkusers.add(user);
    }
    public void removeFromDink(User user){
        dinkusers.add(user);
    }
    public boolean isInDink(User user){
        return dinkusers.contains(user);
    }
    public boolean isOnCooldown(){
        return cooldownService.isOnCooldown("dink");
    }
    public String remainingTime(){
        return cooldownService.getRemainingTimeFormatted("dink");
    }

    public User randomFromDinkAndStartCooldown(){
        Random random = new Random();
        cooldownService.startCooldown("dink");
        return dinkusers.get(random.nextInt(dinkusers.size()));
    }

}
