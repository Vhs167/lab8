package lab8.server.commands;

import lab8.common.commands.CommandType;
import lab8.common.dto.Response;
import lab8.common.dto.Request;
import lab8.server.managers.CollectionManager;


/**
 * Команда 'clear' очищает все элементы коллекции
 */

public class Clear extends AbstractCommand {

    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", CommandType.NO_ARG, "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        return collectionManager.clear(userId);

    }
}
