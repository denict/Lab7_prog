package org.example.command.commands;

import org.example.command.Command;
import org.example.entity.builders.UserBuilder;
import org.example.network.Client;
import org.example.network.Request;
import org.example.network.User;
import org.example.utility.ConsoleInput;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

public class Register extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 155L;
    transient private ConsoleInput consoleInput;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;

    public Register(ConsoleInput consoleInput, ConsoleOutput consoleOutput, Client client) {
        super("register", "зарегистрировать нового пользователя", 0, "");
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.client = client;
    }

    @Override
    public void execute(String[] args, User user) throws InterruptedException, NullPointerException {
    }
}