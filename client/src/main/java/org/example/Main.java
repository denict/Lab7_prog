package org.example;
import org.example.command.Command;
import org.example.command.commands.*;
import org.example.managers.CommandManager;
import org.example.network.*;
import org.example.utility.ConsoleInput;
import org.example.utility.ConsoleOutput;

public class Main {
    public static void main(String[] args) throws InterruptedException{

        ConsoleInput consoleInput = new ConsoleInput();
        ConsoleOutput consoleOutput = new ConsoleOutput();
        Client client = new Client("localhost", 7118, 3000, 5, consoleOutput);
        CommandManager commandManager = new CommandManager() {{
            register(new Add(consoleInput, consoleOutput, client));
            register(new AddIfMin(consoleInput, consoleOutput, client));
            register(new Clear(consoleOutput, client));
            register(new Info(consoleOutput, client));
            register(new Show(consoleOutput, client));
            register(new CountByOfficialAddress(consoleOutput, client));
            register(new FilterByAnnualTurnover(consoleOutput, client));
            register(new Help(consoleOutput, client));
            register(new History(consoleOutput, client));
            register(new PrintUniqueAnnualTurnover(consoleOutput, client));
            register(new RemoveByID(consoleOutput, client));
            register(new RemoveFirst(consoleOutput, client));
            register(new UpdateByID(consoleInput,consoleOutput, client));
            register(new ExecuteScript(consoleOutput, client));
            register(new Login(consoleOutput, client));
            register(new Register(consoleInput, consoleOutput, client));
        }};
        client.connect();
        RequestManager requestManager = new RequestManager(consoleInput, consoleOutput, commandManager, client);
        requestManager.run();
    }
}
