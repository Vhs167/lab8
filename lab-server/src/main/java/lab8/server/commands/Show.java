package lab8.server.commands;

import lab8.common.commands.CommandType;
import lab8.common.dto.Response;
import lab8.common.dto.Request;
import lab8.common.utils.Validator;
import lab8.server.managers.CollectionManager;


/**
 * Команда 'show' выводит все элементы коллекции
 */


public class Show extends AbstractCommand {

    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", CommandType.NO_ARG,"вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        Validator.validateIsEmpty(collectionManager.getCollection());
        return collectionManager.getAll();
    }
}
