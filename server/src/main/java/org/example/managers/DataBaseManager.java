package org.example.managers;

import org.example.entity.Address;
import org.example.entity.Coordinates;
import org.example.entity.Organization;
import org.example.entity.OrganizationType;
import org.example.network.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;

/**
 * Менеджер для работы с базой данных приложения.
 * Обеспечивает все операции с базой данных: аутентификацию пользователей,
 * CRUD операции с организациями, управление соединением с БД.
 */
public class DataBaseManager {

    private static String pepper;
    private final Connection connection;


    private final QueryManager queryManager = new QueryManager();


    /**
     * Конструктор менеджера БД.
     * Инициализирует соединение с базой данных на основе параметров из файла configuration.txt.
     * @throws SQLException если не удалось установить соединение с БД
     */
    public DataBaseManager() throws SQLException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("configuration.txt")) {
            if (input == null) {
                throw new SQLException("\"configuration.txt\" не найден");
            }
            Properties prop = new Properties();
            prop.load(input);
            String url = prop.getProperty("url");
            String user = prop.getProperty("login");
            String password = prop.getProperty("password");
            pepper = Files.readString(Paths.get("pepper.txt")).trim();
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (IOException e) {
            throw new SQLException("Ошибка загрузки конфигурации БД");
        }

    }


    public void close() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }


    /**
     * Проверяет существование пользователя в базе данных.
     * @param user пользователь для проверки
     * @return true если пользователь существует и пароль верный, иначе false
     */
    public boolean existUser(User user) {
        try {
            PasswordManager passwordManager = new PasswordManager();
            Connection connection = this.connection;
            PreparedStatement preparedStatement = connection.prepareStatement(queryManager.findingUser);
            preparedStatement.setString(1, user.getLogin());

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String password = user.getPassword() + resultSet.getString("salt") + pepper;
                if(resultSet.getString("password").equals(passwordManager.hashPassword(password))) {
                    resultSet.close();
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Ошибка выполнения запроса");
            e.printStackTrace();
            return false;
        }
    }


    public void addUser(User user) {
        try {
            PreparedStatement checkUser = connection.prepareStatement("SELECT 1 FROM users WHERE login = ?");
            checkUser.setString(1, user.getLogin());
            ResultSet checkResult = checkUser.executeQuery();
            if (checkResult.next()) {
                System.err.println("Пользователь с таким логином уже существует");
                checkResult.close();
                return;
            }

            PasswordManager passwordManager = new PasswordManager();
            String salt = saltGenerator();
            Connection connection = this.connection;
            String password = passwordManager.hashPassword(user.getPassword() + salt + pepper);
            PreparedStatement preparedStatement = connection.prepareStatement(queryManager.addUser);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, salt);
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            System.err.println("Ошибка выполнения запроса");
            e.printStackTrace();
        }
    }


    /**
     * Генерирует случайную соль для хеширования паролей.
     * @return строка со случайной солью
     */
    private String saltGenerator() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(15);

        for (int i = 0; i < 15; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    /**
     * Добавляет организацию в базу данных.
     * @param org организация для добавления
     * @param user пользователь, добавляющий группу
     * @return ID добавленной группы или -52 при ошибке
     */
    public int addOrganization(Organization org, User user) {
        try {
            Connection connection = this.connection;
            PreparedStatement preparedStatement = connection.prepareStatement(queryManager.addOrganization);
            preparedStatement.setString(1, org.getName());
            preparedStatement.setLong(2, org.getCoordinates().getX());
            preparedStatement.setLong(3, org.getCoordinates().getY());
            preparedStatement.setTimestamp(4, new java.sql.Timestamp(org.getCreationDate().getTime()));
            preparedStatement.setDouble(5, org.getAnnualTurnover());
            if (org == null || org.getOrganizationType() == null) {
                preparedStatement.setNull(6, java.sql.Types.OTHER);
            } else {
                preparedStatement.setObject(6, org.getOrganizationType().toString(), java.sql.Types.OTHER);
            }
            preparedStatement.setString(7, org.getOfficialAddress().getStreet());
            if (org.getOfficialAddress().getZipCode() == null) {
                preparedStatement.setNull(8, java.sql.Types.VARCHAR);
            } else {
                preparedStatement.setString(8, org.getOfficialAddress().getZipCode());
            }
            preparedStatement.setString(9, user.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.err.println("Не удалось добавить объект organization");
                resultSet.close();
                return -52;
            }
            System.out.println("Объект успешно добавлен!");
            int id = resultSet.getInt("id_organization");
            return id;

        } catch(SQLException e) {
            System.err.println("Ошибка выполнения добавления организации");
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Обновляет организацию в базе данных.
     * @param id ID организации для обновления
     * @param user пользователь, выполняющий обновление
     * @param org новые данные организации
     * @return true если обновление успешно, иначе false
     */
    public boolean updateOrganization(int id, User user, Organization org) {
        try {
            Connection connection = this.connection;
            PreparedStatement preparedStatement = connection.prepareStatement(queryManager.updateOrganization);
            preparedStatement.setString(1, org.getName());
            preparedStatement.setLong(2, org.getCoordinates().getX());
            preparedStatement.setLong(3, org.getCoordinates().getY());
            preparedStatement.setString(4, org.getCreationDate().toString());
            preparedStatement.setDouble(5, org.getAnnualTurnover());
            preparedStatement.setString(6, org.getOrganizationType().toString());
            preparedStatement.setString(7, org.getOfficialAddress().getStreet());
            preparedStatement.setString(8, org.getOfficialAddress().getZipCode());
            preparedStatement.setString(9, user.getLogin());
            preparedStatement.setInt(10, id);
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (!resultSet.next()) {
//                System.err.println("Не удалось обновить объект organization");
//                resultSet.close();
//                return false;
//            }
//            System.out.println("Объект успешно обновлён!");
//            return true;

        } catch(SQLException e) {
            System.err.println("Ошибка выполнения запроса на обновление организации по ID");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Удаляет организацию по ID.
     * @param user пользователь, выполняющий удаление
     * @param id ID организации для удаления
     * @return true если удаление успешно, иначе false
     */
    public boolean deleteOrganization(int id, User user) {
        try {
            Connection connection = this.connection;
            PreparedStatement preparedStatement = connection.prepareStatement(queryManager.deleteOrganization);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setInt(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.err.println("Не удалось удалить объект organization");
                resultSet.close();
                return false;
            }
            System.out.println("Объект успешно удалён!");
            return true;
        } catch (SQLException e) {
            System.err.println("Ошибка выполнения запроса на удаление организации по ID");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Удаляет все организации пользователя по списку ID.
     * @param ids список ID организаций для удаления
     * @param user пользователь, чьи организации удаляются
     * @return true если удаление успешно, иначе false
     */
    public List<Integer> deleteAllOrganizationsByUser(User user) {
        Connection connection = this.connection;
        List<Integer> deletedIds = new ArrayList<>();
        try {
                PreparedStatement preparedStatement = connection.prepareStatement(queryManager.deleteAllOrganizationsByUser);
                preparedStatement.setString(1, user.getLogin());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    deletedIds.add(resultSet.getInt("id_organization"));
                }
                resultSet.close();
        } catch (SQLException e) {
            System.err.println("Ошибка выполнения запроса на удаление всех организаций пользователя");
            e.printStackTrace();
            return null;
        }
        return  deletedIds;
    }



    /**
     * Подсчитывает количество организаций по заданному адресу.
     * @param street улица официального адреса
     * @param zipCode почтовый индекс официального адреса
     * @return количество организаций с указанным адресом, либо -52 при ошибке
     */
    public int countByOfficialAddress(String street, String zipCode) {
        Connection connection = this.connection;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(queryManager.countByOfficialAddress);
            preparedStatement.setString(1, street);
            preparedStatement.setString(2, zipCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                resultSet.close();
                return count;
            }
            resultSet.close();
        } catch (SQLException e) {
            System.err.println("Ошибка выполнения запроса на подсчёт организаций по адресу, SQLException");
            e.printStackTrace();
            return -52;
        }
        return -52;
    }



    /**
     * Создает коллекцию организаций из базы данных.
     * @return стек объектов Organization
     */
    public Stack<Organization> createCollection() {
        Stack<Organization> organizations = new Stack<>();
        try {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(queryManager.getAllOrganizations);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    organizations.add(new Organization(resultSet.getInt(1),
                            resultSet.getString(2),
                            new Coordinates(resultSet.getLong(3), resultSet.getLong(4)),
                            Date.from(
                                    LocalDateTime.parse(
                                            resultSet.getString(5),
                                            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                                    ).atZone(ZoneId.systemDefault()).toInstant()
                            ),
                            resultSet.getDouble(6),
                            OrganizationType.valueOf(resultSet.getString(7)),
                            new Address(resultSet.getString(8), resultSet.getString(9))
                            ));
                }
                resultSet.close();
                return organizations;
            } catch (SQLException e) {
                System.err.println("Ошибка выполнения запроса на получение всех организаций");
                e.printStackTrace();
                return new Stack<>();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Поля Объектов не валидны");
            return new Stack<>();
        }
    }
}
