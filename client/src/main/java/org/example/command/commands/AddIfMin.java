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
 * Команда "add_if_min".
 * Описание команды: добавить новый элемент, если его значение меньше, чем у наименьшего элемента коллекции.
 */
public class AddIfMin extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 54L;
    transient private ConsoleInput consoleInput;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;



    public AddIfMin(ConsoleInput consoleInput, ConsoleOutput consoleOutput,Client client) {
        super("add_if_min", "добавить элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", 0, "");
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
        Organization organization = new OrganizationBuilder(consoleInput, consoleOutput).build();
        consoleOutput.println(client.sendRequest(new Request(this, organization, user)).getMessage());
    }
}
