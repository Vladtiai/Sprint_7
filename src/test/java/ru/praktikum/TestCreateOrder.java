package ru.praktikum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.Matchers.notNullValue;

import static io.restassured.RestAssured.given;

public class TestCreateOrder {

    TestUtils testUtils = new TestUtils();

    @BeforeEach
    void setUp() {
        testUtils.setupUrl();
    }


    @ParameterizedTest
    @CsvSource({
            "BLACK",
            "GREY",
            "BLACK,GREY",
            "NO_COLOR",
            "EMPTY"
    })
    @DisplayName("Создание заказа с разными вариантами цветов")
    @Step("Создание заказа с параметром цвета: {0}" )
    public void createOrder(String colors){

        String[] actualColor;

        if (colors.equals("NO_COLOR")) {
            actualColor = null;
        } else if (colors.equals("EMPTY")) {
            actualColor = new String[]{};
        } else {
            actualColor = colors.split(",");  // "BLACK" или "BLACK,GREY"
        }

        Order order = new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                actualColor);

        given()
                .header("Content-type", "application/json")
                .body(order)
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
    }





