package org.example.command.commands;

import org.example.command.Command;
import org.example.entity.Organization;
import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.network.User;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "remove_first".
 * Описание команды: удалить первый элемент из коллекции
 */
public class RemoveFirst extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 64L;
    private final CollectionManager collectionManager;
    private final DataBaseManager dataBaseManager;
    public RemoveFirst(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("remove_first", "удалить первый элемент из коллекции", 0, "");
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
        User user = request.getUser();
        if (collectionManager.getCollection() == null || collectionManager.getCollection().isEmpty()) {
            return new Response(false, "Коллекция пуста. Нельзя удалить первый элемент.");
        }
        Integer IdFirstOrg = collectionManager.getFirstElementId();
        // Проверяем, есть ли у пользователя доступ к первому элементу коллекции
        if (!dataBaseManager.checkUserById(IdFirstOrg, user)) {
            return new Response(false, "Вы не являетесь владельцем первого элемента коллекции! У первого элемента id=" + IdFirstOrg);
        }
        if (dataBaseManager.deleteOrganization(collectionManager.getFirstElementId(), user)) {
            collectionManager.removeFirstElementOfCollection();
            return new Response(true, "Первый элемент коллекции был успешно удален!");
        } else {
            return new Response(false, "Не удалось удалить первый элемент коллекции. Возможно, он не принадлежит вам.");
        }
    }
}
