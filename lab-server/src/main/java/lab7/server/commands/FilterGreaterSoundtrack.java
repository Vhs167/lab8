package lab7.server.commands;

import lab7.common.commands.CommandType;
import lab7.common.dto.Response;
import lab7.common.dto.Request;
import lab7.server.managers.CollectionManager;


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
