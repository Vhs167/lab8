package lab7.server.commands;

import lab7.common.commands.CommandType;
import lab7.common.dto.Request;
import lab7.common.dto.Response;
import lab7.server.factory.HumanBeingFactory;
import lab7.server.managers.CollectionManager;
import lab7.common.models.HumanBeing;

/**
 * Команда "add". Добавляет новый элемент в коллекцию.
 */

public class Add extends AbstractCommand {
    private final CollectionManager collectionManager;


    public Add(CollectionManager collectionManager) {
        super("add", CommandType.WITH_OBJECT, "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }


    @Override
    public Response execute(Request request, Long userId) {
        HumanBeing human = HumanBeingFactory.create(request.getHumanBeingRequest());
        return collectionManager.add(human, userId);
    }
}
