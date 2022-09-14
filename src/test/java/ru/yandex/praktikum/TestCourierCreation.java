package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.Matchers.equalTo;

public class TestCourierCreation {

//    @Before
//    public void setUp() {
//        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";
//    }


    @Test
    @DisplayName("Check courier creation")
    @Description("check ability to create courier with correct params")
    public void checkCourierCreation() {
        CourierApi courierApi = new CourierApi();
        courierApi.createCourier().then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));
        courierApi.deleteCourier(courierApi.getCourierId());
    }


    @Test
    // тест падает, т.к. поведение отличается от описанного в документации
    // согласно документации возвращаться должна строка "Этот логин уже используется"
    // на практике - "Этот логин уже используется. Попробуйте другой."
    @DisplayName("Check two equal couriers cannot be created")
    @Description("check couriers with equal parameters cannot be created")
    public void checkTwoEqualCouriersCannotBeCreated(){

        CourierApi courierApi = new CourierApi();
        courierApi.createCourier().then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));
        courierApi.createCourier().then()
                .statusCode(409)
                .assertThat().body("message", equalTo("Этот логин уже используется"));

        courierApi.deleteCourier(courierApi.getCourierId());
    }

    @Test
    @DisplayName("Check courier with NO Login cannot be created")
    @Description("check ability to create courier with Incorrect params - NO Login")
    public void checkCourierCreationWithNoLogin() {
        CourierApi courierApi = new CourierApi();
        courierApi.setCourierLogin(null);
        courierApi.createCourier().then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check courier with NO Password cannot be created")
    @Description("check ability to create courier with Incorrect params - NO Password")
    public void checkCourierCreationWithNoPassword() {
        CourierApi courierApi = new CourierApi();
        courierApi.setCourierPass(null);
        courierApi.createCourier().then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check courier with NO FirstName can be created")
    @Description("check ability to create courier with Incorrect params - NO FirstName")
    public void checkCourierCreationWithNoFirstName() {
        CourierApi courierApi = new CourierApi();
        courierApi.setFirstName(null);
        courierApi.createCourier().then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));
        courierApi.deleteCourier(courierApi.getCourierId());
    }

    @Test
    @DisplayName("Check courier with NO params cannot be created")
    @Description("check ability to create courier with Incorrect params - NO Params")
    public void checkCourierCreationWithNoParams() {
        Courier testCourier = new Courier();
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(testCourier)
                .baseUri(ApiClient.BASE_URL)
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}

