package org.example.entity;

import java.io.Serializable;

/**
 * Класс, представляющий адрес с улицей и почтовым индексом.
 *
 * Поле {@code street} не может быть null, а поле {@code zipCode} может быть null.
 */
public class Address implements Serializable {
    /**
     * Улица, не может быть null.
     */
    private String street;
    /**
     * Почтовый индекс, может быть null.
     */
    private String zipCode;

    /**
     * Конструктор для создания объекта {@code Address}.
     *
     * @param street Улица. Не может быть null.
     * @param zipCode Почтовый индекс. Может быть null.
     */
    public Address(String street, String zipCode) {
        this.street = street;
        this.zipCode = zipCode;
    }
    // Геттеры и сеттеры
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Проверяет, является ли объект {@code Address} валидным.
     * Валидным считается адрес, если поле {@code street} не равно null.
     *
     * @return {@code true}, если адрес валиден (т.е. {@code street} не null), иначе {@code false}.
     */
    public boolean validate() {
        return  street != null;
    }
    /**
     * Возвращает строковое представление объекта {@code Address}.
     *
     * @return строковое представление в формате "street - {street}; zipCode - {zipCode}".
     */
    @Override
    public String toString() {
        return "street - "+street +"; zipCode - " + zipCode;
    }
}
