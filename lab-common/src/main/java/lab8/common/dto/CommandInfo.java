package lab8.common.dto;

import java.io.Serializable;

import lab8.common.commands.CommandType;

public class CommandInfo implements Serializable {
    private static final long serialVersionUID = 5L;

    private final String name;
    private final int argCount;
    private final boolean requiresObject;
    private final boolean requiresAuthorization;

    public CommandInfo(String name, CommandType type) {
        this.name = name;
        this.argCount = type.getArgsCount();
        this.requiresObject = type.getRequiresObject();
        this.requiresAuthorization = type.getRequiresAuthorization();
    }


    public String getName() {
        return name;
    }

    public int getArgCount() {
        return argCount;
    }

    public boolean isRequiresObject() {
        return requiresObject;
    }

    public boolean isRequiresAuthorization() {
        return requiresAuthorization;
    }
}
