package org.example.command.commands;

import org.example.command.Command;
import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.network.User;

import java.io.Serial;
import java.io.Serializable;

public class CheckById extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 201L;
    private final transient CollectionManager collectionManager;
    private final transient DataBaseManager dataBaseManager;

    public CheckById(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("check_by_id", "проверка существования элемента с заданным id и принадлежности его к текущему пользователю", 1, "id");
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            String[] args = (String[]) request.getArgs();
            int id = Integer.parseInt(args[0]);
            User user = request.getUser();
            if (collectionManager.byId(id) == null) {
                return new Response(false, "Элемент с id=" + id + " не существует в коллекции.");
            }
            if (!dataBaseManager.checkUserById(id, user)) {
                return new Response(false, "Вы не являетесь владельцем элемента с id=" + id + "!");
            }
            return new Response(true, "Элемент с id=" + id + " существует в коллекции.");

        } catch (Exception e) {
            return new Response(false, "Ошибка проверки id: " + e.getMessage());
        }
    }
}
