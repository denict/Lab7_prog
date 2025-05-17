package org.example.entity.builders;

import org.example.managers.RunnerScriptManager;
import org.example.utility.ConsoleInput;
import org.example.utility.InputHandler;
import org.example.utility.OutputHandler;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Абстрактный класс Builder, представляющий шаблон для создания объектов типа T.
 * Позволяет запрашивать у пользователя ввод различных типов данных с валидацией.
 *
 * @param <T> тип создаваемого объекта
 */
public abstract class Builder<T> {
    protected final InputHandler consoleInput;
    protected final OutputHandler consoleOutput;

    /**
     * Конструктор для инициализации обработчиков ввода и вывода.
     * @param consoleInput обработчик ввода
     * @param consoleOutput обработчик вывода
     */
    public Builder(InputHandler consoleInput, OutputHandler consoleOutput) {
        this.consoleInput = ConsoleInput.isFileMode() ? new RunnerScriptManager() : consoleInput;
        this.consoleOutput = consoleOutput;
    }


    /**
     * Создает объект типа T на основе введенных данных.
     * @return построенный объект
     */
    public abstract T build();


    /**
     * Запрашивает строковое значение у пользователя с валидацией.
     * @param valueName имя параметра
     * @param valueInfo описание параметра
     * @param validateRule правило валидации
     * @param errorMessage сообщение об ошибке
     * @return валидированное строковое значение
     */
    public String askString(String valueName, String valueInfo, Predicate<String> validateRule, String errorMessage) {
        while (true) {
            consoleOutput.print(valueName + " (" + valueInfo + ")\n> ");
            String value = consoleInput.readLine();
            if (value != null && value.isBlank()) value = value.trim();
            if (validateRule.test(value)) return value;
            consoleOutput.printError("Введенное значение не удовлетворяет одному или нескольким условиям валидации поля \"" + valueName + "\". " + errorMessage);
            dropIfFileMode();
        }
    }


    /**
     * Запрашивает у пользователя значение из Enum с валидацией.
     * @param valueName имя параметра
     * @param valueInfo описание параметра
     * @param enumClass класс перечисления
     * @param validateRule правило валидации
     * @param errorMessage сообщение об ошибке
     * @return валидированное значение из Enum или null, если ввод пустой
     */
    public <T extends Enum<T>> T askEnum(String valueName, String valueInfo, Class<T> enumClass, Predicate<T> validateRule, String errorMessage) {
        while (true) {
            consoleOutput.print(valueName + " (" + valueInfo + ")\n" + Arrays.toString(enumClass.getEnumConstants()) + "\n> ");
            try {
                String input = consoleInput.readLine().trim();
                if (input.isBlank()) return null;
                T value = Enum.valueOf(enumClass, input.toUpperCase());
                if (validateRule.test(value)) return value;
                consoleOutput.printError("Введенное значение не удовлетворяет одному или нескольким условиям валидации поля \"" + valueName + "\". " + errorMessage);

            }
            catch (IllegalArgumentException e) {
                consoleOutput.printError("Выберите одно из корректных значений из Enum'а!");
                dropIfFileMode();
            }
        }
    }


    /**
     * Запрашивает у пользователя число с плавающей точкой Double с валидацией.
     * @param valueName имя параметра
     * @param valueInfo описание параметра
     * @param validateRule правило валидации
     * @param errorMessage сообщение об ошибке
     * @return валидированное значение Double
     */
    public Double askDouble(String valueName, String valueInfo, Predicate<Double> validateRule, String errorMessage) {
        while (true) {
            consoleOutput.print(valueName + " (" + valueInfo + ")\n> ");
            try {
                String input = consoleInput.readLine();
                if (input != null) input = input.trim();
                if (input.isBlank()) throw new NumberFormatException();
                Double value = Double.parseDouble(input);
                if (validateRule.test(value)) return value;
                consoleOutput.printError("Введенное значение не удовлетворяет одному или нескольким условиям валидации поля \"" + valueName + "\". " + errorMessage);
                dropIfFileMode();
            }
            catch (NumberFormatException e) {
                consoleOutput.printError("Введите число с плавающей точкой типа Double!");
                dropIfFileMode();
            }
        }
    }

    /**
     * Запрашивает у пользователя целочисленное значение Long с валидацией.
     * @param valueName имя параметра
     * @param valueInfo описание параметра
     * @param validateRule правило валидации
     * @param errorMessage сообщение об ошибке
     * @return валидированное значение Long
     */
    public Long askLong(String valueName, String valueInfo, Predicate<Long> validateRule, String errorMessage) {
        while (true) {
            consoleOutput.print(valueName + " (" + valueInfo + ")\n> ");
            try {
                String input = consoleInput.readLine();
                if (input != null) input = input.trim();
                if (input.isBlank()) throw new NumberFormatException();
                Long value = Long.parseLong(input);
                if (validateRule.test(value)) return value;
                consoleOutput.printError("Введенное значение не удовлетворяет одному или нескольким условиям валидации поля \"" + valueName + "\". " + errorMessage);
            }
            catch (NumberFormatException e) {
                consoleOutput.printError("Введите целочисленное число типа Long!");
                dropIfFileMode();
            }
        }
    }



    /**
     * Завершает программу в режиме чтения из файла при ошибке ввода.
     */
    protected void dropIfFileMode() {
        if (ConsoleInput.isFileMode()) {
            consoleOutput.printError("Неверный ввод. Завершение программы");
            System.exit(-1);
        }
    }

}


