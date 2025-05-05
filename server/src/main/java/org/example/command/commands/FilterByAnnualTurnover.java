package org.example.command.commands;

import org.example.command.Command;
import org.example.entity.Organization;
import org.example.managers.CollectionManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Команда "filter_by_annual_turnover".
 * Описание команды: вывести элементы, значение поля annualTurnover которых равно заданному.
 */
public class FilterByAnnualTurnover extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 58L;
    transient private final CollectionManager collectionManager;

    public FilterByAnnualTurnover(CollectionManager collectionManager) {
        super("filter_by_annual_turnover", "вывести элементы, значение поля annualTurnover которых равно заданному", 1, "\"annualTurnover\"");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    @Override
    public Response execute(Request request) {
        try {
        Double annualTurnover = Double.parseDouble(((String[])request.getArgs())[0]);
        if (CollectionManager.getCollection() == null) {
            return new Response(false, "Коллекция пустая! Элементов с фиксированным annualTurnover нет!");
        }


            // StreamAPI
            List<Organization> filtered = collectionManager.getCollection().stream().filter(org -> org.getAnnualTurnover().equals(annualTurnover)).toList();
            int count = 1;
            StringBuilder sb = new StringBuilder("");
            for (Organization org : filtered) {
                sb.append(count++ + ": " + org.toString() + "\n");
            }
            if (filtered.isEmpty()) {
                return new Response(false, "Нет элементов, значение поля annualTurnover которых равно " + annualTurnover);
//                consoleOutput.println("Нет элементов, значение поля annualTurnover которых равно " + annualTurnover);
            }
            return new Response(true, sb.toString());
        } catch (NumberFormatException e) {
//            consoleOutput.printError("Введите число типа Double");
            return new Response(false, "Введите число типа Double");
        }
    }
}
