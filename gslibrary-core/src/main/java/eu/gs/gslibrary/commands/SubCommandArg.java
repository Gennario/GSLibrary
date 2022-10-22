package eu.gs.gslibrary.commands;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCommandArg {

    private String name;
    private CommandArgType type;
    private CommandArgValue value;
    public SubCommandArg(String name, CommandArgType type, CommandArgValue value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public enum CommandArgType {
        OPTIONAL,
        REQUIRED
    }

    public enum CommandArgValue {
        STRING,
        INT,
        DOUBLE,
        FLOAT,
        LONG,
        PLAYER,
        OFFLINE_PLAYER,
        ENTITY,
        MATERIAL,
        GUI
    }

}
