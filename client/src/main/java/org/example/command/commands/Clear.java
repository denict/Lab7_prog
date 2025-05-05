package org.example.command.commands;

import org.example.command.Command;
import org.example.network.Client;
import org.example.network.Request;
import org.example.network.User;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "clear".
 * Описание команды: очистить коллекцию.
 */
public class Clear extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 55L;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;

    public Clear(ConsoleOutput consoleOutput, Client client) {
        super("clear", "очистить коллекцию", 0, "");
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
