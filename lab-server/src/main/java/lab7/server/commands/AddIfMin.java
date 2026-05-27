package lab7.server.commands;

import lab7.common.commands.CommandType;
import lab7.common.dto.Request;
import lab7.common.dto.Response;
import lab7.server.factory.HumanBeingFactory;
import lab7.server.managers.CollectionManager;
import lab7.common.models.HumanBeing;

/**
 * Команда 'add_if_min". Добавляет элемент в коллекцию, если его ImpactSpeed минимальный
 *
 * @author mikhail
 */


public class AddIfMin extends AbstractCommand {

    private final CollectionManager collectionManager;

    public AddIfMin(CollectionManager collectionManager) {
        super("add_if_min {element}", CommandType.WITH_OBJECT, "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        HumanBeing human = HumanBeingFactory.create(request.getHumanBeingRequest());
        return collectionManager.addIfMin(human,userId);
    }
}
