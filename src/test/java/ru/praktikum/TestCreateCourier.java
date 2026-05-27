package ru.praktikum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;

public class TestCreateCourier {

    TestUtils testUtils = new TestUtils();

    @BeforeEach
    @Step("Генерация тестовых данных")
    void setUp() {
        testUtils.setupUrl();
        testUtils.setupCourierData();
    }


    @Test
    @DisplayName("Проверка условия - курьера можно создать")
    void createCourierSuccess() {
        testUtils.createCourier(
                testUtils.getCreatedLogin(),
                testUtils.getCreatedPassword(),
                testUtils.getCreatedName()
        )
                .then().statusCode(201).body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка условия - Нельзя создать двух одинаковых курьеров")
    void createDuplicateLogin() {
        createFirstCourier();
        createDuplicateCourier();
    }

    @Step("Создание первого курьера")
    void createFirstCourier() {
        testUtils.createCourier(
                testUtils.getCreatedLogin(),
                testUtils.getCreatedPassword(),
                testUtils.getCreatedName()
                )
                .then().statusCode(201).body("ok", equalTo(true));
    }

    @Step("Создание дубликата курьера")
    void createDuplicateCourier() {
        testUtils.createCourier(
                testUtils.getCreatedLogin(),
                testUtils.getCreatedPassword(),
                testUtils.getCreatedName()
                )
                .then().statusCode(409);
    }


    @Test
    @DisplayName("Проверка условия - чтобы создать курьера, нужно передать в ручку все обязательные поля")
    public void createWithoutInput() {
        createWithoutLogin();
        createWithoutPassword();
        createWithoutFirstName();
    }

    @Step("Без логина")
    void createWithoutLogin() {
        testUtils.createCourier(
                null,
                testUtils.getCreatedPassword(),
                testUtils.getCreatedName()
                )
                .then().statusCode(400);
    }

    @Step("Без пароля")
    void createWithoutPassword() {
        testUtils.createCourier(
                testUtils.getCreatedLogin(),
                null,
                testUtils.getCreatedName()
                )
                .then().statusCode(400);
    }

    @Step("Без имени")
    void createWithoutFirstName() {
        testUtils.createCourier(
                testUtils.getCreatedLogin(),
                testUtils.getCreatedPassword(),
                null
                )
                .then().statusCode(201);
    }

    @AfterEach
    @Step("Удаление тестовых данных")
    void delete(){
        testUtils.deleteTestData();
    }
}