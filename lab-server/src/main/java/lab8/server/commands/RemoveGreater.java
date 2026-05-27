package lab8.server.commands;


import lab8.common.commands.CommandType;
import lab8.common.dto.Response;
import lab8.common.dto.Request;
import lab8.common.utils.Validator;
import lab8.server.managers.CollectionManager;


/**
 * Команда 'remove_greater' удаляет все элементы коллекции, превыщающие заданное значение поля impactSpeed
 */

public class RemoveGreater extends AbstractCommand {

    private final CollectionManager collectionManager;

    public RemoveGreater(CollectionManager collectionManager) {
        super("remove_greater {element}", CommandType.ONE_ARG ,"удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        double impactSpeed = Validator.validateDoubleArg(request.getArgs(), getName());
        return collectionManager.removeGreater(impactSpeed, userId);
    }
}
