package lab8.client.gui;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import lab8.client.network.DTLSClient;
import lab8.common.dto.Response;
import lab8.common.models.HumanBeing;
import prog.lab8.gui.Lab8Gui;
import prog.lab8.gui.api.CommandAction;
import prog.lab8.gui.api.CommandContext;
import prog.lab8.gui.api.GatewayResult;
import prog.lab8.gui.api.Lab8GuiConfig;

public final class HumanBeingApp extends Application {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private static final String BUNDLE = "lab8.client.gui.i18n.human";
    private static final int RESPONSE_DIALOG_COLUMNS = 72;
    private static final int RESPONSE_DIALOG_ROWS = 16;

    @Override
    public void start(Stage stage) {
        DTLSClient client = buildClient();
        if (client == null) {
            return;
        }
        HumanBeingGateway gateway = new HumanBeingGateway(client);
        HumanBeingEditor editor = new HumanBeingEditor();
        Lab8GuiConfig<HumanBeing> config =
                Lab8GuiConfig.builder(gateway, new HumanBeingDescriptor(gateway), editor)
                        .applicationTitle("app.title", "Human Being Manager")
                        .collectionItemName("collection.item.humanBeing", "HumanBeing")
                        .resourceBundleBaseName(BUNDLE)
                        .commands(commands(gateway, editor))
                        .build();
        new Lab8Gui().start(stage, config);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private DTLSClient buildClient() {
        List<String> raw = getParameters().getRaw();
        String host = raw.isEmpty() ? DEFAULT_HOST : raw.get(0);
        int port = raw.size() > 1 ? Integer.parseInt(raw.get(1)) : DEFAULT_PORT;
        try {
            return new DTLSClient(host, port);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Не удалось запустить GUI-клиент: " + e.getMessage());
            alert.showAndWait();
            return null;
        }
    }

    private static List<CommandAction<HumanBeing>> commands(
            HumanBeingGateway gateway, HumanBeingEditor editor) {
        return List.of(
                serverOutput("show", "command.show", "show", gateway, false),
                serverOutput("help", "command.help", "help", gateway, false),
                serverOutput("info", "command.info", "info", gateway, false),
                clearCommand(gateway),
                addIfMinCommand(gateway, editor),
                numericCommand("remove_greater", "command.removeGreater", "dialog.impactSpeed", gateway, true),
                numericCommand("remove_lower", "command.removeLower", "dialog.impactSpeed", gateway, true),
                serverOutput("group_by_real_hero", "command.groupByRealHero", "group_by_real_hero", gateway, false),
                numericCommand("count_by_impact_speed", "command.countByImpactSpeed", "dialog.impactSpeed", gateway, false),
                textCommand(
                        "filter_greater_then_soundtrack_name",
                        "command.filterGreaterSoundtrack",
                        "dialog.soundtrackName",
                        gateway,
                        false));
    }

    private static CommandAction<HumanBeing> serverOutput(
            String key,
            String labelKey,
            String command,
            HumanBeingGateway gateway,
            boolean refreshAfter) {
        return new CommandAction<>(
                key,
                labelKey,
                false,
                refreshAfter,
                context -> showResponse(context, labelKey, gateway.execute(command)));
    }

    private static CommandAction<HumanBeing> clearCommand(HumanBeingGateway gateway) {
        return new CommandAction<>(
                "clear",
                "command.clear",
                false,
                true,
                context -> {
                    if (!confirm(context, "command.clear", "dialog.confirmClear")) {
                        return GatewayResult.success(context.localization().message("status.cancelled"));
                    }
                    return showGatewayResult(context, "command.clear", gateway.clear());
                });
    }

    private static CommandAction<HumanBeing> addIfMinCommand(
            HumanBeingGateway gateway, HumanBeingEditor editor) {
        return new CommandAction<>(
                "add_if_min",
                "command.addIfMin",
                false,
                true,
                context -> {
                    Optional<HumanBeing> human =
                            callOnFxThread(() -> editor.create(context.owner(), context.localization()));
                    if (human.isEmpty()) {
                        return GatewayResult.success(context.localization().message("status.cancelled"));
                    }
                    return showGatewayResult(context, "command.addIfMin", gateway.addIfMin(human.get()));
                });
    }

    private static CommandAction<HumanBeing> numericCommand(
            String command,
            String labelKey,
            String promptKey,
            HumanBeingGateway gateway,
            boolean refreshAfter) {
        return new CommandAction<>(
                command,
                labelKey,
                false,
                refreshAfter,
                context -> {
                    Optional<String> value = prompt(context, labelKey, promptKey);
                    if (value.isEmpty()) {
                        return GatewayResult.success(context.localization().message("status.cancelled"));
                    }
                    Response response = gateway.execute(command, value.get().trim().replace(',', '.'));
                    return showResponse(context, labelKey, response);
                });
    }

    private static CommandAction<HumanBeing> textCommand(
            String command,
            String labelKey,
            String promptKey,
            HumanBeingGateway gateway,
            boolean refreshAfter) {
        return new CommandAction<>(
                command,
                labelKey,
                false,
                refreshAfter,
                context -> {
                    Optional<String> value = prompt(context, labelKey, promptKey);
                    if (value.isEmpty() || value.get().isBlank()) {
                        return GatewayResult.success(context.localization().message("status.cancelled"));
                    }
                    Response response = gateway.execute(command, value.get().trim());
                    return showResponse(context, labelKey, response);
                });
    }

    private static GatewayResult showResponse(
            CommandContext<HumanBeing> context, String titleKey, Response response) throws Exception {
        if (response == null) {
            return GatewayResult.error("Сервер не ответил");
        }
        if (response != null && response.getMessage() != null && response.getMessage().startsWith("Ошибка")) {
            return GatewayResult.error(response.getMessage());
        }
        showText(context, titleKey, responseText(response));
        return GatewayResult.success("");
    }

    private static GatewayResult showGatewayResult(
            CommandContext<HumanBeing> context, String titleKey, GatewayResult result) throws Exception {
        if (!result.success()) {
            return result;
        }
        showText(context, titleKey, result.message());
        return GatewayResult.success("");
    }

    private static String responseText(Response response) {
        StringBuilder text = new StringBuilder(response.getMessage() == null ? "" : response.getMessage());
        if (response.getCollection() != null) {
            for (Object item : response.getCollection()) {
                if (text.length() > 0) {
                    text.append(System.lineSeparator()).append(System.lineSeparator());
                }
                text.append(item);
            }
        }
        return text.toString();
    }

    private static void showText(CommandContext<HumanBeing> context, String titleKey, String message)
            throws Exception {
        callOnFxThread(
                () -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(context.owner());
                    alert.setTitle(context.localization().message(titleKey, titleKey));
                    alert.setHeaderText(null);
                    TextArea text = new TextArea(message == null || message.isBlank() ? "-" : message);
                    text.setEditable(false);
                    text.setWrapText(true);
                    text.setPrefColumnCount(RESPONSE_DIALOG_COLUMNS);
                    text.setPrefRowCount(RESPONSE_DIALOG_ROWS);
                    alert.getDialogPane().setContent(text);
                    alert.showAndWait();
                    return null;
                });
    }

