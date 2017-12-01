package com.hopsquad.hopsquadapp.api;

import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.models.HSUser;
import com.hopsquad.hopsquadapp.models.HistoryOrder;
import com.hopsquad.hopsquadapp.models.Order;
import com.hopsquad.hopsquadapp.models.StripeInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by memo on 15/11/17.
 * Here all our api entry points are declared.
 * For more info see: http://square.github.io/retrofit/
 */

public interface Webservice {

    @GET("/api/get_all_beers")
    Call<List<Beer>> getTapList();

    @GET("/api/get_beer")
    Call<Beer> getBeer(@Query("beerId") int beerId);

    @GET("/api/get_all_orders")
    Call<List<HistoryOrder>> getAllOrders();

    @GET("/api/get_all_orders_by_user")
    Call<List<HistoryOrder>> getAllOrdersByUserId(@Query("userId") String userId);

    @GET("/api/get_all_orders_by_user")
    Call<List<HistoryOrder>> getAllOrdersByUserId(@Query("userId") String userId, @Query("limit") int limit);

    @POST("/api/add_order")
    Call<Order> addOrder(@Body Order order);

    @POST("/api/add_user")
    Call<HSUser> addUser(@Body HSUser user);

    @POST("/api/process_payment")
    Call<StripeInfo> sendStripe(@Body StripeInfo si);

    @POST("/api/update_user")
    Call<HSUser> updateUser(@Body HSUser user);

    @GET("/api/get_user")
    Call<HSUser> getUser(@Query("fbUserId") String fbUserId);

}
