package org.example.command.commands;

import org.example.command.Command;
import org.example.managers.CommandManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда "history".
 * Описание команды: выводит названия пяти последних выполненных команд.
 */
public class History extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 60L;
    private CommandManager commandManager;
    private ConsoleOutput consoleOutput;

    public History(CommandManager commandManager, ConsoleOutput consoleOutput) {
        super("history", "выводит названия пяти последних выполненных команд", 0, "");
        this.commandManager = commandManager;
        this.consoleOutput = consoleOutput;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    @Override
    public Response execute(Request request) {
        List<Command> history = commandManager.getCommandHistory();
        if (history.isEmpty()) {
            commandManager.addToHistory(this);
            return new Response(false, "В истории хранится 0 команд. Похоже, это первая команда.");
        }

        List<Command> tmpHistory = history.stream()
                .skip(history.size() - Math.min(5, history.size()))
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (Command h: tmpHistory) {
            sb.append((i+1) + ". " + h.toString() + "\n");
//            consoleOutput.println((i+1) + ". " + history.get(i).toString());
            i++;
        }
        return new Response(true, sb.toString());
    }

}
