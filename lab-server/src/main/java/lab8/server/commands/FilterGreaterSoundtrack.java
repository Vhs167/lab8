package lab8.server.commands;

import lab8.common.commands.CommandType;
import lab8.common.dto.Response;
import lab8.common.dto.Request;
import lab8.server.managers.CollectionManager;


/**
 * Команда 'filter_greater_than_soundtrack_name' выводит элементы, значение поля soundtrackName которых больше заданного
 */

public class FilterGreaterSoundtrack extends AbstractCommand {

    private final CollectionManager collectionManager;

    public FilterGreaterSoundtrack(CollectionManager collectionManager) {
        super("filter_greater_than_soundtrack_name soundtrackName", CommandType.ONE_ARG, "вывести элементы, значение поля soundtrackName которых больше заданного");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        String soundtrack = request.getArgs()[0];
        return collectionManager.filterGreaterThanSoundtrack(soundtrack);
    }
}
