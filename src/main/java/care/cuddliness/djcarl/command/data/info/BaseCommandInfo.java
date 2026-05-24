package care.cuddliness.djcarl.command.data.info;


public class BaseCommandInfo {

    private String name;
    private long id;

    public BaseCommandInfo(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
