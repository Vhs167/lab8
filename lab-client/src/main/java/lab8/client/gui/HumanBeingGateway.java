package lab8.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lab8.client.network.DTLSClient;
import lab8.common.dto.HumanBeingRequest;
import lab8.common.dto.Request;
import lab8.common.dto.Response;
import lab8.common.models.HumanBeing;
import prog.lab8.gui.api.CollectionGateway;
import prog.lab8.gui.api.GatewayResult;

public final class HumanBeingGateway implements CollectionGateway<HumanBeing> {
    private final DTLSClient client;
    private String username;
    private String password;
    private long userId = -1;

    public HumanBeingGateway(DTLSClient client) {
        this.client = client;
    }

    @Override
    public GatewayResult login(String username, String password) {
        Response response = send(new Request("login", null, new String[0], username, password));
        if (isSuccess(response)) {
            this.username = username;
            this.password = password;
            this.userId = readUserId(response);
            return GatewayResult.success(response.getMessage());
        }
        return GatewayResult.error(message(response));
    }

    @Override
    public GatewayResult register(String username, String password) {
        Response response = send(new Request("register", null, new String[0], username, password));
        if (!isSuccess(response)) {
            return GatewayResult.error(message(response));
        }
        return login(username, password);
    }

    @Override
    public List<HumanBeing> loadAll() {
        return humans(execute("show"));
    }

    @Override
    public GatewayResult add(HumanBeing object) {
        return toResult(execute("add", toRequest(object), new String[0]));
    }

    @Override
    public GatewayResult update(long id, HumanBeing object) {
        return toResult(execute("update_by_id", toRequest(object), new String[] {String.valueOf(id)}));
    }

    @Override
    public GatewayResult remove(long id) {
        return toResult(execute("remove_by_id", null, new String[] {String.valueOf(id)}));
    }

    public Response execute(String command, String... args) {
        return execute(command, null, args);
    }

    public Response execute(String command, HumanBeingRequest object, String[] args) {
        return send(new Request(command, object, args, username, password));
    }

    public GatewayResult addIfMin(HumanBeing object) {
        return toResult(execute("add_if_min", toRequest(object), new String[0]));
    }

    public GatewayResult clear() {
        return toResult(execute("clear"));
    }

    public String ownerName(long ownerId) {
        if (ownerId == userId && username != null) {
            return username;
        }
        return "user " + ownerId;
    }

    public GatewayResult toResult(Response response) {
        if (response == null) {
            return GatewayResult.error("Сервер не ответил");
        }
        if (message(response).startsWith("Ошибка")) {
            return GatewayResult.error(message(response));
        }
        return GatewayResult.success(message(response));
    }

    public String text(Response response) {
        if (response == null) {
            return "Сервер не ответил";
        }
        StringBuilder builder = new StringBuilder(message(response));
        List<?> collection = response.getCollection();
        if (collection != null && !collection.isEmpty()) {
            for (Object item : collection) {
                if (builder.length() > 0) {
                    builder.append(System.lineSeparator()).append(System.lineSeparator());
                }
                builder.append(item);
            }
        }
        return builder.toString();
    }

    private Response send(Request request) {
        return client.sendRequest(request);
    }

    private static HumanBeingRequest toRequest(HumanBeing human) {
        HumanBeingRequest request = new HumanBeingRequest();
        request.name = human.getName();
        request.coordinates = human.getCoordinates();
        request.realHero = human.getRealHero();
        request.hasToothpick = human.getHasToothpick();
        request.impactSpeed = human.getImpactSpeed();
        request.soundtrackName = human.getSoundtrackName();
        request.minutesOfWaiting = human.getMinutesOfWaiting();
        request.mood = human.getMood();
        request.car = human.getCar();
        request.userId = human.getUserId();
        return request;
    }

    private static List<HumanBeing> humans(Response response) {
        if (response == null || response.getCollection() == null) {
            return Collections.emptyList();
        }
        List<HumanBeing> result = new ArrayList<>();
        for (Object item : response.getCollection()) {
            if (item instanceof HumanBeing human) {
                result.add(human);
            }
        }
        return result;
    }

    private static boolean isSuccess(Response response) {
        return response != null && response.getMessage() != null && response.getMessage().contains("Успешно");
    }

    private static long readUserId(Response response) {
        if (response.getCollection() == null || response.getCollection().isEmpty()) {
            return -1;
        }
        Object value = response.getCollection().get(0);
        return value instanceof Number number ? number.longValue() : -1;
    }

    private static String message(Response response) {
        if (response == null || response.getMessage() == null) {
            return "";
        }
        return response.getMessage();
    }
}
