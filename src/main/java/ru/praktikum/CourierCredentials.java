package ru.praktikum;

public class CourierCredentials {
    private String login;
    private String password;

    public CourierCredentials(String login, String password){
        this.login = login;
        this.password = password;
    }

    // геттеры
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
