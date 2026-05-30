package lab8.client.gui;

import java.time.LocalDateTime;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import lab8.common.models.Car;
import lab8.common.models.Coordinates;
import lab8.common.models.HumanBeing;
import lab8.common.models.Mood;
import prog.lab8.gui.api.ObjectEditor;
import prog.lab8.gui.i18n.Localization;

public final class HumanBeingEditor implements ObjectEditor<HumanBeing> {

    @Override
    public Optional<HumanBeing> create(Window owner, Localization localization) {
        return show(owner, localization, "dialog.addTitle", null);
    }

    @Override
    public Optional<HumanBeing> edit(Window owner, Localization localization, HumanBeing object) {
        return show(owner, localization, "dialog.editTitle", object);
    }

    private Optional<HumanBeing> show(Window owner, Localization loc, String titleKey, HumanBeing existing) {
        Form form = new Form();
        if (existing != null) {
            form.fill(existing);
        }
        Dialog<HumanBeing> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle(loc.message(titleKey, titleKey));
        dialog.getDialogPane().setContent(form.grid(loc));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(button -> button == ButtonType.OK ? safeRead(form, loc, existing) : null);
        return dialog.showAndWait();
    }

    private static HumanBeing safeRead(Form form, Localization loc, HumanBeing existing) {
        try {
            return form.toHuman(existing);
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(loc.message("dialog.warning", "Warning"));
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return null;
        }
    }

    private static final class Form {
        private static final double HGAP = 10;
        private static final double VGAP = 8;

        private final TextField name = new TextField();
        private final TextField coordinateX = new TextField();
        private final TextField coordinateY = new TextField();
        private final CheckBox realHero = new CheckBox();
        private final CheckBox hasToothpick = new CheckBox();
        private final TextField impactSpeed = new TextField();
        private final TextField soundtrackName = new TextField();
        private final TextField minutesOfWaiting = new TextField();
        private final ComboBox<String> mood = new ComboBox<>();
        private final ComboBox<String> carCool = new ComboBox<>();

        private Form() {
            mood.getItems().add("");
            for (Mood value : Mood.values()) {
                mood.getItems().add(value.name());
            }
            carCool.getItems().addAll("", "true", "false");
            carCool.setValue("");
            mood.setValue(Mood.CALM.name());
        }

        private GridPane grid(Localization loc) {
            GridPane grid = new GridPane();
            grid.setHgap(HGAP);
            grid.setVgap(VGAP);
            int row = 0;
            row = addRow(grid, loc, "field.name", name, row);
            row = addRow(grid, loc, "field.x", coordinateX, row);
            row = addRow(grid, loc, "field.y", coordinateY, row);
            row = addRow(grid, loc, "field.realHero", realHero, row);
            row = addRow(grid, loc, "field.hasToothpick", hasToothpick, row);
            row = addRow(grid, loc, "field.impactSpeed", impactSpeed, row);
            row = addRow(grid, loc, "field.soundtrackName", soundtrackName, row);
            row = addRow(grid, loc, "field.minutesOfWaiting", minutesOfWaiting, row);
            row = addRow(grid, loc, "field.mood", mood, row);
            addRow(grid, loc, "field.carCool", carCool, row);
            return grid;
        }

        private static int addRow(GridPane grid, Localization loc, String key, Node control, int row) {
            grid.add(new Label(loc.message(key, key)), 0, row);
            grid.add(control, 1, row);
            return row + 1;
        }

        private void fill(HumanBeing human) {
            name.setText(human.getName());
            coordinateX.setText(String.valueOf(human.getCoordinates().getX()));
            coordinateY.setText(String.valueOf(human.getCoordinates().getY()));
            realHero.setSelected(human.getRealHero());
            hasToothpick.setSelected(human.getHasToothpick());
            impactSpeed.setText(String.valueOf(human.getImpactSpeed()));
            soundtrackName.setText(human.getSoundtrackName());
            minutesOfWaiting.setText(String.valueOf(human.getMinutesOfWaiting()));
            mood.setValue(human.getMood() == null ? "" : human.getMood().name());
            Boolean cool = human.getCar().getCool();
            carCool.setValue(cool == null ? "" : String.valueOf(cool));
        }

        private HumanBeing toHuman(HumanBeing existing) {
            Coordinates coordinates = new Coordinates(parseInt(coordinateX), (float) parseDouble(coordinateY));
            Car car = new Car(readCarCool());
            if (existing == null) {
                return new HumanBeing(
                        text(name),
                        coordinates,
                        realHero.isSelected(),
                        hasToothpick.isSelected(),
                        parseDouble(impactSpeed),
                        text(soundtrackName),
                        parseDouble(minutesOfWaiting),
                        readMood(),
                        car,
                        0);
            }
            return new HumanBeing(
                    existing.getId(),
                    text(name),
                    coordinates,
                    creationDate(existing),
                    realHero.isSelected(),
                    hasToothpick.isSelected(),
                    parseDouble(impactSpeed),
                    text(soundtrackName),
                    parseDouble(minutesOfWaiting),
                    readMood(),
                    car,
                    existing.getUserId());
        }

        private Boolean readCarCool() {
            String value = carCool.getValue();
            return value == null || value.isBlank() ? null : Boolean.parseBoolean(value);
        }

        private Mood readMood() {
            String value = mood.getValue();
            return value == null || value.isBlank() ? null : Mood.valueOf(value);
        }

        private static LocalDateTime creationDate(HumanBeing existing) {
            return existing.getCreationDate() == null ? LocalDateTime.now() : existing.getCreationDate();
        }

        private static String text(TextField field) {
            String value = field.getText();
            return value == null || value.isBlank() ? null : value.trim();
        }

        private static int parseInt(TextField field) {
            try {
                return Integer.parseInt(field.getText().trim());
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Некорректное целое: " + field.getText());
            }
        }

        private static double parseDouble(TextField field) {
            try {
                return Double.parseDouble(field.getText().trim().replace(',', '.'));
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Некорректное число: " + field.getText());
            }
        }
    }
}
