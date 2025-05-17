package org.example.command.commands;

import org.example.command.Command;
import org.example.exception.LogoutException;
import org.example.network.User;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Завершает сессию пользователя.
 */
public class Logout extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 200L;
    private transient ConsoleOutput consoleOutput;

    public Logout(ConsoleOutput consoleOutput) {
        super("logout", "выйти из аккаунта", 0, "");
        this.consoleOutput = consoleOutput;
    }


    @Override
    public void execute(String[] args, User user) {
        consoleOutput.println("Вы вышли из аккаунта.");
        throw new LogoutException();
    }


}
