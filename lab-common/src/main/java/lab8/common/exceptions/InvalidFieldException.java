package lab8.common.exceptions;

/**
 * Исключение в случае неправильного заполнения поля
 */

public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }
}
