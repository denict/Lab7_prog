package org.example.command.commands;

import org.example.command.Command;
import org.example.entity.Organization;
import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.network.User;
import org.example.utility.ConsoleInput;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Команда "update_by_id".
 * Описание команды: обновить значение элемента коллекции, id которого равен заданному
 */
public class UpdateByID extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 65L;
    private final CollectionManager collectionManager;
    private final DataBaseManager dataBaseManager;


    public UpdateByID(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("update_by_id", "обновить значение элемента коллекции, id которого равен заданному", 1, "\"id\"");
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;
    }

    /**
     * Выполняет команду обновления.
     * Получает объект Organization из запроса, обновляет его сначала в базе данных, а затем - в коллекции.
     *
     * @param request объект запроса, содержащий объект organization и данные пользователя
     * @return объект ответа с результатом выполнения
     */
    @Override
    public Response execute(Request request) {
                int id = Integer.parseInt(((String[])(request.getArgs()))[0]);
                Organization organization = request.getOrganization();
                User user = request.getUser();

                // Сначала обновляем в базе данных
                if (dataBaseManager.updateOrganization(id, user, organization)) {
                    // Если обновление в базе данных прошло успешно, обновляем в коллекции
                    collectionManager.updateById(id, organization);
                    return new Response(true, "Элемент с id=" + id + " был успешно изменен!");
                }
                return new Response(false, "Элемент с id=" + id + " не был удален! Его просто нет в коллекции.");
    }
}
