package lab7.server.commands;

import lab7.common.commands.CommandType;
import lab7.common.dto.Response;
import lab7.common.dto.Request;

/**
 * Интерфейс Command является базовым контрактом поведения для всех команд
 */

public interface Command {
    String getName();

    String getDescription();

    CommandType getCommandType();

    Response execute(Request request, Long userId);
}
