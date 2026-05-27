package lab7.client.readers;

import lab7.common.dto.HumanBeingRequest;
import lab7.common.exceptions.InvalidFieldException;
import lab7.common.models.Mood;
import lab7.client.managers.IOManager;

/**
 * Класс для создания объекта типа HumanBeing
 */


public class HumanBeingInputReader {

    private final IOManager ioManager;
    private final CoordinatesInputReader coordinatesInputReader;
    private final CarInputReader carInputReader;

    public HumanBeingInputReader(IOManager ioManager) {
        this.ioManager = ioManager;
        this.coordinatesInputReader = new CoordinatesInputReader(ioManager);
        this.carInputReader = new CarInputReader(ioManager);
    }


    public HumanBeingRequest read() {
        HumanBeingRequest request = new HumanBeingRequest();
        try {
            request.name = readName();
            request.coordinates = coordinatesInputReader.read();
            request.realHero = readRealHero();
            request.hasToothpick = readHasToothpick();
            request.impactSpeed = readImpactSpeed();
            request.soundtrackName = readSoundtrackName();
            request.minutesOfWaiting = readMinutesOfWaiting();
            request.mood = readMood();
            request.car = carInputReader.read();

        } catch (InvalidFieldException e) {
            ioManager.printError("Ошибка при создании объекта: " + e.getMessage());
        }
        return request;
    }

    private String readName() throws InvalidFieldException {
        int attempts = 3;

        while (attempts > 0) {
            ioManager.print("Введите name: ");
            String input = ioManager.readLine();

            if (input == null) {
                throw new InvalidFieldException("Выход из цикла");
            }

            if (input.trim().isEmpty()) {
                ioManager.println("Имя не может быть пустым. Попробуйте снова. ");
                attempts--;
                continue;
            }

            return input.trim();
        }
        throw new InvalidFieldException("Превышено количество попыток ввода");
    }

    private Boolean readRealHero() throws InvalidFieldException {
        int attempts = 3;

        while (attempts > 0) {
            ioManager.print("Является ли персонаж реальным героем? (true/false): ");
            String input = ioManager.readLine();
            if (input == null) {
                throw new InvalidFieldException("Выход из цикла");
            }

            if ("true".equalsIgnoreCase(input)) return true;

            if ("false".equalsIgnoreCase(input)) return false;

            attempts--;
            ioManager.print("Введите true или false: ");

        }
        throw new InvalidFieldException("Превышено количество попыток ввода ");
    }

    private boolean readHasToothpick() throws InvalidFieldException {
        int attempts = 3;

        while (attempts > 0) {
            ioManager.print("Есть ли зубочистка? (true/false): ");
            String input = ioManager.readLine();
            if (input == null) {
                throw new InvalidFieldException("Выход из цикла");
            }

            if ("true".equalsIgnoreCase(input)) return true;
            if ("false".equalsIgnoreCase(input)) return false;

            attempts--;
            ioManager.print("Введите true или false: ");

        }
        throw new InvalidFieldException("Превышено количество попыток ввода ");
    }

    private double readImpactSpeed() throws InvalidFieldException {
        int attempts = 3;
        while (attempts > 0) {
            ioManager.print("Введите impactSpeed (double): ");
            String input = ioManager.readLine();
            if (input == null) {
                throw new InvalidFieldException("Выход из цикла");
            }
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                attempts--;
                ioManager.print("impactSpeed должен быть числом. Попробуйте снова: ");
            }

        }
        throw new InvalidFieldException("Превышено количество попыток ввода ");
    }

    private String readSoundtrackName() throws InvalidFieldException {
        int attempts = 3;
        while (attempts > 0) {
            ioManager.print("Введите soundtrackName: ");
            String input = ioManager.readLine();

            if (input == null) {
                throw new InvalidFieldException("Выход из цикла");
            }

            if (input.trim().isEmpty()) {
                attempts--;
                ioManager.print("soundtrackName не может быть пустым. Попробуйте снова: ");
                continue;
            }

            return input.trim();
        }
        throw new InvalidFieldException("Превышено количество попыток ввода ");
    }

    private double readMinutesOfWaiting() throws InvalidFieldException {
        int attempts = 3;
        while (attempts > 0) {
            ioManager.print("Введите minutesOfWaiting (double): ");
            String input = ioManager.readLine();
            if (input == null) {
                throw new InvalidFieldException("Выход из цикла");
            }
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                attempts--;
                ioManager.print("minutesOfWaiting должен быть числом. Попробуйте снова: ");
            }
        }
        throw new InvalidFieldException("Превышено количество попыток ввода ");
    }

    private Mood readMood() {
        int attempts = 3;
        while (attempts > 0) {
            ioManager.print("Введите mood (SORROW, LONGING, GLOOM, APATHY, CALM) или оставьте пустым: ");
            String input = ioManager.readLine();
            if (input == null || input.trim().isEmpty()) return null;
            try {
                return Mood.valueOf(input.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                ioManager.print("Неверное значение mood. Попробуйте снова: ");
                attempts--;
            }
        }
        throw new InvalidFieldException("Превышено количество попыток ввода ");
    }
}