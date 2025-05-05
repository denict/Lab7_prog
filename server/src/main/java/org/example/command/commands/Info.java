package org.example.command.commands;

import org.example.command.Command;
import org.example.managers.CollectionManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;


/**
 * Команда "info".
 * Описание команды: вывод в стандартный поток вывода информации о коллекции (тип, дата инициализации, количество элементов и т.д.)
 */
public class Info extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 61L;
    private final CollectionManager collectionManager;
    private final ConsoleOutput consoleOutput;

    public Info(CollectionManager collectionManager, ConsoleOutput consoleOutput) {
        super("info", "вывод в стандартный поток вывода информации о коллекции (тип, дата инициализации, количество элементов и т.д.)", 0, "");
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
        return new Response(true, "Информация о коллекции:\n" + String.format(
                "Тип: %s\n" +
                        "Дата инициализации: %s\n" +
                        "Количество элементов: %d",
                collectionManager.getTypeOfCollection(),
                collectionManager.getInitTime(),
                collectionManager.getCollectionSize()
        ));
    }
}
