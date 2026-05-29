package care.cuddliness.djcarl.command.data.info;


public class BaseCommandInfo {

    private String name;
    private long id;
    private String description;

    public BaseCommandInfo(String name, String description, long id) {
        this.name = name;
        this.id = id;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
