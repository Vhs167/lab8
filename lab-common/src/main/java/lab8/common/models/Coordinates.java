package lab8.common.models;

import lab8.common.exceptions.InvalidFieldException;

import java.io.Serializable;

/**
 * Класс координат
 */

public class Coordinates implements Serializable {
    private Integer x; //Значение поля должно быть больше -213, Поле не может быть null
    private Float y; //Поле не может быть null

    public Coordinates(Integer x, Float y) {
        setX(x);
        setY(y);
    }

    private void setX(Integer x) {
        if (x == null) {
            throw new InvalidFieldException("x не может быть null");
        }
        if (x <= -213) {
            throw new InvalidFieldException("x должен быть больше -213");
        }
        this.x = x;
    }

    private void setY(Float y) {
        if (y == null) {
            throw new InvalidFieldException("y не может быть null");
        }
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String toString() {
        return "x=" + x + " y=" + y;
    }
}
