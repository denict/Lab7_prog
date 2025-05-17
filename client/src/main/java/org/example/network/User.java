package org.example.network;

import java.io.Serial;
import java.io.Serializable;


public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 152L;

    private String login;
    private String password;

    public User(String username, String password) {
        this.login = username;
        this.password = password;
    }


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
