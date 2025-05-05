package org.example.network;

import org.example.command.Command;
import org.example.managers.CollectionManager;
import org.example.managers.CommandManager;
import org.example.managers.DataBaseManager;
import org.example.utility.ConsoleInput;
import org.example.utility.ConsoleOutput;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ServerRunnerManager implements Runnable{
    private final Server server;
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private final ConsoleInput consoleInput;
    private final ConsoleOutput consoleOutput;
    private final DataBaseManager dataBaseManager;
    private static final Logger logger = Logger.getLogger(ServerRunnerManager.class.getName());
    private volatile boolean isRunning = true;

    // Пул потоков для обработки полученного запроса
    private final ExecutorService requestProcessorPool = Executors.newCachedThreadPool();

    // Пул потоков для отправки ответа
    private final ForkJoinPool responseSenderPool = ForkJoinPool.commonPool();


    private final BufferedInputStream input = new BufferedInputStream(System.in);
    private final BufferedReader scanner = new BufferedReader(new InputStreamReader(input));

    /**
     * Конструктор класса ServerRunnerManager.
     *
     * @param server сервер
     * @param commandManager менеджер команд
     * @param dumpManager менеджер дампов
     * @param collectionManager менеджер коллекции
     * @param consoleInput консольный ввод
     * @param consoleOutput консольный вывод
     */
    public ServerRunnerManager(Server server, CommandManager commandManager, DataBaseManager dataBaseManager, CollectionManager collectionManager, ConsoleInput consoleInput, ConsoleOutput consoleOutput) {
        this.server = server;
        this.commandManager = commandManager;
        this.dataBaseManager = dataBaseManager;
        this.collectionManager = collectionManager;
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
    }



    /**
     * Обрабатывает запрос от клиента.
     * @param clientSocket сокет клиента
     */
    private void processClientRequest(SocketChannel clientSocket) {
        Request userRequest;
        Response responseToUser;

        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.socket().getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.socket().getOutputStream())) {
            userRequest = (Request) clientReader.readObject();
            logger.info("Запрос с командой " + userRequest.getCommand().getName());
            Future<Response> responseFuture = requestProcessorPool.submit(() -> { // Обработка полученного запроса
                Command command = CommandManager.getCommand(userRequest.getCommand().getName());
                if (command == null) {
//                    consoleOutput.printError("Команда " + userRequest.getCommand().getName() + " не найдена");
                    logger.warning("Команда " + userRequest.getCommand().getName() + " не найдена");
                    clientWriter.writeObject(new Response(false, "Команда " + userRequest.getCommand().getName() + " не найдена"));
                    return new Response(false, "Команда " + userRequest.getCommand().getName() + " не найдена");
                }
                return command.execute(userRequest); // Вызов команды
            });
//            responseToUser = CommandManager.getCommand(userRequest.getCommand().getName()).execute(userRequest); // Вызов команды
              responseToUser = responseFuture.get(); // Получение результата выполнения команды

            CompletableFuture.runAsync(() -> { // Отправка ответа клиенту
                try {
                    clientWriter.writeObject(responseToUser); // Отправка Response
                    logger.info("Отправлен ответ " + responseToUser.getMessage());
                    clientWriter.flush();
                } catch (IOException e) {
                    logger.warning("Ошибка при отправке ответа клиенту: " + e.getMessage());
                }
            }, responseSenderPool).join(); // Возврат результирующего значения

        } catch (ClassNotFoundException | InvalidClassException | NotSerializableException e) {
            consoleOutput.printError("Ошибка при взаимодействии с клиентом!\n" + e.getMessage());
            e.printStackTrace();
            logger.warning("Ошибка при взаимодействии с клиентом!\n" + e.getMessage());
        } catch (IOException e) {
            consoleOutput.printError("Ошибка ввода вывода+\n" + e.getMessage());
            e.printStackTrace();
            logger.warning("Ошибка ввода вывода");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                consoleOutput.printError("Ошибка при закрытии клиентского сокета");
                logger.warning("Ошибка при закрытии клиентского сокета");
            }
        }
    }


    /**
     * Запуск сервера обработка клиентских запросов
     */
    public void run() {

        server.start();

        consoleOutput.println("Здравствуйте! Для справки по доступным командам, нажмите \"help\".");
        // При завершении программы срабатывает addShutdownHook;
        // Создаётся новый поток, который будет выполнен.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                consoleOutput.println("Завершение работы программы. До свидания!");
                logger.info("Завершение работы программы. До свидания!");
                server.stop();
                requestProcessorPool.shutdown();
                responseSenderPool.shutdown();
                logger.info("Сервер остановлен.");
        }));

        Thread inputThread = new Thread(() -> { // Поток для обработки ввода с консоли
            try {

                while (isRunning) {
                    String line = scanner.readLine();
                    if (line.equals("save")) {
                        collectionManager.loadCollection(); // DataBaseManager.createCollection(). Загрузка коллекции в локальную память
                        logger.info("Объекты коллекции Organization сохранены в локальную память");
                    } else if (line.equals("exit")) {
                        logger.info("Была введена команда exit.");
                        isRunning = false;
                    }
                }
            } catch (IOException e) {
                logger.warning("Ошибка ввода-вывода");
                e.printStackTrace();
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();

        while (isRunning) {
            try {
                SocketChannel clientSocketChannel = server.getServerSocketChannel().accept();

                if (clientSocketChannel != null) {
                    logger.info("Соединение с клиентом установлено.");
                    new Thread(() -> processClientRequest(clientSocketChannel)).start(); // Создание нового потока для чтения запросов
                }
            } catch (IOException e) {
                logger.severe("Ошибка в основном цикле сервера: " + e.getMessage());
            } catch (NoSuchElementException e) {
                logger.info("Конец ввода."); //Ctrl + D
                break;
            }
        }
        shutdown();
    }

    private void shutdown() {
        try {
            server.stop();
            requestProcessorPool.shutdown(); // запрос на завершение работы пула
            responseSenderPool.shutdown(); // запрос на завершение работы пула
            if (!requestProcessorPool.awaitTermination(5, TimeUnit.SECONDS)) { // ждёт 5 секунд
                requestProcessorPool.shutdownNow(); // принудительное завершение работы пула
            }
            if (!responseSenderPool.awaitTermination(5, TimeUnit.SECONDS)) {
                responseSenderPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.warning("Прервано ожидание завершения потоков");
        }
    }
}



//package org.example.network;
//
//import org.example.managers.CollectionManager;
//import org.example.managers.CommandManager;
//import org.example.managers.DumpManager;
//import org.example.utility.ConsoleInput;
//import org.example.utility.ConsoleOutput;
//
//import java.io.*;
//import java.nio.channels.SocketChannel;
//import java.util.NoSuchElementException;
//import java.util.concurrent.*;
//import java.util.logging.Logger;
//
//public class ServerRunnerManager implements Runnable {
//    private final Server server;
//    private final CommandManager commandManager;
//    private final CollectionManager collectionManager;
//    private final ConsoleInput consoleInput;
//    private final ConsoleOutput consoleOutput;
//    private final DumpManager dumpManager;
//    private static final Logger logger = Logger.getLogger(ServerRunnerManager.class.getName());
//    private volatile boolean isRunning = true;
//
//    // --- ТЗ: CachedThreadPool для обработки запросов
//    private final ExecutorService requestProcessorPool = Executors.newCachedThreadPool();
//
//    // --- ТЗ: ForkJoinPool для отправки ответов
//    private final ForkJoinPool responseSenderPool = ForkJoinPool.commonPool();
//
//    BufferedInputStream input = new BufferedInputStream(System.in);
//    BufferedReader scanner = new BufferedReader(new InputStreamReader(input));
//
//    public ServerRunnerManager(Server server, CommandManager commandManager, DumpManager dumpManager, CollectionManager collectionManager, ConsoleInput consoleInput, ConsoleOutput consoleOutput) {
//        this.server = server;
//        this.commandManager = commandManager;
//        this.dumpManager = dumpManager;
//        this.collectionManager = collectionManager;
//        this.consoleInput = consoleInput;
//        this.consoleOutput = consoleOutput;
//    }
//
//    // Обработка запроса клиента
//    private void processClientRequest(SocketChannel clientSocket) {
//        // --- ТЗ: обработка запроса в CachedThreadPool
//        requestProcessorPool.submit(() -> {
//            try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.socket().getInputStream());
//                 ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.socket().getOutputStream())) {
//
//                Request userRequest = (Request) clientReader.readObject();
//                logger.info("Запрос с командой " + userRequest.getCommand().getName());
//
//                Response responseToUser;
//                if (CommandManager.getCommand(userRequest.getCommand().getName()) == null) {
//                    responseToUser = new Response(false, "Команда " + userRequest.getCommand().getName() + " не найдена");
//                    logger.warning("Команда " + userRequest.getCommand().getName() + " не найдена");
//                } else {
//                    // --- ТЗ: синхронизация коллекции должна быть реализована в CollectionManager через ReentrantLock
//                    responseToUser = CommandManager.getCommand(userRequest.getCommand().getName()).execute(userRequest);
//                }
//
//                // --- ТЗ: отправка ответа в ForkJoinPool
//                responseSenderPool.execute(() -> {
//                    try {
//                        clientWriter.writeObject(responseToUser);
//                        clientWriter.flush();
//                        logger.info("Отправлен ответ " + responseToUser.getMessage());
//                    } catch (IOException e) {
//                        logger.warning("Ошибка при отправке ответа клиенту: " + e.getMessage());
//                    } finally {
//                        try {
//                            clientSocket.close();
//                        } catch (IOException e) {
//                            logger.warning("Ошибка при закрытии клиентского сокета");
//                        }
//                    }
//                });
//
//            } catch (ClassNotFoundException | InvalidClassException | NotSerializableException e) {
//                logger.warning("Ошибка при взаимодействии с клиентом!\n" + e.getMessage());
//            } catch (IOException e) {
//                logger.warning("Ошибка ввода вывода+\n" + e.getMessage());
//            }
//        });
//    }
//
//
//    public void run() {
//        server.start();
//
//        consoleOutput.println("Здравствуйте! Для справки по доступным командам, нажмите \"help\".");
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            dumpManager.writeCollection(collectionManager.getCollection());
//            consoleOutput.println("Завершение работы программы. До свидания!");
//            logger.info("Завершение работы программы. До свидания!");
//            server.stop();
//            logger.info("Сервер остановлен.");
//            requestProcessorPool.shutdown();
//            responseSenderPool.shutdown();
//        }));
//
//        Thread inputThread = new Thread(() -> {
//            try {
//                while (isRunning) {
//                    String line = scanner.readLine();
//                    if (line.equals("save")) {
//                        dumpManager.writeCollection(collectionManager.getCollection());
//                        logger.info("Объекты коллекции Organization сохранены в файл " + dumpManager.getFile().getName());
//                    } else if (line.equals("exit")) {
//                        logger.info("Была введена команда exit.");
//                        isRunning = false;
//                    }
//                }
//            } catch (IOException e) {
//                logger.warning("Ошибка ввода-вывода");
//                e.printStackTrace();
//            }
//        });
//        inputThread.setDaemon(true);
//        inputThread.start();
//
//        while (isRunning) {
//            try {
//                SocketChannel clientSocketChannel = server.getServerSocketChannel().accept();
//                if (clientSocketChannel != null) {
//                    logger.info("Соединение с клиентом установлено.");
//                    // --- ТЗ: чтение запроса в отдельном потоке (Thread)
//                    new Thread(() -> processClientRequest(clientSocketChannel)).start();
//                }
//            } catch (IOException e) {
//                logger.severe("Ошибка в основном цикле сервера: " + e.getMessage());
//            } catch (NoSuchElementException e) {
//                logger.info("Конец ввода."); //Ctrl + D
//                break;
//            }
//        }
//    }
//}