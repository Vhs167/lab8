package lab7.server.commands;

import lab7.common.commands.CommandType;
import lab7.common.dto.Request;
import lab7.common.dto.Response;
import lab7.server.managers.CommandManager;

public class GetCommands extends AbstractCommand implements Command{

    private final CommandManager commandManager;

    public GetCommands(CommandManager commandManager){
        super("get_commands", CommandType.NO_ARG, "Возвращает информацию о командах");
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        return new Response(commandManager.getCommandsInfo(),"commands");
    }
}
