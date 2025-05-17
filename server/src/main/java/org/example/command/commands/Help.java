package org.example.command.commands;

import org.example.command.Command;
import org.example.managers.CommandManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * Команда "help".
 * Описание команды: вывод справки о доступных командах.
 */
public class Help extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 59L;
    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", "вывод справки о доступных командах", 0, "");
        this.commandManager = commandManager;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    public Response execute(Request request) {
        StringBuilder sb = new StringBuilder("");
        sb.append("Краткая справка по всем доступным командам.\n");

        // Stream API
        commandManager.getCommands().entrySet().stream()
                .filter(entry -> !entry.getKey().equals("login") && !entry.getKey().equals("register") && !entry.getKey().equals("check_by_id"))
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        sb.append(entry.getKey() + " - " + entry.getValue().getDescription() + "\n")
                );
        return new Response(true, sb.toString());
//        for (Map.Entry<String, Command> entry : commandManager.getCommands().entrySet()) {
//            String commandName = entry.getKey();
//            Command command = entry.getValue();
//            consoleOutput.println(commandName + " - " + command.getDescription());
//        }
    }
}

