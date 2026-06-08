package care.cuddliness.djcarl.command.data.info;


import lombok.Getter;
import net.dv8tion.jda.api.Permission;

public class BaseCommandInfo {

    @Getter private String name;
    @Getter private long id;
    @Getter private String description;
    @Getter private Permission permission;

    public BaseCommandInfo(String name, String description, Permission permission, long id) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.permission = permission;
    }
}
