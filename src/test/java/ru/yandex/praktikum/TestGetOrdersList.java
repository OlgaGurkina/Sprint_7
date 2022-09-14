package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;


import static org.hamcrest.Matchers.*;

public class TestGetOrdersList {



    @Test
    //тест падает, т.к. при привязкке заказа к курьеру создается две записи
    //которые отличаются ид (у 1й - ид = orderID,  cоответствующий созднному заказу, a у 2й ид = orderID+1)
    @DisplayName("get order list for Courier")
    @Description("create Courier > Create Order > assign Order to Courier > get Order list for Courier")
    public void getOrderListForCourier(){
        //create courier and get his ID
        CourierApi courierApi = new CourierApi();
        courierApi.createCourier().then().statusCode(201);
        int courierId = Integer.parseInt(courierApi.getCourierId());
        //accept Order by Courier (assign order to courier)
        courierApi.acceptOrderByCourier(courierId,courierApi.getOrderId(courierApi.getOrderTrackId()));

        //get order list for Courier
        //тест падает, т.к. при привязкке заказа к курьеру создается две записи
        //которые отличаются ид (у 1й - ид = orderID,  cоответствующий созднному заказу, a у 2й ид = orderID+1)
        courierApi.getCourierOrdersList(courierId).then()
                .assertThat().body("orders.id", contains(courierApi.getOrderId(courierApi.getOrderTrackId())))
                .and()
                .statusCode(200);

        //complete created order
        courierApi.completeOrder(courierApi.getOrderId(courierApi.getOrderTrackId()));

        //delete created courier
        courierApi.deleteCourier(courierApi.getCourierId());

    }

}
