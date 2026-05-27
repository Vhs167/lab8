package lab7.client;

import lab7.client.factory.RequestBuilder;
import lab7.common.dto.CommandInfo;
import lab7.common.dto.Request;
import lab7.common.dto.Response;
import lab7.client.managers.IOManager;
import lab7.client.network.DTLSClient;
import lab7.client.network.UserSession;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public final class Client {

    private final IOManager ioManager;
    private final UserSession session;
    private RequestBuilder requestBuilder;
    private Map<String, CommandInfo> commands = new HashMap<>();

    private Client(IOManager ioManager) {
        this.ioManager = ioManager;
        this.session = new UserSession();
    }

    public void start() {
        DTLSClient dtlsClient;

        try {
            dtlsClient = new DTLSClient("localhost", 11111);
        } catch (Exception e) {
            ioManager.printError(e.getMessage());
            return;
        }

        loadCommand(dtlsClient);
        requestBuilder = new RequestBuilder(ioManager, commands);




        while (true) {
            Request request = requestBuilder.buildRequest();

            if (request == null) {
                continue;
            }


            String command = request.getCommandName();

            if (command.equals("exit")) {
                ioManager.println("Завершение работы");
                break;
            }
            if (command.equals("logout")){
                session.logout();
                continue;
            }

            if (command.equals("execute_script")) {
                if(request.getArgs().length == 0){
                    ioManager.printError("Нужно указать имя файла");
                    continue;
                }
                String fileName = request.getArgs()[0];

                if (ioManager.getScripts().contains(fileName)) {
                    ioManager.printError("Ошибка: попытка повторного запуска скрипта " + fileName);
                    continue;
                }

                try {
                    ioManager.setFileInput(fileName);
                    ioManager.println("Выполение скрипта: " + fileName);
                } catch (FileNotFoundException e) {
                    ioManager.println("Файл не найден: " + fileName);
                }

                continue;
            }

            if(command.equals("register") || command.equals("login")){
                Response response = dtlsClient.sendRequest(request);
                if(response != null) {
                    if(response.getMessage().contains("Успешно")){
                        session.login(request.getUserName(), request.getPassword());
                        ioManager.println(response.getMessage());
                    } else if(response.getMessage().contains("Ошибка")) {
                        ioManager.printError(response.getMessage());
                    }
                    else{
                        ioManager.printError(response.getMessage());
                    }
                }
                continue;
            }

            request = session.applyTo(request);

            Response response = dtlsClient.sendRequest(request);

            if (response != null) {
                ioManager.println(response.getMessage());

                if (response.getCollection() != null) {
                    for (Object human : response.getCollection()) {
                        ioManager.println(human);
                    }
                }
            } else {
                ioManager.println("Нет ответа от сервера");
            }
        }
    }

    private void loadCommand(DTLSClient udpClient) {

        Request request = new Request("get_commands", null, new String[0], null, null);

        Response response = udpClient.sendRequest(request);

        if (response == null) {
            ioManager.printError("Сервер не ответил на get_commands");
            return;
        }

        commands.clear();

        if (response.getCollection() == null) {
            ioManager.printError("Сервер вернул пустой список команд");
            return;
        }

        if (response.getCollection() != null) {
            for (Object obj : response.getCollection()) {
                CommandInfo info = (CommandInfo) obj;
                commands.put(info.getName(), info);
            }
        }
    }


    public static void main(String[] args) {
        IOManager ioManager = new IOManager();
        Client client = new Client(ioManager);
        client.start();
    }
}
