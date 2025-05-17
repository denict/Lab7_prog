package org.example.entity;

import org.example.utility.Validateable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Класс, представляющий организацию.
 * Реализует интерфейсы Serializable и Comparable для поддержки сериализации
 * и сравнения по годовому доходу.
 */
public class Organization implements Serializable, Comparable<Organization>, Validateable {
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
            this.name = name;
            this.coordinates = coordinates;
            this.annualTurnover = annualTurnover;
            this.type = type;
            this.officialAddress = officialAddress;
    }

    public Organization(String name, Coordinates coordinates, Date creationDate, Double annualTurnover, OrganizationType type, Address officialAddress) {
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
     * Проверяет корректность значений полей организации.
     * @return true, если организация корректна, иначе false.
     */
    public boolean validate() {
        if (id == null || id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (creationDate == null) return false;
        if (annualTurnover == null || annualTurnover <= 0) return false;
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

    public Integer getId(){
        return id;
    } 

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreationDate(Date creationDate){
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
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
