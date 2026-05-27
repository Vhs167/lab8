package lab8.server.commands;


import lab8.common.commands.CommandType;
import lab8.common.dto.Response;
import lab8.common.dto.Request;
import lab8.common.utils.Validator;
import lab8.server.managers.CollectionManager;


/**
 * Команда 'remove_by_id' удаляет элемент из коллекции по значению его id
 */

public class RemoveById extends AbstractCommand {

    private final CollectionManager collectionManager;


    public RemoveById(CollectionManager collectionManager) {
        super("remove_by_id id", CommandType.ONE_ARG,"удалить элемент из коллекции по его id");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request, Long userId) {

        long id = Validator.validateLongArg(request.getArgs(), getName());
        Validator.validateId(id);
        return collectionManager.removeById(id, userId);
    }
}
