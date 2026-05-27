package lab8.server.handlers;

import lab8.common.dto.Response;
import lab8.common.dto.Request;
import lab8.server.managers.CommandManager;

public class RequestHandler {

    private final CommandManager commandManager;

    public RequestHandler(CommandManager commandmanager) {
        this.commandManager = commandmanager;
    }

    public Response handle(Request request){
        return commandManager.executeCommand(request);

    }
}
