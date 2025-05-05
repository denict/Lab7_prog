package org.example.command.commands;

import org.example.command.Command;
import org.example.network.Client;
import org.example.network.Request;
import org.example.network.User;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "filter_by_annual_turnover".
 * Описание команды: вывести элементы, значение поля annualTurnover которых равно заданному.
 */
public class FilterByAnnualTurnover extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 58L;
    transient private ConsoleOutput consoleOutput;
    transient Client client;

    public FilterByAnnualTurnover(ConsoleOutput consoleOutput, Client client) {
        super("filter_by_annual_turnover", "вывести элементы, значение поля annualTurnover которых равно заданному", 1, "\"annualTurnover\"");
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
