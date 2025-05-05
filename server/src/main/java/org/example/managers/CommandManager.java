package org.example.managers;

import org.example.command.Command;

import java.util.*;

/*
 * Отвечает за управление командами
 */
public class CommandManager {

    /**
     * Словарь команд, упорядоченный по имени команды.
     */
    private static final HashMap<String, Command> commands = new LinkedHashMap<>(); // Linked HashMap сохраняет порядок добавления команд
    /**
     * История команд, выполняемых в системе.
     */
    private final List<Command> commandHistory = new ArrayList<>();

    /**
     * Регистрация команды с её названием
     *
     *
     * @param command Команда
     */
    public void register(Command command) {
        this.commands.put(command.getName(), command);
    }




    static public Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    /**
     * @return Упорядоченный словарь команд
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * @return История команд
     */
    public List<Command> getCommandHistory() {
        return commandHistory;
    }

    /**
     * Добавление команды в историю
     * @param command Команда.
     */
    public void addToHistory(Command command) {
        commandHistory.add(command);
    }
}



