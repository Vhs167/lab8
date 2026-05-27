package lab8.server.commands;

import lab8.common.commands.CommandType;
import lab8.common.dto.Request;
import lab8.common.dto.Response;
import lab8.server.managers.CommandManager;

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
