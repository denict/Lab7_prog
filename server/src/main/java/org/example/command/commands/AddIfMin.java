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
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Команда "add_if_min".
 * Описание команды: добавить новый элемент, если его значение меньше, чем у наименьшего элемента коллекции.
 */
public class AddIfMin extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 54L;

    /** Менеджер коллекции, управляющий всеми организациями в памяти */
    private final CollectionManager collectionManager;

    /** Менеджер базы данных, обрабатывающий взаимодействие с базой */
    private final DataBaseManager dataBaseManager;

    /**
     * Конструктор команды add_if_min.
     *
     * @param collectionManager менеджер коллекции
     * @param dataBaseManager менеджер базы данных
     */
    public AddIfMin(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("add_if_min", "добавить элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", 0, "");
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;
    }

    /**
     * Выполняет команду add_if_min.
     * Добавляет объект Organization в коллекцию, если его значение меньше минимального.
     *
     * @param request объект запроса, содержащий объект organization и данные пользователя
     * @return объект ответа с результатом выполнения
     */
    @Override
    public Response execute(Request request) {
        Organization org = request.getOrganization();
        User user = request.getUser();
        if (CollectionManager.getCollection() == null) {

            // Сначала добавляем элемент в базу данных
            int id = dataBaseManager.addOrganization(org, user);
            if (id == -52) return new Response(false, "Ошибка добавления в базу данных, что-то со стороны сервера или проверьте корректность данных");
            collectionManager.add(org);
            collectionManager.updateMinElement();
            return new Response(true, "Элемент успешно добавлен в коллекцию,коллекция и так пустая! Минимальный элемент коллекции обновлён.");
        } else {

            // Находим минимальный элемент
            Organization min = collectionManager.getCollection().stream().min(Comparator.comparing(or -> or.getAnnualTurnover())).orElse(null);
            // Сравниваем новый элемент с минимальным
            if (Stream.of(org).anyMatch(o -> o.getAnnualTurnover() < min.getAnnualTurnover())) {
                int id = dataBaseManager.addOrganization(org, user);
                if (id == -52) return new Response(false, "Ошибка добавления элемента в базу данных. Не удалось добавить элемент.");
                if (id == -1) return new Response(false, "\"SQLException\"");
                org.setId(id);
                collectionManager.add(org);
                collectionManager.setMinElement(org);
                return new Response(true, "Элемент успешно добавлен в коллекцию! Минимальный элемент коллекции обновлён.");
            } else {

                return new Response(false, "Элемент не добавлен в коллекцию, потому он больше минимального элемента этой коллекции: " + collectionManager.getMinElement());
            }
        }
    }
}
