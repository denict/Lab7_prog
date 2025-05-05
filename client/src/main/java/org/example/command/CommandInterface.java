package org.example.command;


import org.example.network.User;

/**
 * Интерфейс для Command, реализующий метод execute
 */
public interface CommandInterface {

    void execute(String[] args, User user) throws InterruptedException;

}
