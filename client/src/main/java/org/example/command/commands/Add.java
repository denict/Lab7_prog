package org.example.command.commands;

import org.example.command.Command;
import org.example.entity.Organization;
import org.example.entity.builders.OrganizationBuilder;
import org.example.network.Client;
import org.example.network.Request;
import org.example.network.User;
import org.example.utility.ConsoleInput;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "add".
 * Описание команды: добавление нового элемента в коллекцию.
 */
public class Add extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 53L;
    transient private ConsoleInput consoleInput;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;

    public Add(ConsoleInput consoleInput, ConsoleOutput consoleOutput, Client client) {
        super("add", "добавить новый элемент в коллекцию", 0, "");
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.client = client;
    }

    public void execute(String[] args, User user) throws InterruptedException, NullPointerException {
            Organization organization = new OrganizationBuilder(consoleInput, consoleOutput).build();
            consoleOutput.println(client.sendRequest(new Request(this, organization, user)).getMessage()); // отправляем

    }


}
