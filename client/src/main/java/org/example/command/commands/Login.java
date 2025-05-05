package org.example.command.commands;

import org.example.command.Command;
import org.example.network.Client;
import org.example.network.Request;
import org.example.network.User;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

public class Login extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 153L;
    private transient ConsoleOutput consoleOutput;
    private transient Client client;

    public Login(ConsoleOutput consoleOutput, Client client) {
        super("login", "войти в систему", 0, "");
        this.consoleOutput = consoleOutput;
        this.client = client;
    }

    @Override
    public void execute(String[] args, User user) throws InterruptedException, NullPointerException {
//        consoleOutput.println(client.sendRequest(new Request(this, user)).getMessage());
        return;
    }
}
