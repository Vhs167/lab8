package lab8.server.commands;

import lab8.common.commands.CommandType;
import lab8.common.dto.Response;
import lab8.common.dto.Request;
import lab8.common.utils.Validator;
import lab8.server.managers.CollectionManager;


/**
 * Команда 'remove_lower' удаляет все элементы коллекции, не превыщающие заданное значение поля impactSpeed
 */

public class RemoveLower extends AbstractCommand {

    private final CollectionManager collectionManager;


    public RemoveLower(CollectionManager collectionManager) {
        super("remove_lower {element}", CommandType.ONE_ARG,"удалить из коллекции все элементы, меньшие, чем заданный");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        double impactSpeed = Validator.validateDoubleArg(request.getArgs(), getName());
        return collectionManager.removeLower(impactSpeed, userId);
    }
}

