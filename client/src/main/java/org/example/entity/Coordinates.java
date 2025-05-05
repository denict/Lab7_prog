package org.example.entity;

import java.io.Serializable;

/**
 * Модель координат.
 * Координаты представлены двумя значениями: x и y.
 */
public class Coordinates implements Serializable {
    private long x;
    private long y;

    public Coordinates(long x, long y) {
        this.x = x;
        this.y = y;
    }
    // Геттеры и сеттеры
    public long getX() {
        return x;
    }
    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }
    public void setY(long y) {
        this.y = y;
    }

    /**
     * Возвращает строковое представление объекта координат.
     *
     * @return строка в формате "x = [значение x]; y = [значение y]"
     */
    @Override
    public String toString() {
        return "x = "+x+"; "+ "y = "+y;
    }

}