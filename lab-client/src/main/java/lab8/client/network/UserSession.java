package lab8.client.network;

import lab8.common.dto.Request;

public class UserSession {
    private String userName;
    private String password;

    public void login(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public void logout() {
        this.userName = null;
        this.password = null;
    }

    public Request applyTo(Request request) {
        return new Request(request.getCommandName(),
                request.getHumanBeingRequest(),
                request.getArgs(),
                userName,
                password);
    }

}
