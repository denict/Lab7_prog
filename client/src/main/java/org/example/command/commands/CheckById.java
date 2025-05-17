package org.example.command.commands;

import org.example.command.Command;
import org.example.network.Client;
import org.example.network.Request;
import org.example.network.User;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

public class CheckById extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 201L;
    private final transient ConsoleOutput consoleOutput;
    private final transient Client client;

    public CheckById(ConsoleOutput consoleOutput, Client client) {
        super("check_by_id", "проверка существования элемента с заданным id и принадлежности его к текущему пользователю", 1, "id");
        this.consoleOutput = consoleOutput;
        this.client = client;
    }

    @Override
    public void execute(String[] args, User user) {
        consoleOutput.println(client.sendRequest(new Request(this, args, user)).getMessage());
    }
}
