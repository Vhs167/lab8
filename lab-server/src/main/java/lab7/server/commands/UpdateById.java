package lab7.server.commands;

import lab7.common.commands.CommandType;
import lab7.common.dto.Response;
import lab7.common.dto.Request;
import lab7.common.utils.Validator;
import lab7.server.factory.HumanBeingFactory;
import lab7.server.managers.CollectionManager;
import lab7.common.models.HumanBeing;

/**
 * Команда 'update' обновляет элемент коллекции по заданному id
 */

public class UpdateById extends AbstractCommand {

    private final CollectionManager collectionManager;

    public UpdateById(CollectionManager collectionManager) {
        super("update id {element}", CommandType.ONE_ARG_WITH_OBJECT, "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        long id = Validator.validateLongArg(request.getArgs(), getName());
        Validator.validateId(id);

        HumanBeing oldHuman = collectionManager.findById(id);
        Validator.validateExists(oldHuman);

        HumanBeing newHuman = HumanBeingFactory.create(request.getHumanBeingRequest());
        return collectionManager.updateById(id, newHuman, userId);
    }
}
