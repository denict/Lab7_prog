package org.example.entity.builders;

import org.example.network.User;
import org.example.utility.InputHandler;
import org.example.utility.OutputHandler;

public class UserBuilder extends Builder<User> {
    /**
     * Конструктор для {@code UserBuilder}.
     *
     * @param consoleInput  обработчик ввода.
     * @param consoleOutput обработчик вывода.
     */
    public UserBuilder(InputHandler consoleInput, OutputHandler consoleOutput) {
        super(consoleInput, consoleOutput);
    }

    public String askLogin() {
        while (true) {
            consoleOutput.println("Введите логин (не может быть пустым):");
            String login = consoleInput.readLine();
            if (login != null && !login.trim().isEmpty()) {
                return login.trim();
            }
            consoleOutput.printError("Логин не может быть пустым!");
            dropIfFileMode();
        }
    }

    public String askPassword() {
        while (true) {
            consoleOutput.println("Введите пароль (минимум 8 символов, хотя бы 1 заглавная и строчная буквы, цифру и спецсимвол):");
            String password = consoleInput.readLine();
            if (password == null) password = "";
            password = password.trim();
            boolean lengthOk = password.length() > 8;
            boolean hasLower = password.matches(".*[a-z].*");
            boolean hasUpper = password.matches(".*[A-Z].*");
            boolean hasDigit = password.matches(".*\\d.*");
            boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");
            boolean hasSpace = password.matches(".*\\s.*");
            if (!hasSpace && lengthOk && hasLower && hasUpper && hasDigit && hasSpecial) {
                return password;
            }
            consoleOutput.printError("Пароль должен быть длиннее 8 символов и содержать\nхотя бы одну заглавную и строчные буквы, цифру и спецсимвол!");
            dropIfFileMode();
        }
    }



    public User build() {
        consoleOutput.println("Создание нового пользователя");
        consoleOutput.println("Введите логин пользователя");
        String login = askLogin();
        consoleOutput.println("Введите пароль пользователя");
        String password = askPassword();
        return new User(login, password);
    }

}
