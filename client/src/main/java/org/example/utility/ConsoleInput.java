package org.example.utility;

import org.example.managers.InputManager;

import java.util.Scanner;

/**
 * Класс для контроля пользовательского ввода.
 * <p>
 * Обрабатывает ввод пользователя как с консоли, так и из файла.
 * </p>
 */
public class ConsoleInput implements InputHandler {

    /**
     * Глобальный сканер для считывания ввода.
     */
    private static final Scanner scanner = InputManager.scanner;

    /**
     * Флаг, указывающий, осуществляется ли ввод из файла.
     */
    private static boolean fileMode = false;

    /**
     * Проверяет, включен ли режим чтения из файла.
     *
     * @return {@code true}, если ввод осуществляется из файла, иначе {@code false}.
     */
    public static boolean isFileMode() {
        return fileMode;
    }

    /**
     * Устанавливает режим чтения из файла.
     *
     * @param fileMode {@code true}, если ввод должен осуществляться из файла, иначе {@code false}.
     */
    public static void setFileMode(boolean fileMode) {
        ConsoleInput.fileMode = fileMode;
    }

    /**
     * Считывает строку из пользовательского ввода.
     *
     * @return Введённая строка.
     */
    @Override
    public String readLine() {
        return scanner.nextLine().strip();
    }

}
