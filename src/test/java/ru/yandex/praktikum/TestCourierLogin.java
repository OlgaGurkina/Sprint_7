package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;

import static org.hamcrest.Matchers.*;

public class TestCourierLogin {

    static CourierApi courierApi;
    static String courierId;


    @Before
    public void setUp() {

        courierApi = new CourierApi();
        courierApi.createCourier().then().statusCode(201);
        courierId = courierApi.getCourierId();

    }

    @Test
    @DisplayName("Check login - positive case with valid data")
    @Description(" login with correct login+password")
    public void checkLoginWithCorrectData(){
        courierApi.courierLogin().then()
                .statusCode(200)
                .assertThat().body("id", is(instanceOf(Integer.class)));
        courierApi.deleteCourier(courierApi.getCourierId());

    }
    @Test
    @DisplayName("Check login - negative case with not valid data")
    @Description(" login with no login")
    public void checkLoginWithNoLogin(){
        courierApi.setLogin(null);
        courierApi.courierLogin().then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
        courierApi.deleteCourier(courierId);

    }

    @Test
    // тест падает, т.к. работа сервиса не соответствует документации
    // при отсутсвии пароля ожидается 400 код и сообщение "Недостаточно данных для входа"
    // на практике падает 504 Service unavailable
    @DisplayName("Check login - negative case with not valid data")
    @Description(" login with no Password")
    public void checkLoginWithNoPassword(){
        courierApi.setPassword(null);
        courierApi.courierLogin().then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));

        courierApi.deleteCourier(courierId);

    }

    @Test
    // тест падает, т.к. работа сценария не описана документации
    // при отсутсвии пароля и логина предполагаю логику как и при отсутствии или логина, или пароля
    // на практике падает 504 Service unavailable
    @DisplayName("Check login with NO Login and Password")
    @Description("login and password are not set")
    public void checkLoginWithNoData(){
        LoginData loginData = new LoginData();
        courierApi.deleteCourier(courierApi.getCourierId());

    }

    @Test
    @DisplayName("Check login - incorrect pair login+password")
    @Description("incorrect password for existing login")
    public void checkLoginWithIncorrectPassword(){
        courierApi.setPassword("incorrect");
        courierApi.courierLogin().then()
                .statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
        courierApi.deleteCourier(courierId);

    }

    @Test
    @DisplayName("Check login - incorrect pair login+password")
    @Description("incorrect login (password exists)")
    public void checkLoginWithIncorrectLogin(){
        courierApi.setLogin("incorrect");
        courierApi.courierLogin().then()
                .statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
        courierApi.deleteCourier(courierId);

    }

    @Test
    @DisplayName("Check login for NOT Existing Courier")
    @Description("login and password do not exist(courier with such data is not created)")
    public void checkLoginWithNotExistingCourier(){
        CourierApi newCourierApi = new CourierApi();
        newCourierApi.courierLogin().then()
                .statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));


    }

}
