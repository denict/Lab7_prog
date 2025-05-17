package org.example.command.commands;

import org.example.command.Command;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

public class Logout extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 200L;
    public Logout() {
        super("logout", "выйти из аккаунта", 0, "");
    }

    @Override
    public Response execute(Request request) {
        return new Response(true, "Вы вышли из аккаунта.");
    }
}
