package lab8.client.readers;


import lab8.common.exceptions.InvalidFieldException;
import lab8.common.models.Car;
import lab8.client.managers.IOManager;

/**
 * Класс для создания объекта типа Car
 */


public class CarInputReader {

    private final IOManager ioManager;

    public CarInputReader(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    public Car read() {
        int attempts = 3;
        while (attempts > 0) {
            ioManager.print("Является ли автомобиль крутым? (true/false): ");
            String input = ioManager.readLine();

            if (input == null || input.trim().isEmpty()) {
                ioManager.println("Car не может быть null");
                continue;
            }
            if (input.equalsIgnoreCase("true")) {
                return new Car(true);
            }
            if (input.equalsIgnoreCase("false")) {
                return new Car(false);
            }
            attempts--;
            ioManager.println("Введите true, false: ");
        }
        throw new InvalidFieldException("Превышено количество попыток ввода");
    }
}
