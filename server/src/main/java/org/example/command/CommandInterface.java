package org.example.command;

import org.example.network.Request;
import org.example.network.Response;

/**
 * Интерфейс для Command, реализующий метод execute
 */
public interface CommandInterface {


    Response execute(Request request);

}
