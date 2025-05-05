package org.example.command.commands;

import org.example.command.Command;
import org.example.managers.CollectionManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "print_unique_annual_turnover".
 * Описание команды: вывести уникальные значения поля annualTurnover всех элементов в коллекции
 */
public class PrintUniqueAnnualTurnover extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 62L;
    private CollectionManager collectionManager;
    private ConsoleOutput consoleOutput;

    public PrintUniqueAnnualTurnover(CollectionManager collectionManager, ConsoleOutput consoleOutput) {
        super("print_unique_annual_turnover", "вывести уникальные значения поля annualTurnover всех элементов в коллекции", 0, "");
        this.collectionManager = collectionManager;
        this.consoleOutput = consoleOutput;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection() == null) {
            return new Response(false, "Коллекция пустая! Элементов с уникальными \"annualTurnover\" нет!");

        }
        var sb = new StringBuilder("");
        sb.append("Уникальные значения annualTurnover элементов коллекции: ");
        // Stream API
        collectionManager.getCollection().stream().map(org -> org.getAnnualTurnover()
        ).distinct().forEach(d -> sb.append(d + "; "));
        return new Response(true, sb.toString());
        }
    }
