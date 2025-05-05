package org.example.utility;


import java.io.IOException;

/**
 * Интерфейс реализующий методы readline() - 
 * для построчного считывания ввода и close() - для закрытия потока
 *
 */
public interface InputHandler {

    /**
     * Для построчного прочтения ввода
     * @return считанная строка
     */
    public String readLine();



    /**
     * закрытие потока ввода
     */
    public void close() throws IOException;
}
