package lab7.server.commands;

import lab7.common.commands.CommandType;
import lab7.common.dto.Response;
import lab7.common.dto.Request;
import lab7.common.utils.Validator;
import lab7.server.managers.CollectionManager;


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
