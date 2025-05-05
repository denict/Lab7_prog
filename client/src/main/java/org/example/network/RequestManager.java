package org.example.network;
import org.example.command.Command;
import org.example.command.commands.Login;
import org.example.command.commands.Register;
import org.example.entity.builders.UserBuilder;
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

    private void runCommand(String request, CommandManager commandManager, ConsoleOutput consoleOutput, User user) throws InterruptedException {

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

        if (nameCommand.equals("execute_script")) {
            String scriptName = requestArray[1];
            Set<String> scriptSet = new HashSet<>();
            if (hasRecursion(scriptName, scriptSet)) {
                consoleOutput.printError("Обнаружена рекурсия в скрипте: " + scriptName);
                return;
            }
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
            System.out.println("Здравствуйте! Хотите вы зарегистрироваться или войти в систему?\n \"login\" или \"register\")");
            String line = consoleInput.readLine();
            if (line.equals("login")) {
                System.out.println("Вход в систему");
            } else (line.equals("register")) {
                Response response = client.sendRequest(new Request(new Login(consoleOutput, client), user))
            }
        }
//        boolean success = false;
        User user = loginToDb();
        boolean
        if (isLogged) {
            Response response = client.sendRequest(new Request(new Login(consoleOutput, client), user));
            System.out.println();

            if (!response.isSuccess()) {
                System.out.println("Такого пользователя не существует или введены неверные данные.");
                while (true) {
                    exit();
                    user = loginToDb(); // создание нового пользователя
                    Response newResponse = client.sendRequest(new Request(new Login(consoleOutput, client), user));
                    System.out.println(newResponse.getMessage());
                    if (newResponse.isSuccess()) {
                        success = true;
                        break;
                    } else {
                        break;
                    }
                }
            } else {
                success = true;
            }
        }

        if (!success) {
            Response response = client.sendRequest(new Request(new Register(consoleInput, consoleOutput, client), user));
            System.out.println(response.getMessage());
            if (!response.isSuccess()) {
                System.out.println("Та самая ошибка");
                System.exit(1);
            }
        }


        consoleOutput.println("Здравствуйте! Для справки по доступным командам, нажмите \"help\".");
        while (true) {
            try {
                consoleOutput.print("> ");
                String request = consoleInput.readLine().trim();
                runCommand(request, commandManager, consoleOutput, user);
            } catch (NoSuchElementException e) {
                consoleOutput.println("Конец ввода."); // Ctrl+D
                break;
            } catch (Exception e) {
                consoleOutput.printError("Ошибка во время выполнения: " + e.getMessage());
                e.printStackTrace();
                break;
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

    private boolean hasRecursion(String scriptName, Set<String> scriptSet) {
        if (scriptSet.contains(scriptName)) {
            return true;
        }

        scriptSet.add(scriptName);

        try (Scanner fileScanner = new Scanner(new File(scriptName))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.startsWith("execute_script")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length == 2) {
                        String nestedScript = parts[1];
                        if (hasRecursion(nestedScript, scriptSet)) {
                            return true;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл скрипта не найден: " + scriptName);
        }

        scriptSet.remove(scriptName);
        return false;
    }
}