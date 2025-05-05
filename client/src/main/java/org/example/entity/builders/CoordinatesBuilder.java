package org.example.entity.builders;

import org.example.entity.Coordinates;
import org.example.utility.InputHandler;
import org.example.utility.OutputHandler;

import java.util.function.Predicate;

/**
 * Класс для построения объекта {@link Coordinates} на основе пользовательского ввода.
 */
public class CoordinatesBuilder extends Builder<Coordinates> {

    /**
     * Конструктор для {@code CoordinatesBuilder}.
     *
     * @param consoleInput  обработчик ввода.
     * @param consoleOutput обработчик вывода.
     */
    public CoordinatesBuilder(InputHandler consoleInput, OutputHandler consoleOutput) {
        super(consoleInput, consoleOutput);
    }

    /**
     * Создает новый объект {@link Coordinates}, запрашивая у пользователя данные.
     *
     * @return новый объект {@link Coordinates}.
     */
    @Override
    public Coordinates build() {
        Predicate<Long> validateX = (x) -> (true);
        Predicate<Long> validateY = (y) -> (true);
        consoleOutput.println("Создание нового объекта Coordinates");

        Long x = askLong("Координата x", "Целочисленное число типа Long", validateX, "Неверный формат ввода!");
        Long y = askLong("Координата y", "Целочисленное число типа Long", validateY, "Неверный формат ввода!");
        return new Coordinates(x, y);
    }
}
