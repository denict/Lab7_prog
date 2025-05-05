package org.example.command.commands;

import org.example.command.Command;
import org.example.network.Client;
import org.example.network.Request;
import org.example.network.User;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "show".
 * Описание команды: выводит в стандартный поток вывода все элементы коллекции в строковом представлении
 */
public class Show extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 67L;
    private transient ConsoleOutput consoleOutput;
    private transient Client client;

    public Show(ConsoleOutput consoleOutput, Client client) {
        super("show", "выводит в стандартный поток вывода все элементы коллекции в строковом представлении", 0, "");
        this.consoleOutput = consoleOutput;
        this.client = client;
    }

    @Override
    public void execute(String[] args, User user) throws InterruptedException, NullPointerException {
        consoleOutput.println(client.sendRequest(new Request(this, user)).getMessage());
    }
}
