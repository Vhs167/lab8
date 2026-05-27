package lab7.server.handlers;

import lab7.common.dto.Response;
import lab7.common.dto.Request;
import lab7.server.managers.CommandManager;

public class RequestHandler {

    private final CommandManager commandManager;

    public RequestHandler(CommandManager commandmanager) {
        this.commandManager = commandmanager;
    }

    public Response handle(Request request){
        return commandManager.executeCommand(request);

    }
}
