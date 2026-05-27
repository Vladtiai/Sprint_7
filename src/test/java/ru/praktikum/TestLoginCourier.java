package ru.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class TestLoginCourier {

    TestUtils testUtils = new TestUtils();

    @BeforeEach
    @DisplayName("Генерация тестовых данных")
    void setUp() {
        testUtils.setupUrl();
        testUtils.setupCourierData();

        testUtils.createCourier(
                testUtils.getCreatedLogin(),
                testUtils.getCreatedPassword(),
                testUtils.getCreatedName());
    }

    //=========================================

    @Test
    @DisplayName("Успешная авторизация возвращает статус 200 и ID")
    void authorizationSuccessWithChecks() {
        Response authResponse = testUtils.authorization(
                testUtils.getCreatedLogin(),
                testUtils.getCreatedPassword()
        );
        authResponse.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    //=========================================

    @Test
    @DisplayName("Проверка обязательных полей для авторизации")
    void checkMandatoryAuthorizationEmptyFields(){
        checkAuthorizationWithEmptyLogin();
        checkAuthorizationWithEmptyPassword();
        checkAuthorizationWithEmptyAll();
    }

    @Step("Пустой login")
    void checkAuthorizationWithEmptyLogin(){
        Response authResponse = testUtils.authorization(
                "",
                testUtils.getCreatedPassword()
        );
        authResponse.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
        }

    @Step("Не передаем password")
    void checkAuthorizationWithEmptyPassword(){
        Response authResponse = testUtils.authorization(
                testUtils.getCreatedLogin(),
                ""
        );
        authResponse.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Не передаем login и password")
    void checkAuthorizationWithEmptyAll(){
        Response authResponse = testUtils.authorization(
                "",
                ""
        );
        authResponse.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    //=========================================

    @Test
    @DisplayName("Запрос с несуществующей парой логин-пароль")
    void checkAuthorizationWithWrongData(){
        checkAuthorizationWithWrongLogin();
        checkAuthorizationWithWrongPassword();
        checkAuthorizationWithWrongLoginAndPassword();
    }

    @Step("Передаем несуществующий логин")
    void checkAuthorizationWithWrongLogin(){
        Response authResponse = testUtils.authorization(
                testUtils.getCreatedLogin()+ "1",
                testUtils.getCreatedPassword()
        );
        authResponse
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Передаем несуществующий пароль")
    void checkAuthorizationWithWrongPassword(){
        Response authResponse = testUtils.authorization(
                testUtils.getCreatedLogin(),
                testUtils.getCreatedPassword()+ "1"
        );
        authResponse
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Передаем несуществующиую пару логин-пароль")
    void checkAuthorizationWithWrongLoginAndPassword(){
        Response authResponse = testUtils.authorization(
                testUtils.getCreatedLogin()+ "1",
                testUtils.getCreatedPassword()+ "1"
        );
        authResponse
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    //=========================================

    @AfterEach
    @DisplayName("Удаление тестовых данных")
    void delete(){
        testUtils.deleteTestData();

    }
}