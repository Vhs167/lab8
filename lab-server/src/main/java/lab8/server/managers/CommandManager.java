package lab8.server.managers;

import lab8.common.commands.CommandType;
import lab8.common.dto.CommandInfo;
import lab8.common.dto.Request;
import lab8.common.utils.Validator;
import lab8.server.auth.AuthService;
import lab8.server.commands.*;
import lab8.common.dto.Response;

import lab8.server.commands.*;
import lab8.server.utils.ServerLogger;


import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;


/**
 * Класс оперирует командами
 */


public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();
    private final Set<String> publicCommands = Set.of("login", "register", "get_commands");
    private final AuthService authService;

    public CommandManager(CollectionManager collectionManager, AuthService authService) {
        this.authService = authService;
        commands.put("show", new Show(collectionManager));
        commands.put("clear", new Clear(collectionManager));
        commands.put("remove_by_id", new RemoveById(collectionManager));
        commands.put("group_by_real_hero", new GroupByRealHero(collectionManager));
        commands.put("count_by_impact_speed", new CountByImpactSpeed(collectionManager));
        commands.put("add", new Add(collectionManager));
        commands.put("update_by_id", new UpdateById(collectionManager));
        commands.put("add_if_min", new AddIfMin(collectionManager));
        commands.put("remove_greater", new RemoveGreater(collectionManager));
        commands.put("remove_lower", new RemoveLower(collectionManager));
        commands.put("filter_greater_then_soundtrack_name", new FilterGreaterSoundtrack(collectionManager));
        commands.put("info", new Info(collectionManager));
        commands.put("help", new Help(this));
        commands.put("get_commands", new GetCommands(this));
        commands.put("register", new Register(authService));
        commands.put("login", new Login(authService));
    }

    public Map<String, Command> getCommandsList() {
        return commands.entrySet().stream()
                .filter(entry -> !"get_commands".equals(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    public List<CommandInfo> getCommandsInfo() {
        return commands.entrySet().stream()
                .map(c -> new CommandInfo(
                        c.getKey(),
                        c.getValue().getCommandType()))
                .collect(Collectors.toList());
    }


    /**
     * Выполняет команду, введённую пользователем в виде строки
     */

    public Response executeCommand(Request request) {
        String commandName = request.getCommandName();
        Command command = commands.get(commandName);

        if (command == null) {
            return new Response(Collections.emptyList(), "Неизвестная команда: " + request.getCommandName());
        }
        try {
            CommandType type = command.getCommandType();
            Validator.validate(type.getArgsCount(), type.getRequiresObject(), request);


            Long userId = null;

            if (!publicCommands.contains(commandName)) {
                if (request.getUserName() == null || request.getPassword() == null) {
                    return new Response(Collections.emptyList(), "Ошибка: необходима авторизация");
                }
                userId = authService.login(request.getUserName(), request.getPassword());
                if (userId == null || userId <= 0) {
                    return new Response(Collections.emptyList(), "Ошибка: такого пользователя не существует");
                }
            }

            Response response = command.execute(request, userId);
            ServerLogger.logger.info("Команда выполнена: " + command.getName());

            return response;

        } catch (Exception e) {
            ServerLogger.logger.log(Level.WARNING, "Ошибка выполнения команды", e);
            return new Response(Collections.emptyList(), "Ошибка: " + e.getMessage());
        }

    }
}
