package lab8.server.commands;

import lab8.common.commands.CommandType;
import lab8.common.dto.Response;
import lab8.common.dto.Request;
import lab8.common.utils.Validator;
import lab8.server.factory.HumanBeingFactory;
import lab8.server.managers.CollectionManager;
import lab8.common.models.HumanBeing;

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