    private static Optional<String> prompt(
            CommandContext<HumanBeing> context, String titleKey, String promptKey) throws Exception {
        return callOnFxThread(
                () -> {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.initOwner(context.owner());
                    dialog.setTitle(context.localization().message(titleKey, titleKey));
                    dialog.setHeaderText(null);
                    dialog.setContentText(context.localization().message(promptKey, promptKey));
                    return dialog.showAndWait();
                });
    }

    private static boolean confirm(
            CommandContext<HumanBeing> context, String titleKey, String messageKey) throws Exception {
        return callOnFxThread(
                () -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.initOwner(context.owner());
                    alert.setTitle(context.localization().message(titleKey, titleKey));
                    alert.setHeaderText(null);
                    alert.setContentText(context.localization().message(messageKey, messageKey));
                    return alert.showAndWait().filter(ButtonType.OK::equals).isPresent();
                });
    }

    private static <T> T callOnFxThread(Callable<T> action) throws Exception {
        if (Platform.isFxApplicationThread()) {
            return action.call();
        }
        FutureTask<T> task = new FutureTask<>(action);
        Platform.runLater(task);
        try {
            return task.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception exception) {
                throw exception;
            }
            if (cause instanceof Error error) {
                throw error;
            }
            throw new IllegalStateException(cause);
        }
    }
}
