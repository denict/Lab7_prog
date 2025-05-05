package org.example.managers;

import org.example.utility.InputHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.HashSet;

/**
 * Менеджер для контроля выполнения исполняемых скриптов.
 * <p>
 * Этот класс отслеживает файлы, которые были запущены в рамках выполнения скрипта, предотвращая рекурсивные вызовы.
 * Также предоставляет методы для чтения ввода из файла.
 * </p>
 */
public class RunnerScriptManager implements InputHandler {
    /**
     * Набор запущенных файлов, используемый для предотвращения рекурсивного запуска.
     */
    private static final HashSet<String> setFiles = new HashSet<>();

    /**
     * Очередь потоков чтения для управления последовательным выполнением скриптов.
     */
    private static final ArrayDeque<BufferedReader> readers = new ArrayDeque<>();

    /**
     * Текущий поток чтения.
     */
    private static BufferedReader br;

    /**
     * Проверяет, был ли указанный файл уже запущен.
     *
     * @param fileName Имя проверяемого файла.
     * @return {@code true}, если файл уже выполняется, иначе {@code false}.
     */
    public static boolean checkIfFileInStack(String fileName) {
        return setFiles.contains(fileName);
    }

    /**
     * Добавляет файл в список запущенных и открывает поток для чтения его содержимого.
     *
     * @param fileName Имя файла, который необходимо добавить.
     * @throws FileNotFoundException Если файл не найден.
     */
    public static void addFile(String fileName) throws FileNotFoundException {
        setFiles.add(fileName);
        readers.add(br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8)));
    }

    /**
     * Удаляет файл из списка запущенных.
     *
     * @param fileName Имя файла, который необходимо удалить из списка.
     */
    public static void removeFile(String fileName) {
        setFiles.remove(fileName);
    }

    /**
     * Читает строку из текущего потока ввода, перенаправленного на файл.
     *
     * @return Прочитанная строка или сообщение об ошибке, если файл не найден.
     */
    @Override
    public String readLine() {
        try {
            return readers.getLast().readLine();
        } catch (IOException e) {
            return "Не найден файл";
        }
    }
}
