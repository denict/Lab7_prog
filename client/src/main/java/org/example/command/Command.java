package org.example.command;

import java.io.Serial;
import java.io.Serializable;

/**
 * Абстрактный класс, представляющий команду.
 * <p>
 * Каждая команда имеет имя, описание и количество аргументов.
 * </p>
 */
public abstract class Command implements CommandInterface, Serializable {

    @Serial
    private static final long serialVersionUID = 100L;
    private final String name;
    private final String description;
    private final int argsCount;
    private final String usageArg;
    /**
     * Создаёт команду с указанным именем, описанием и количеством аргументов.
     *
     * @param name        название команды
     * @param description описание команды
     * @param argsCount   количество аргументов, которые принимает команда
     */
    public Command(String name, String description, int argsCount, String usageArg) {
        this.name = name;
        this.description = description;
        this.argsCount = argsCount;
        this.usageArg = usageArg;
    }

    /**
     * Возвращает название команды.
     *
     * @return название команды
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает количество аргументов, которые принимает команда.
     *
     * @return количество аргументов
     */
    public int getArgsCount() {
        return argsCount;
    }

    /**
     * Возвращает название параметров, которые принимает команда.
     *
     * @return количество аргументов
     */
    public String getUsageArg() {
        return usageArg;
    }

    /**
     * Проверяет равенство двух команд по их названию и описанию.
     *
     * @param obj объект для сравнения
     * @return {@code true}, если команды равны, иначе {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Command command = (Command) obj;
        return name.equals(command.name) && description.equals(command.description);
    }

    /**
     * Возвращает хеш-код команды, основанный на её названии и описании.
     *
     * @return хеш-код команды
     */
    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    /**
     * Возвращает строковое представление команды в формате ": имя | описание".
     *
     * @return строковое представление команды
     */
    @Override
    public String toString() {
        return String.format(": %-1s | %s", name, description);
    }
}
