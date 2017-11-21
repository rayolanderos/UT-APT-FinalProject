package com.hopsquad.hopsquadapp.api;

import android.arch.lifecycle.BuildConfig;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by memo on 15/11/17.
 */

public class BeerRepository {

    // This is for debugging purposes on the simulator
    // TODO: switch to actual project on Google Cloud
    public static final String SIMULATOR_BASE_URL = "http://10.0.2.2:8080/";
    public static final String APP_BASE_URL = "https://hopsquadatx.appspot.com";


    private static Gson gson = new GsonBuilder().setDateFormat("MM/dd/YYYY").create();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(APP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = retrofitBuilder.build();
    private static Webservice webservice = retrofit.create(Webservice.class);


    public LiveData<List<Beer>> getAllBeers() {
        final MutableLiveData<List<Beer>>  data = new MutableLiveData<>();

        webservice.tapList().enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {

            }
        });

        return data;
    }
}
