package lab8.server.commands;

import lab8.common.commands.CommandType;

public abstract class AbstractCommand implements Command {
    private final String name;
    private final String description;
    private final CommandType type;

    /**
     * Класс абстрактной команды. Родительский класс для всех команд.
     */

    public AbstractCommand(String name,CommandType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CommandType getCommandType(){
        return type;
    }
}
