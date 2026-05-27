package lab8.server.commands;

import lab8.common.commands.CommandType;
import lab8.common.dto.Response;
import lab8.common.dto.Request;

/**
 * Интерфейс Command является базовым контрактом поведения для всех команд
 */

public interface Command {
    String getName();

    String getDescription();

    CommandType getCommandType();

    Response execute(Request request, Long userId);
}
