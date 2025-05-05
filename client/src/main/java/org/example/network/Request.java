package org.example.network;
import org.example.command.Command;
import org.example.entity.Organization;

import java.io.Serial;
import java.io.Serializable;
public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;
    public Organization organization;
    Command command;
    Object args;
    User user;

    public Request(Command command) {
        this.command = command;
    }

    public Request (String string) {
        this.args = string;
    }

    public Request(Command command, Object args) {
        this.command = command;
        this.args = args;
    }

    public Request(Command command, User user) {
        this.command = command;
        this.user = user;
    }

    public Request(Command command, Object args, User user) {
        this.command = command;
        this.args = args;
        this.user = user;
    }

    public Request(Command command, Organization organization, User user) {
        this.organization = organization;
        this.command = command;
        this.user = user;
    }

    public Request(Organization organization, User user) {
        this.organization = organization;
        this.user = user;
    }

    public Request(Organization organization, Command command, Object args, User user) {
        this.organization = organization;
        this.command = command;
        this.args = args;
        this.user = user;
    }

    public Request(Command command, Organization organization) {
        this.command = command;
        this.organization = organization;
    }

    public Request(Organization organization) {
        this.organization = organization;
    }

    public Request(Command command, Organization organization, Object args) {
        this.command = command;
        this.organization = organization;
        this.args = args;
    }
    public Request(Organization organization, Command command, User user) {
        this.organization = organization;
        this.command = command;
        this.user = user;
    }

    public Command getCommand() {
        return command;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Object getArgs() {
        return args;
    }
}