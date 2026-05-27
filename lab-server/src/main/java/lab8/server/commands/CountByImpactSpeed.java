package lab8.server.commands;

import lab8.common.commands.CommandType;
import lab8.common.dto.Response;
import lab8.common.dto.Request;
import lab8.common.utils.Validator;
import lab8.server.managers.CollectionManager;

/**
 * Команада "count_by_impact_speed выводит количество элементов с заданным полем impactSpeed
 */

public class CountByImpactSpeed extends AbstractCommand {

    private final CollectionManager collectionManager;

    public CountByImpactSpeed(CollectionManager collectionManager) {
        super("count_by_impact_speed impactSpeed", CommandType.ONE_ARG, "вывести количество элементов, значение поля impactSpeed которых равно заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        double impactSpeed = Validator.validateDoubleArg(request.getArgs(), getName());
        return collectionManager.countByImpactSpeed(impactSpeed);
    }
}
