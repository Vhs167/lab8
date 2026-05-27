package lab7.client.readers;

import lab7.common.exceptions.InvalidFieldException;
import lab7.common.models.Coordinates;
import lab7.client.managers.IOManager;

/**
 * Класс для создания объекта типа Coordinate
 */

public class CoordinatesInputReader {

    private final IOManager ioManager;

    public CoordinatesInputReader(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    public Coordinates read() throws InvalidFieldException {
        int attempts = 3;
        while (attempts > 0) {
            try {
                ioManager.print("Введите координату x (>-213): ");
                String inputX = ioManager.readLine();
                if (inputX == null) {
                    throw new InvalidFieldException("Выход из цикла");
                }
                int x = Integer.parseInt(inputX);
                if (x <= -213) {
                    attempts--;
                    ioManager.print("x должен быть больше -213. Попробуйте снова.");
                    continue;
                }

                ioManager.print("Введите координату y: ");
                String inputY = ioManager.readLine();
                if (inputY == null) {
                    throw new InvalidFieldException("Выход из цикла");
                }
                float y = Float.parseFloat(inputY);

                return new Coordinates(x, y);
            } catch (NumberFormatException e) {
                attempts--;
                ioManager.print("Координаты должны быть числами. Попробуйте снова: ");
            }
        }
        throw new InvalidFieldException("Превышено количество попыток ввода");
    }

}
