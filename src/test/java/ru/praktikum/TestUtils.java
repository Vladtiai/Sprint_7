package ru.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Random;
import static io.restassured.RestAssured.given;

public class TestUtils {



    // Общий метод вызова ручки создания курьера
    private String createdLogin;
    private String createdPassword;
    private String createdName;
    private Random random = new Random();

    // Общий setUp
    public void setupUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    // Общий метод генерации тестовых данных курьера
    public void setupCourierData() {
        int randomInt = random.nextInt(10000);
        createdLogin = "VladTiai" + randomInt;
        createdPassword = String.valueOf(randomInt);
        createdName = "Vlad" + randomInt;

        System.out.println("✅ Генерация тестовых данных: [Логин: " + createdLogin +
                ", Пароль: " + createdPassword + ", Имя: " + createdName + "]");
    }

    // Общий метод создания курьера
    public Response createCourier(String login, String password, String firstName) {
        return given()
                .header("Content-type", "application/json")
                .body(new Courier(login, password, firstName))
                .post("/api/v1/courier");
    }


    // Общий метод авторизации
    public Response authorization(String login, String password){
        String json = String.format("{\"login\":\"%s\",\"password\":\"%s\"}", login, password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier/login");
        return response;
    }

    // Общий метод для удаления тестовых данных
    public void deleteTestData() {
        if (createdLogin != null && createdPassword != null) {
            Response loginResponse = authorization(createdLogin, createdPassword);
            if (loginResponse.statusCode() == 200) {
                int idUser = loginResponse.jsonPath().getInt("id");
                given()
                        .delete("/api/v1/courier/" + idUser)
                        .then().statusCode(200);
                System.out.println("✅ Учётка удалена: " + createdLogin);
            }
        }
    }

    public String getCreatedLogin() {
        return createdLogin;
    }

    public String getCreatedPassword() {
        return createdPassword;
    }

    public String getCreatedName() {
        return createdName;
    }
}
