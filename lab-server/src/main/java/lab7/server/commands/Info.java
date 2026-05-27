package lab7.server.commands;

import lab7.common.commands.CommandType;
import lab7.common.dto.Request;
import lab7.common.dto.Response;
import lab7.server.managers.CollectionManager;

import java.util.Collections;

/**
 * Команда 'info' выводит информацию о коллекции
 */

public class Info extends AbstractCommand {

    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        super("info", CommandType.NO_ARG ,"вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        StringBuilder sb = new StringBuilder();

        sb.append("Тип коллекци: ");
        sb.append(collectionManager.getCollectionType());
        sb.append("\n");

        sb.append("Дата инициализации: ");
        sb.append(collectionManager.getInitTime());
        sb.append("\n");

        sb.append("Количество элементов: ");
        sb.append(collectionManager.getSize());

        return new Response(Collections.emptyList(), sb.toString());
    }
}
