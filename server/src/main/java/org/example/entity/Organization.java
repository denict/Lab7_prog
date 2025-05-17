package org.example.entity;
import org.example.managers.CollectionManager;
import org.example.utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Класс, представляющий организацию.
 * Реализует интерфейсы Serializable и Comparable для поддержки сериализации
 * и сравнения по годовому доходу.
 */
public class Organization implements Serializable, Comparable<Organization>{
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Double annualTurnover; //Поле не может быть null, Значение поля должно быть больше 0
    private OrganizationType type; //Поле может быть null
    private Address officialAddress; //Поле может быть null

    public Organization(String name, Coordinates coordinates, Double annualTurnover, OrganizationType type, Address officialAddress) {
            this.id = CollectionManager.generateFreeId();
            this.name = name;
            this.coordinates = coordinates;
            this.creationDate = new Date();
            this.annualTurnover = annualTurnover;
            this.type = type;
            this.officialAddress = officialAddress;
    }

    public Organization(Integer id, String name, Coordinates coordinates, Date creationDate, Double annualTurnover, OrganizationType type, Address officialAddress) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.annualTurnover = annualTurnover;
        this.type = type;
        this.officialAddress = officialAddress;
    }


    /**
     * Возвращает строковое представление организации.
     * @return Строка с данными об организации.
     */
    @Override
    public String toString() {
        return "Organization:\n" + 
        "id: " + id + "\n" + 
        "name: " + name + "\n" +
        "coordinates: " + coordinates + "\n" +
        "creationDate: " + creationDate + "\n" + 
        "annualTurnover: " + annualTurnover + "\n" +
        "type: " + type + "\n" +
        "officialAddress: " + officialAddress;

    }

    /**
     * Проверяет корректность значений полей организации. (Без автоматически генерируемых полей)
     * @return true, если организация корректна, иначе false.
     */
    public boolean validate() {
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (annualTurnover == null || annualTurnover <= 0) return false;
        if (!officialAddress.validate()) return false;
        return true;
    }
    /**
     * Сравнивает организации по годовому доходу.
     * @param organization Другая организация.
     * @return Положительное число, если текущий объект больше,
     *         отрицательное, если меньше, и 0, если равны.
     */
    @Override
    public int compareTo(Organization organization) {
        return (int) (this.annualTurnover.compareTo(organization.annualTurnover));
    }

    // Геттеры и сеттеры

    public int getId(){
        return id;
    } 

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setCreationDate(Date date) {
        this.creationDate = date;
    }


    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Double getAnnualTurnover() {
        return annualTurnover;
    }


    public OrganizationType getOrganizationType() {
        return type;
    }


    public Address getOfficialAddress() {
        return officialAddress;
    }



}
