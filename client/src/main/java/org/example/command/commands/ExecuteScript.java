package org.example.command.commands;

import org.example.command.Command;
import org.example.network.Client;
import org.example.network.Request;
import org.example.network.User;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "execute_script".
 * Описание команды: считать и исполнить скрипт из указанного файла.
 */
public class ExecuteScript extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 57L;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;

    public ExecuteScript(ConsoleOutput consoleOutput, Client client) {
        super("execute_script", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.", 1, "\"file_script\"");
        this.consoleOutput = consoleOutput;
        this.client = client;
    }

    @Override
    public void execute(String[] args, User user) throws InterruptedException, NullPointerException {
        consoleOutput.println(client.sendRequest(new Request(this, args, user)).getMessage());
    }
}
