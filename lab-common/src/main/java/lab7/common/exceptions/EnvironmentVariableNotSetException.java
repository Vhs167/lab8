package lab7.common.exceptions;

/**
 * Исключение в случае, если не указана переменная окружения
 */

public class EnvironmentVariableNotSetException extends RuntimeException {
    public EnvironmentVariableNotSetException(String message) {
        super(message);
    }
}
