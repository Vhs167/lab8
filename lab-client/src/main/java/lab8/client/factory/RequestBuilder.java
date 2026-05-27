package lab8.client.factory;

import lab8.common.dto.CommandInfo;
import lab8.common.dto.HumanBeingRequest;
import lab8.common.dto.Request;
import lab8.common.exceptions.InvalidCommandException;
import lab8.common.exceptions.InvalidFieldException;
import lab8.common.utils.Validator;
import lab8.client.managers.IOManager;
import lab8.client.readers.HumanBeingInputReader;

import java.util.Map;

public class RequestBuilder {
    private final IOManager ioManager;
    private final Map<String, CommandInfo> commands;


    public RequestBuilder(IOManager ioManager, Map<String, CommandInfo> commands) {
        this.ioManager = ioManager;
        this.commands = commands;
    }

    public Request buildRequest() {
        if (!ioManager.isScriptMode()) {
            ioManager.print("> ");
        }

        String line = ioManager.readLine();

        if (line == null || line.isEmpty()) {
            return null;
        }

        String[] parts = line.trim().split("\\s+");
        String commandName = parts[0];

        CommandInfo info = commands.get(commandName);

        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);

        if (commandName.equals("exit") || commandName.equals("logout") || commandName.equals("execute_script")) {
            return new Request(commandName, null, args, null, null);
        }

        if (info == null) {
            ioManager.printError("Неизвестная команда");
            return null;
        }

        HumanBeingRequest humanBeingRequest = null;
        if (info.isRequiresObject()) {
            try {
                HumanBeingInputReader reader = new HumanBeingInputReader(ioManager);
                humanBeingRequest = reader.read();
            } catch (InvalidFieldException e) {
                ioManager.printError("Ошибка ввода: " + e.getMessage());
                ioManager.println("Попробуйте снова");

                return null;
            }
        }
        String user = null;
        String password = null;

        if (commandName.equals("register") || commandName.equals("login")) {
            if (!ioManager.isScriptMode()) {
                ioManager.print("Введите логин: ");
            }
            user = ioManager.readLine();
            if (!ioManager.isScriptMode()) {
                ioManager.print("Введите пароль: ");
            }
            password = ioManager.readPassword();
        }
        Request request = new Request(commandName, humanBeingRequest, args, user, password);

        try {
            Validator.validate(info.getArgCount(), info.isRequiresObject(), request);
        } catch (InvalidCommandException e) {
            ioManager.printError(e.getMessage());
            return null;
        }
        return request;
    }
}
