package org.example.network;

import java.io.Serial;
import java.io.Serializable;
import java.util.Scanner;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 152L;

    private String login;
    private String password;

    public User(String username, String password) {
        this.login = username;
        this.password = password;
    }

//    public boolean isLogin() {
//        Scanner scanner = new Scanner(System.in);
//        String line;
//        while (true) {
//            System.out.println("Вы зарегистрированы? (y/n)");
//            line = scanner.nextLine();
//            switch (line) {
//                case "y", "yes", "Y", "YES", "ДА", "да" -> {
//                    return true;
//                }
//                case "n", "N", "no", "NO", "нет", "НЕТ" -> {
//                    return false;
//                }
//                default -> {
//                    System.out.println("Неверный ввод");
//                }
//            }
//        }
//    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
