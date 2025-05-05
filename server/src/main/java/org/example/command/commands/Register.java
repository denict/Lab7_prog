package org.example.command.commands;

import org.example.command.Command;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

public class Register extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 155L;

    private final DataBaseManager dataBaseManager;

    public Register(DataBaseManager dataBaseManager) {
        super("register", "Зарегистрировать пользователя", 0, "");
        this.dataBaseManager = dataBaseManager;
    }


    @Override
    public Response execute(Request request) {
        if (!dataBaseManager.existUser(request.getUser())) {
            dataBaseManager.addUser(request.getUser());
            return new Response(true, "Регистрация успешна!");
        }

        return new Response(false, "Регистрация не прошла, логин пользователя уже занят, повторите попытку!" + dataBaseManager.existUser(request.getUser()));
    }
}
