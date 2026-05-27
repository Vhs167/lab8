package lab8.server.commands;

import lab8.common.commands.CommandType;
import lab8.common.dto.Response;
import lab8.common.dto.Request;
import lab8.server.managers.CommandManager;

import java.util.Collections;
import java.util.Map;

/**
 * Команда 'help' выводит справку по командам
 */


public class Help extends AbstractCommand {

    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", CommandType.NO_ARG, "вывести справку по доступным командам");
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request request, Long userId) {
        StringBuilder sb = new StringBuilder();
        try{
            Thread.sleep(10000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        sb.append("Список доступных команд: \n");

        for (Map.Entry<String, Command> entry : commandManager.getCommandsList().entrySet()) {
            String name = entry.getKey();
            Command command = entry.getValue();

            sb.append("-");
            sb.append(name);
            sb.append(" : ");
            sb.append(command.getDescription());
            sb.append("\n");

        }
        sb.append("-");
        sb.append("logout");
        sb.append(" : ");
        sb.append("команда для выхода из аккаунта");
        sb.append("\n");
        sb.append("-");
        sb.append("execute_script");
        sb.append(" : ");
        sb.append("считать и исполнить скрипт из указанного файла");
        sb.append("\n");
        sb.append("-");
        sb.append("exit");
        sb.append(" : ");
        sb.append("выход из приложения");
        sb.append("\n");
        return new Response(Collections.emptyList(), sb.toString());
    }
}
