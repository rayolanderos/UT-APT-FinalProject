package com.hopsquad.hopsquadapp.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.models.HSUser;
import com.hopsquad.hopsquadapp.models.Order;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by memo on 15/11/17.
 */

public class WebServiceRepository {

    // This is for debugging purposes on the simulator
    // TODO: switch to actual project on Google Cloud
    public static final String SIMULATOR_BASE_URL = "http://10.0.2.2:8080/";
    public static final String APP_BASE_URL = "https://hopsquad-app.appspot.com/";

    public static final String STRIPE_PUBLISHABLE_KEY = "pk_test_8J6KmmzwK32kRCoYqGPgBTiZ";

    // '%Y-%m-%d %H:%M:%S'
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(APP_BASE_URL)
            .addConverterFactory(new NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = retrofitBuilder.build();
    private static Webservice webservice = retrofit.create(Webservice.class);

    // We have 2 different types of date formats, so we create 2 services.
    private static Gson gson2 = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();

    private static Retrofit.Builder retrofitBuilder2 = new Retrofit.Builder()
            .baseUrl(APP_BASE_URL)
            .addConverterFactory(new NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson2));

    private static Retrofit retrofit2 = retrofitBuilder2.build();
    private static Webservice webservice2 = retrofit2.create(Webservice.class);

    // Added for entrypoints that return empty content to convert it to null
    static class NullOnEmptyConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            super.responseBodyConverter(type, annotations, retrofit);
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);

            return (Converter<ResponseBody, Object>) value -> {
                if (value.contentLength() == 0) {
                    return null;
                }
                return delegate.convert(value);
            };
        }
    }



    public LiveData<List<Beer>> getAllBeers() {
        final MutableLiveData<List<Beer>>  data = new MutableLiveData<>();

        webservice2.getTapList().enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return data;
    }

    public LiveData<Order> placeOrder(Order order) {

        final MutableLiveData<Order> data = new MutableLiveData<>();

        webservice.addOrder(order).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Order value = response.isSuccessful() ? order : new Order();
                data.setValue(value);
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return data;
    }

    public LiveData<HSUser> addUser(HSUser user) {

        final MutableLiveData<HSUser> data = new MutableLiveData<>();

        webservice.addUser(user).enqueue(new Callback<HSUser>() {
            @Override
            public void onResponse(Call<HSUser> call, Response<HSUser> response) {
                HSUser value = response.isSuccessful() ? response.body() : new HSUser();
                data.setValue(value);
            }

            @Override
            public void onFailure(Call<HSUser> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return data;
    }
}
