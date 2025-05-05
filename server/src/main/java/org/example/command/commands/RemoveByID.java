package org.example.command.commands;

import org.example.command.Command;
import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.network.User;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "remove_by_id".
 * Описание команды: удалить элемент из коллекции по его id
 */
public class RemoveByID extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 63L;
    private CollectionManager collectionManager;
    private final DataBaseManager dataBaseManager;

    public RemoveByID(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("remove_by_id", "удалить элемент из коллекции по его id", 1, "\"id\"");
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    @Override
    public Response execute(Request request) {
        try {
            int id = Integer.parseInt(((String[])request.getArgs())[0]);
            User user = request.getUser();
            // Пытаемся удалить элемент из базы данных по id и пользователю
            if (dataBaseManager.deleteOrganization(id, user)) {
                // Удаление элемента из коллекции
                collectionManager.removeById(id);
                return new Response(true, "Элемент с id=" + id + " был успешно удален!");
            } else{
                return new Response(false, "Элемента с id=" + id + " не был удален! Его просто нет в коллекции.");
            }
        } catch (NumberFormatException e) {
            return new Response(false, "Введите целочисленное число");
        }
    }
}
