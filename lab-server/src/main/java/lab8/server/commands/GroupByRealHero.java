package lab8.server.commands;

import lab8.common.commands.CommandType;
import lab8.common.dto.Response;
import lab8.common.dto.Request;
import lab8.server.managers.CollectionManager;

import java.util.Collections;

/**
 * Команда 'group_counting_by_real_hero' группирует элементы по значению поля realHero и выводит количество элементов в каждой группе
 */

public class GroupByRealHero extends AbstractCommand {

    private final CollectionManager collectionManager;


    public GroupByRealHero(CollectionManager collectionManager) {
        super("group_counting_by_real_hero", CommandType.NO_ARG, "сгруппировать элементы коллекции по значению поля realHero, вывести количество элементов в каждой группе");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        return new Response(Collections.emptyList(), "Количество настоящих героев и не героев: " + collectionManager.countByRealHero());
    }
}
