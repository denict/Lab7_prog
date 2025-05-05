package org.example.command.commands;

import org.example.command.Command;
import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;
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
    transient private final CollectionManager collectionManager;

    public CountByOfficialAddress(CollectionManager collectionManager) {
        super("count_by_official_address", "вывести количество элементов, у которых значение поля officialAddress равно заданному", 2, "\"street\" \"zipCode\"");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    @Override
    public Response execute(Request request) {
        String street = ((String[])request.getArgs())[0];
        String zipCode = ((String[])request.getArgs())[1];
//        if (count == -52) return new Response(false, "Ошибка при подсчёте количества элементов по заданному OfficialAddress: \"street\" " + street + " и \"zipCode\"");
//        if (count == 0) return new Response(false, "Количество элементов по заданному OfficialAddress: \"street\" " + street + " и \"zipCode\" равно 0");
        // Использование StreamAPI и lambda exspression
        long count = collectionManager.getCollection().stream().filter(org -> org.getOfficialAddress().getStreet().equals(street) && org.getOfficialAddress().getZipCode().equals(zipCode)).count();
        return new Response(true, "Количество элементов, у которых значение поля officialAddress (\"street\" = " + street + "; \"zipCode\" = " + zipCode + ") равно " + count);
    }

}
