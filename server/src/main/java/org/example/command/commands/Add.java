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
 * Команда "add".
 * Описание команды: добавление нового элемента в коллекцию.
 */
public class Add extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 53L;

    /** Менеджер коллекции, управляющий всеми организациями в памяти */
    private final CollectionManager collectionManager;

    /** Менеджер базы данных, обрабатывающий взаимодействие с базой */
    private final DataBaseManager dataBaseManager;

    /**
     * Конструктор команды добавления.
     *
     * @param collectionManager менеджер коллекции
     * @param dataBaseManager менеджер базы данных
     */
    public Add(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("add", "добавить новый элемент в коллекцию", 0, "");
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;

    }

    /**
     * Выполняет команду добавления.
     * Получает объект Organization из запроса, добавляет его в базу данных и в коллекцию.
     *
     * @param request объект запроса, содержащий объект organization и данные пользователя
     * @return объект ответа с результатом выполнения
     */
    @Override
    public Response execute(Request request) {
        try {
            Organization org = request.getOrganization();
            User user = request.getUser();
            // Сначала добавляем элемент в базу данных
            if (request.getOrganization().validate()) {
                int id = dataBaseManager.addOrganization(org, user); // запрос возвращает id добавленного объекта (id создаётся в БД)
                if (id == -52) {
                    return new Response(false, "Не удалось добавить объект в базу данных, что-то стороны базы данных");
                }
                // Если добавление элемента в БД прошло успешно, добавляем элемент в локальную коллекцию
                request.getOrganization().setId(id);
                collectionManager.add(org);
                return new Response(true, "Объект успешно создан и добавлен в коллекцию");
            } else {
                return new Response(false, "Объект не прошел валидацию, проверьте корректность данных");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "Не удалось добавить объект в коллекцию, проверьте корректность данных");
        }
    }
}
