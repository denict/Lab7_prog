package org.example.utility;

import java.io.PrintStream;

/**
 * Класс для управления выводом в стандартный поток.
 * <p>
 * Обеспечивает вывод обычных и ошибочных сообщений в консоль.
 * </p>
 */
public class ConsoleOutput implements OutputHandler {

    /**
     * Поток вывода, используемый для печати сообщений.
     */
    private final PrintStream printStream = System.out;
    public Object println;

    /**
     * Выводит строку без перехода на новую строку.
     *
     * @param s строка для вывода
     */
    @Override
    public void print(String s) {
        printStream.print(s);
    }

    /**
     * Выводит строку с переходом на новую строку.
     *
     * @param s строка для вывода
     */
    @Override
    public void println(String s) {
        printStream.println(s);
    }

    /**
     * Выводит сообщение об ошибке с префиксом "Ошибка: ".
     *
     * @param s сообщение об ошибке
     */
    @Override
    public void printError(String s) {
        printStream.print("Ошибка: " + s + "\n");
    }

}
