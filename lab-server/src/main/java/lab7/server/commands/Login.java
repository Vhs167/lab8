package lab7.server.commands;

import lab7.common.commands.CommandType;
import lab7.common.dto.Request;
import lab7.common.dto.Response;
import lab7.server.auth.AuthService;

import java.util.Collections;


public class Login extends AbstractCommand implements Command {
    private final AuthService authService;

    public Login(AuthService authService) {
        super("login", CommandType.NO_ARG_WITH_AUTH, "команда для запуска процесса авторизации");
        this.authService = authService;
    }

    public Response execute(Request request, Long userId) {
        Long id = authService.login(request.getUserName(), request.getPassword());
        if (id == null) {
            return new Response(Collections.emptyList(), "Ошибка");
        }
        return new Response(Collections.emptyList(), "Успешно");
    }
}
