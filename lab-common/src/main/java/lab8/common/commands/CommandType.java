package lab8.common.commands;

public enum CommandType {
    ONE_ARG(1, false, false),
    NO_ARG_WITH_AUTH(0,false, true),
    NO_ARG(0, false, false),
    WITH_OBJECT(0, true, false),
    ONE_ARG_WITH_OBJECT(1, true, false);


    private final int argsCount;
    private final boolean requiresObject;
    private final boolean requiresAuthorization;

    CommandType(int argsCount, boolean requiresObject, boolean requiresAuthorization) {
        this.argsCount = argsCount;
        this.requiresObject = requiresObject;
        this.requiresAuthorization = requiresAuthorization;
    }


    public int getArgsCount() {
        return argsCount;
    }

    public boolean getRequiresObject() {
        return requiresObject;
    }

    public boolean getRequiresAuthorization() { return requiresAuthorization;}

}
