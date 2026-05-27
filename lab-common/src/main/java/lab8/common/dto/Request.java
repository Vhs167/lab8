package lab8.common.dto;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 3L;

    private final String commandName;
    private final HumanBeingRequest humanBeingRequest;
    private final String[] args;

    private String userName;
    private String password;

    public Request(String commandName, HumanBeingRequest humanBeingRequest,
                   String[] args, String userName, String password) {
        this.commandName = commandName;
        this.humanBeingRequest = humanBeingRequest;
        this.args = args;
        this.userName = userName;
        this.password = password;
    }
    public Request(String commandName, HumanBeingRequest humanBeingRequest,
                   String[] args) {
        this.commandName = commandName;
        this.humanBeingRequest = humanBeingRequest;
        this.args = args;
        this.userName = null;
        this.password = null;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public String getCommandName() {
        return commandName;
    }

    public HumanBeingRequest getHumanBeingRequest() {
        return humanBeingRequest;
    }

    public String[] getArgs() {
        return args;
    }
}
