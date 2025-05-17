package org.example;

import org.example.command.commands.*;
import org.example.managers.CommandManager;
import org.example.managers.*;
import org.example.network.Server;
import org.example.network.ServerRunnerManager;
import org.example.utility.ConsoleInput;
import org.example.utility.ConsoleOutput;

import java.io.IOException;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException, IOException {


        DataBaseManager dataBaseManager = new DataBaseManager();
        ConsoleInput consoleInput = new ConsoleInput();
        ConsoleOutput consoleOutput = new ConsoleOutput();
        Server server = new Server("localhost", 7118, consoleOutput);


        CollectionManager collectionManager = new CollectionManager();
        RunnerScriptManager runnerScriptManager = new RunnerScriptManager();
        CommandManager commandManager = new CommandManager() {{
            register(new Help(this));
            register(new Info(collectionManager, consoleOutput));
            register(new Show(collectionManager, consoleOutput));
            register(new Add(collectionManager, dataBaseManager));
            register(new UpdateByID(collectionManager, dataBaseManager));
            register(new RemoveByID(collectionManager, dataBaseManager));
            register(new CheckById(collectionManager, dataBaseManager));
            register(new Clear(collectionManager, dataBaseManager));
            register(new ExecuteScript(this, consoleInput, consoleOutput, runnerScriptManager));
            register(new RemoveFirst(collectionManager, dataBaseManager));
            register(new AddIfMin(collectionManager, dataBaseManager));
            register(new History(this, consoleOutput));
            register(new CountByOfficialAddress(collectionManager));
            register(new FilterByAnnualTurnover(collectionManager));
            register(new PrintUniqueAnnualTurnover(collectionManager, consoleOutput));
            register(new Login(dataBaseManager));
            register(new Register(dataBaseManager));
            register(new Logout());
        }};
        // загрузка начального состоянии коллекции из БД
        collectionManager.loadCollection();
//        System.out.println(commandManager.getCommands().get("execute_script").execute(new Request(commandManager.getCommands().get("execute_script"), "1.txt")).getMessage());
        ServerRunnerManager serverRunnerManager = new ServerRunnerManager(server, commandManager, dataBaseManager, collectionManager, consoleInput, consoleOutput);
        serverRunnerManager.run();

    }
}





