package org.example.managers;

/**
 * Менеджер SQL-запросов.
 * Содержит предопределенные SQL-запросы для работы с базой данных.
 * Все запросы используют параметризованный ввод для защиты от SQL-инъекций.
 */
public class QueryManager {

    /**
     * SQL-запрос для поиска пользователя и получения его пароля и соли.
     * Параметры:
     * 1. Имя пользователя (String)
     */
    public final String findingUser = "SELECT password, salt FROM users WHERE login = ?;";

    public final String getUserByID = "SELECT user_login FROM organization WHERE id_organization = ?;";

    /**
     * SQL-запрос для добавления нового пользователя.
     * Параметры:
     * 1. Имя пользователя (String)
     * 2. Хеш пароля (String)
     * 3. Соль (String)
     */
    public final String addUser = "INSERT INTO users (login, password, salt) VALUES (?, ?, ?);";


    /**
     * SQL-запрос для добавления организации.
     * Параметры:
     * 1. Название организации name (String)
     * 2. Координата x (long)
     * 3. Координата Y (long)
     * 4. Дата создания creation_date (String)
     * 5. Готовой оборот annual_turnover (Double)
     * 6. Тип организации type (String)
     * 7. Адрес street (String)
     * 8. Почтовый индекс zip_code (String)
     * 9. Логин пользователя user_login (String)
     * Возвращает: ID добавленной записи
     */
    public final String addOrganization = "INSERT INTO organization(name, x, y, creation_date,"
            + "annual_turnover, type, street, zip_code, user_login) "
            + "VALUES (?,?,?,?,?,?,?,?,?) RETURNING id_organization;";

    /**
     * SQL-запрос для удаления организации.
     * Параметры:
     * 1. Логин пользователя (String)
     * 2. ID организации (int)
     * Возвращает: ID удаленной записи
     */
    public final String deleteOrganization = "DELETE FROM organization WHERE (user_login = ?) AND (id_organization = ?) RETURNING id_organization;";


    /**
     * SQL-запрос для удаления всех организаций пользователя.
     * Параметры:
     * 1. Логин пользователя (String)
     * Возвращает: столбец удаленных ID записей
     */
    public final String deleteAllOrganizationsByUser = "DELETE FROM organization WHERE user_login = ? RETURNING id_organization;";

    /**
     * SQL-запрос для подсчёта количества организаций по определённому адресу.
     * Адрес состоит из street и zip_code.
     * Параметры:
     * 1. Адрес street (String)
     * 2. Почтовый индекс zip_code (String)
     */
    public final String countByOfficialAddress = "SELECT COUNT(*) FROM organization WHERE street = ? AND zip_code = ?;";

    /**
     * SQL-запрос для обновления информации об организации.
     * Параметры:
     * 1. Название организации name (String)
     * 2. Координата x (long)
     * 3. Координата Y (long)
     * 4. Дата создания creation_date (String)
     * 5. Готовой оборот annual_turnover (Double)
     * 6. Тип организации type (String)
     * 7. Адрес street (String)
     * 8. Почтовый индекс zip_code (String)
     * 9. Логин пользователя user_login (String)
     * 10. ID организации (id_organization) (int)
     */
    public final String updateOrganization = "UPDATE organization SET name = ?, x = ?, y = ?, creation_date = ?, "
            + "annual_turnover = ?, type = ?, street = ?, zip_code = ? "
            + "WHERE user_login = ? AND id_organization = ?";

    /**
     * SQL-запрос для получения всех организаций.
     * Возвращает все поля и кортежи таблицы organization.
     */
    public final String getAllOrganizations = "SELECT * FROM organization;";
}
