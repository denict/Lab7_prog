package org.example.entity.builders;

import org.example.entity.Address;
import org.example.utility.InputHandler;
import org.example.utility.OutputHandler;

import java.util.function.Predicate;

/**
 * Класс для построения объекта {@link Address} на основе пользовательского ввода.
 */
public class AddressBuilder extends Builder<Address>{
    /**
     * Конструктор для {@code AddressBuilder}.
     *
     * @param consoleInput  обработчик ввода.
     * @param consoleOutput обработчик вывода.
     */
    public AddressBuilder(InputHandler consoleInput, OutputHandler consoleOutput) {
        super(consoleInput, consoleOutput);
    }

    /**
     * Создает новый объект {@link Address}, запрашивая у пользователя данные.
     *
     * @return новый объект {@link Address}.
     */
    @Override
    public Address build() {
        Predicate<String> validateStreet = (street) -> (street != null && !street.isBlank());
        Predicate<String> validateZipCode = (zipCode) -> (true);
        consoleOutput.println("Создание поля Address (типа Address)");
        consoleOutput.println("Создание нового объекта Address");
        String street = askString("Введите поле street", "Последовательность символов String, не может быть пустой", validateStreet, "Неверный формат ввода!");
        String zipCode = askString("Введите поле zipCode", "Последовательность символов String, может быть пустой", validateZipCode, "Неверный формат ввода!");

        return new Address(street, zipCode);
    }
}
