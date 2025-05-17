package org.example.network;
import org.example.command.Command;
import org.example.command.commands.Login;
import org.example.command.commands.Register;
import org.example.entity.builders.UserBuilder;
import org.example.exception.LogoutException;
import org.example.managers.CommandManager;
import org.example.utility.ConsoleInput;
import org.example.utility.ConsoleOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RequestManager implements Runnable{
    private final ConsoleInput consoleInput;
    private final ConsoleOutput consoleOutput;
    private final CommandManager commandManager;
    private final Client client;

    public RequestManager(ConsoleInput consoleInput, ConsoleOutput consoleOutput, CommandManager commandManager, Client client) {
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.commandManager = commandManager;
        this.client = client;
    }

    private void runCommand(String request, CommandManager commandManager, ConsoleOutput consoleOutput, User user) throws InterruptedException, LogoutException {

            String[] requestArray = request.split(" ");
            String nameCommand = requestArray[0].toLowerCase();
        if (nameCommand.isBlank()) {
            consoleOutput.println("Пустая строка. Воспользуйтесь командой \"help\" для просморта доступных команд");
            return;
        }

        if (nameCommand.equals("exit")) {
            System.exit(0);
            client.disconnect();
        }





        if (!commandManager.getCommands().containsKey(nameCommand)) {
            consoleOutput.println("Команда \"" + nameCommand + "\" не найдена. Воспользуйтесь командой \"help\" для просморта доступных команд");
            return;
        }
        try {

            Command command = commandManager.getCommands().get(nameCommand);
            String[] args = Arrays.copyOfRange(requestArray, 1, requestArray.length);
            if (command.getArgsCount() != args.length) {
                consoleOutput.printError("Команда " + nameCommand + " принимает " + command.getArgsCount() + " аргумент(а)." + " Правильное использование: " + nameCommand + " " + command.getUsageArg());
                return;
            }
            if (nameCommand.equals("update_by_id") || nameCommand.equals("remove_by_id")) {
                Response existByIdResponse = client.sendRequest(new Request(
                        commandManager.getCommands().get("check_by_id"), args, user));
                if (existByIdResponse == null || !existByIdResponse.isSuccess()) {
                    consoleOutput.printError(existByIdResponse.getMessage() != null ? existByIdResponse.getMessage() : "Ошибка при проверке id");
                    return;
                }
            }




            command.execute(args, user); // sendRequest отправляется на сервер
            commandManager.addToHistory(commandManager.getCommands().get(nameCommand));
        } catch (NoSuchElementException e) {
            consoleOutput.println("^D");
        }

    }


    public void run() {

        // При завершении программы срабатывает addShutdownHook;
        // Создаётся новый поток, который будет выполнен.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consoleOutput.println("Завершение работы программы. До свидания!");
            client.disconnect();
        }));


        while (true) {
            // Вход в систему
            try {
                User user = authorizeUser();
                consoleOutput.println("Здравствуйте! Для справки по доступным командам, нажмите \"help\".");
                while (true) {
                    consoleOutput.print("> ");
                    String request = consoleInput.readLine().trim();
                    runCommand(request, commandManager, consoleOutput, user);
                }
            } catch (NoSuchElementException e) {
                consoleOutput.println("Конец ввода."); // Ctrl+D
                return;
            } catch (LogoutException e) {
            } catch (InterruptedException e) {
                consoleOutput.println("Поток прерван: " + e.getMessage());
            } catch (Exception e) {
                consoleOutput.printError("Ошибка во время выполнения: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * Вход в аккаунт или регистрация
     * @return Авторизация пользователя User, в котором хранится логин и пароль для
     * дальнейшей идентификации и логирование запросов на сервер и базу данных
     */
    private User authorizeUser() {
        while (true) {
            consoleOutput.println("Здравствуйте! Хотите зарегистрироваться или войти в систему? (\"login\" или \"register\"), или введите \"exit\" для выхода.");
            consoleOutput.print("> ");
            String line = consoleInput.readLine().trim().toLowerCase();

            if (line.equals("login")) {
                consoleOutput.println("Вход в систему");
                UserBuilder userbuilder = new UserBuilder(consoleInput, consoleOutput);
                String login = userbuilder.askLogin();
                String password = userbuilder.askPasswordLogin();
                User user = new User(login, password);
                Response response = client.sendRequest(new Request(new Login(consoleOutput, client), user));
                if (response.isSuccess()) {
                    consoleOutput.println("Успешный вход: " + response.getMessage());
                    return user;
                } else {
                    consoleOutput.println("Ошибка входа: " + response.getMessage());
                }
            } else if (line.equals("register")) {
                consoleOutput.println("Регистрация нового пользователя");
                User user = loginToDb();
                Response response = client.sendRequest(new Request(new Register(consoleInput, consoleOutput, client), user));
                if (response.isSuccess()) {
                    consoleOutput.println("Успешная регистрация: " + response.getMessage());
                    return user;
                } else {
                    consoleOutput.println("Ошибка регистрации: " + response.getMessage());
                }
            } else if (line.equals("exit")) {
                consoleOutput.println("До свидания!");
                System.exit(0);
            } else {
                consoleOutput.println("Пожалуйста, введите \"login\", \"register\" или \"exit\".");
            }
        }
    }


    /**
     * Создание пользователя для подключения к серверу и затем Db
     *
     * @return созданный пользователь
     */
    private User loginToDb() {
        return new UserBuilder(consoleInput, consoleOutput).build();
    }

    /**
     * Метод выхода пользователя из режима ввода.
     * Происходит запрос у пользователя на подтверждение выхода.
     */
    private void exit() {
        String in;
        while (true) {
            System.out.println("Введите \"exit\" если у вас нет существующего аккаунта, иначе нажмите Enter");
            in = consoleInput.readLine();
            if (in.equals("exit")) {
                System.out.println("До связи");
                System.exit(1);
            } else if (in.isBlank()) {
                break;
            } else {
                System.out.println("Неверный ввод. Попробуйте еще раз нажать на Enter.");
            }
        }
    }

    //            if (nameCommand.equals("execute_script")) {
//                String scriptName = requestArray[1];
//                Set<String> scriptSet = new HashSet<>();
//                if (hasRecursion(scriptName, scriptSet)) {
//                    consoleOutput.printError("Обнаружена рекурсия в скрипте: " + scriptName);
//                    return;
//                }
//            }
//    private boolean hasRecursion(String scriptName, Set<String> scriptSet) {
//        if (scriptSet.contains(scriptName)) {
//            return true;
//        }
//
//        scriptSet.add(scriptName);
//
//        try (Scanner fileScanner = new Scanner(new File(scriptName))) {
//            while (fileScanner.hasNextLine()) {
//                String line = fileScanner.nextLine().trim();
//                if (line.startsWith("execute_script")) {
//                    String[] parts = line.split("\\s+");
//                    if (parts.length == 2) {
//                        String nestedScript = parts[1];
//                        if (hasRecursion(nestedScript, scriptSet)) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Файл скрипта не найден: " + scriptName);
//        }
//
//        scriptSet.remove(scriptName);
//        return false;
//    }
}