package lab7.common.utils;


import lab7.common.dto.HumanBeingRequest;
import lab7.common.dto.Request;
import lab7.common.exceptions.InvalidCommandException;
import lab7.common.models.HumanBeing;

import java.util.Collection;


public class Validator {

    public static void validate
            (int expectedArgs,
             boolean requiresObject,
             Request request) throws InvalidCommandException {

        String[] args = request.getArgs();
        HumanBeingRequest human = request.getHumanBeingRequest();
        if (args == null) {
            if (expectedArgs != 0) {
                throw new InvalidCommandException("Ожидалось аргументов: " + expectedArgs);
            }
        } else if (expectedArgs != args.length) {
            throw new InvalidCommandException("Ожидалось аргументов: " + expectedArgs +
                    " Получено: " + args.length);
        }

        if (requiresObject && human == null) {
            throw new InvalidCommandException("Требуется объект");
        }

        if (!requiresObject && human != null) {
            throw new InvalidCommandException("Объект не требуется");
        }
    }
    public static void validateId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id должен быть больше 0");
        }
    }

    public static void validateExists(HumanBeing human) {
        if (human == null) {
            throw new IllegalArgumentException("Элемент с таким id не найден");
        }
    }

    public static void validateIsEmpty(Collection<HumanBeing> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalStateException("Коллекция пуста");
        }
    }

    public static double validateDoubleArg(String[] args, String commandName) {
        try {
            return Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Аргумент команды " + commandName + " должен быть double");
        }
    }

    public static long validateLongArg(String[] args, String commandName) {
        try {
            return Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Аргумент команды " + commandName + " должен быть long");
        }
    }
}
