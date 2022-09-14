package ru.yandex.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;

public class CourierApi {

    private RandomParamsForCouriers params = new RandomParamsForCouriers();
    private Courier testCourier = new Courier(params.generatedLogin, params.generatedPassword, params.generatedFirstName);
    private LoginData loginData = new LoginData(params.generatedLogin, params.generatedPassword);

    private RandomParamsForOrder orderParam = new RandomParamsForOrder();
    List<String> color = List.of("GREY");
    private Orders order = new Orders(orderParam.customerFirstName,
            orderParam.customerLastName,
            orderParam.customerAddress,
            orderParam.customerMetroStation,
            orderParam.customerPhone,
            orderParam.customerRentTime,
            orderParam.customerDeliveryDate,
            orderParam.customComment, color );



    public void setFirstName(String name){
        testCourier.setFirstname(name);
    }
    public void setCourierLogin(String login){
        testCourier.setLogin(login);
    }
    public void setCourierPass(String pass){
        testCourier.setPassword(pass);
    }
    public void setLogin(String login){
        loginData.setLogin(login);
    }
    public void setPassword(String pass){
        loginData.setPassword(pass);
    }

    public Response createCourier() {
        Response response = RestAssured.with()
                .header("Content-Type", "application/json")
                .baseUri(ApiClient.BASE_URL)
                .body(testCourier)
                .post("/api/v1/courier");
        return response;
    }

    public String getCourierId(){
        int id = RestAssured.with()
                .header("Content-Type", "application/json")
                .baseUri(ApiClient.BASE_URL)
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract().body().path("id");
        return Integer.toString(id);
    }
     public void deleteCourier(String id){
         RestAssured.with()
                 .header("Content-Type", "application/json")
                 .baseUri(ApiClient.BASE_URL)
                 .delete("/api/v1/courier/{id}", id)
                 .then()
                 .statusCode(200);

     }

    public Response courierLogin(){
         Response response = RestAssured.with()
            .header("Content-Type", "application/json")
            .baseUri(ApiClient.BASE_URL)
            .body(loginData)
            .post("/api/v1/courier/login");
          return response;
   }
   public int getOrderTrackId(){
        int orderTrack = RestAssured.with()
                .header("Content-Type", "application/json")
                .baseUri(ApiClient.BASE_URL)
                .body(order)
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .extract().body().path("track");
        return orderTrack;}

    public int getOrderId(int orderTrack){
       int orderID = RestAssured.with()
               .header("Content-Type", "application/json")
               .queryParam("t", orderTrack)
               .baseUri(ApiClient.BASE_URL)
               .get("/api/v1/orders/track")
               .then()
               .statusCode(200)
               .extract().body().path("order.id");
       return orderID;
    }

    public void acceptOrderByCourier(int courierID, int orderID){
        RestAssured.with()
                .header("Content-Type", "application/json")
                .queryParam("courierId", courierID)
                .baseUri(ApiClient.BASE_URL)
                .put("/api/v1/orders/accept/{id}", orderID)
                .then()
                .statusCode(200);
    }

    public Response getCourierOrdersList(int courierID){
        Response response = RestAssured.with()
                .header("Content-Type", "application/json")
                .queryParam("courierId", courierID)
                .baseUri(ApiClient.BASE_URL)
                .get("/api/v1/orders");
        return response;
    }

    public void completeOrder(int orderID){
        RestAssured.with()
                .header("Content-Type", "application/json")
                .baseUri(ApiClient.BASE_URL)
                .put("/api/v1/orders/finish/{id}", orderID)
                .then()
                .statusCode(200);
    }
}
