package com.hopsquad.hopsquadapp.api;

import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.models.HSUser;
import com.hopsquad.hopsquadapp.models.HistoryOrder;
import com.hopsquad.hopsquadapp.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by memo on 15/11/17.
 * Here all our api entry points are declared.
 * For more info see: http://square.github.io/retrofit/
 */

public interface Webservice {

    @GET("/api/get_all_beers")
    Call<List<Beer>> getTapList();

    @GET("/api/get_beer")
    Call<Beer> getBeer(@Body int beerId);

    @GET("/api/get_all_orders")
    Call<List<HistoryOrder>> getAllOrders();

    @GET("/api/get_all_orders_by_user")
    Call<List<HistoryOrder>> getAllOrdersByUserId(String userId);

    @POST("/api/add_order")
    Call<Order> addOrder(@Body Order order);

    @POST("/api/add_user")
    Call<HSUser> addUser(@Body HSUser user);
}
