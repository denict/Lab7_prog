package org.example.command.commands;

import org.example.command.Command;
import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.network.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Команда "clear".
 * Описание команды: очистить коллекцию.
 */
public class Clear extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 55L;
    private final CollectionManager collectionManager;
    private final DataBaseManager dataBaseManager;
    public Clear(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("clear", "очистить коллекцию", 0, "");
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;
    }

    /**
     * Выполняет команду очистки коллекции.
     * Удаляет все элементы, принадлежащие пользователю, сначала из БД, а затем - из локальной коллекции.
     *
     * @param request объект запроса, содержащий данные пользователя
     * @return объект ответа с результатом выполнения
     */
    @Override
    public Response execute(Request request) {
        try {
            User user = request.getUser();

            List<Integer> deletedIds = dataBaseManager.deleteAllOrganizationsByUser(user);
            if (deletedIds == null || deletedIds.isEmpty()) {
                return new Response(false, "У вас нет объектов для удаления пользователя\""+user.getLogin()+"\", \"deleteAllOrganizationsByUser\" выдал null");
            }
            for (Integer id : deletedIds) {
                collectionManager.removeById(id);
            }
            return new Response(true, "Объекты пользователя User \"" + user.getLogin() + "\" удалены");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "Произошла ошибка во время выполнения команды \"" + getName() + "\": " + e.getMessage());
        }
    }
}
