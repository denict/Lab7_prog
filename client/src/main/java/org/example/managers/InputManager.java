package org.example.managers;
import java.io.InputStream;
import java.util.Scanner;


/**
 * Менеджер для хранения входящих потоков
 */
public class InputManager {
    public static final InputStream inputStream = System.in; // InputStream - для работы с потоками байтов
    public static final Scanner scanner = new Scanner(inputStream);
}
