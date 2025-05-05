package org.example.command.commands;

import org.example.command.Command;
import org.example.network.Client;
import org.example.network.Request;
import org.example.network.User;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "history".
 * Описание команды: выводит названия пяти последних выполненных команд.
 */
public class History extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 60L;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;

    public History(ConsoleOutput consoleOutput, Client client) {
        super("history", "выводит названия пяти последних выполненных команд", 0, "");
        this.consoleOutput = consoleOutput;
        this.client = client;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    @Override
    public void execute(String[] args, User user) throws InterruptedException, NullPointerException {
        consoleOutput.println(client.sendRequest(new Request(this, user)).getMessage());
    }

}
