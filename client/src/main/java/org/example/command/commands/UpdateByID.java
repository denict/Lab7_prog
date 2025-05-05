package org.example.command.commands;

import org.example.command.Command;
import org.example.entity.builders.OrganizationBuilder;
import org.example.network.Client;
import org.example.network.Request;
import org.example.network.User;
import org.example.utility.ConsoleInput;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Команда "update_by_id".
 * Описание команды: обновить значение элемента коллекции, id которого равен заданному
 */
public class UpdateByID extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 65L;
    transient private ConsoleInput consoleInput;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;


    public UpdateByID(ConsoleInput consoleInput, ConsoleOutput consoleOutput, Client client) {
        super("update_by_id", "обновить значение элемента коллекции, id которого равен заданному", 1, "\"id\"");
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.client = client;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    @Override
    public void execute(String[] args, User user) throws InterruptedException, NullPointerException {
        var organization = new OrganizationBuilder(consoleInput, consoleOutput).build();
        organization.setId(Integer.parseInt(args[0]));
        consoleOutput.println(client.sendRequest(new Request(organization, this, args, user)).getMessage());
    }
}
