package org.example.command.commands;

import org.example.command.Command;
import org.example.network.Client;
import org.example.network.Request;
import org.example.network.User;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "count_by_official_address".
 * Описание команды: вывести количество элементов, у которых значение поля officialAddress равно заданному.
 */
public class CountByOfficialAddress extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 56L;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;

    public CountByOfficialAddress(ConsoleOutput consoleOutput, Client client) {
        super("count_by_official_address", "вывести количество элементов, у которых значение поля officialAddress равно заданному", 2, "\"street\" \"zipCode\"");
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
        consoleOutput.println(client.sendRequest(new Request(this, args, user)).getMessage());
    }


}
