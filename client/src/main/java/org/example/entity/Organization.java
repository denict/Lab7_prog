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


    /**
     * Создает Organization из массива строк.
     * @param a 8-элементный массив строк, содержащий id, name, coordinates, creationDate, annualTurnover, type, street, zipCode.
     *          Если какой-либо из элементов массива имеет неправильный формат,
     *          то будет установлен соответствующий по умолчанию.
     * @return созданная Organization, или null, если массив имеет неправильный размер.
     */
//    public static Organization fromArray(String[] a, ConsoleOutput consoleOutput) {
//        Integer id;
//        String name;
//        Coordinates coordinates;
//        Date creationDate;
//        Double annualTurnover;
//        OrganizationType type;
//        Address officialAddress;
//        try {
//            try {
//                id = Integer.parseInt(a[0]);
//                if (CollectionManager.hasIdInCollection(id)) {
//                    id = CollectionManager.generateFreeId();
//                }
//            } catch (NumberFormatException e) {
//                consoleOutput.printError("\"id\" должен быть целочисленным значением типа Integer.");
//                return null;
//            }
//
//            name = a[1];
//            if (name == null || name.isBlank()) {
//                consoleOutput.printError("\"name\" не может быть пустым или null.");
//                return null;
//            }
//            try {
//                Long x = Long.parseLong(a[2]);
//                Long y = Long.parseLong(a[3]);
//                coordinates = new Coordinates(x, y);
//            } catch (NumberFormatException e) {
//                consoleOutput.printError("\"coordinates\" x и y должны быть целочисленными значениями типа Long.");
//                return null;
//            };
//
//            try {
//                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
//                LocalDateTime localDateTime = LocalDateTime.parse(a[4], formatter);
//                creationDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
//            } catch (DateTimeParseException e) {
//                creationDate = new Date();
//            }
//
//            try { annualTurnover = Double.parseDouble(a[5]); } catch (NumberFormatException e) {
//                annualTurnover = Math.random() * Double.MAX_VALUE;
//                consoleOutput.printError("\"annualTurnover\" должен быть числом.");
//                return null;
//            }
//            try { type = a[6].equals("null") ? null : OrganizationType.valueOf(a[6]); } catch (NullPointerException | IllegalArgumentException  e) {
//               type = null;
//                consoleOutput.printError("\"type\" должен быть одним из значений \"OrganizationType\" или null.");
//                return null;
//            }
//            try {
//                if (a[7] == null || a[7].isEmpty()) {
//                    consoleOutput.printError("\"street\" не может быть пустым или null.");
//                    return null;
//                }
//                officialAddress = new Address(a[7], a[8]);
//            } catch (IllegalArgumentException e) {
//                officialAddress = null;
//            }
//            return new Organization(id, name, coordinates, creationDate, annualTurnover, type, officialAddress);
//        } catch (ArrayIndexOutOfBoundsException e) {
//            consoleOutput.printError("Неправильное количество элементов в строке считывания");
//        }
//        return null;
//    }
    /**
     * Преобразует объект Organization в массив строк.
     * @param org Организация для преобразования.
     * @return Массив строк, содержащий данные об организации.
     */
//    public static String[] toArray(Organization org) {
//        ArrayList<String> list = new ArrayList<>();
//        list.add(org.getId().toString());
//        list.add(org.getName());
//        list.add(Long.toString(org.getCoordinates().getX()));
//        list.add(Long.toString(org.getCoordinates().getY()));
//        list.add(org.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME));
//        list.add(org.getAnnualTurnover().toString());
//        list.add(org.getOrganizationType() == null ? "null" : org.getOrganizationType().toString());
//        list.add(org.getOfficialAddress().getStreet());
//        list.add(org.getOfficialAddress().getZipCode());
//        return list.toArray(new String[0]);
//    }
}
