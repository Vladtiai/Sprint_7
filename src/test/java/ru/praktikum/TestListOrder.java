package ru.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestListOrder {

    TestUtils testUtils = new TestUtils();

    @BeforeEach
    void setUp() {
        testUtils.setupUrl();
    }

    @Test
    @DisplayName("Проверка, что тело ответа содержит список заказов")
    @Step("Получение списка заказов и проверка структуры ответа")
    void checkBodyResponseNotNull() {
        Response response = given()
                .get("/api/v1/orders");

        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", instanceOf(List.class))
                .body("orders.size()", greaterThan(0));
    }
}
