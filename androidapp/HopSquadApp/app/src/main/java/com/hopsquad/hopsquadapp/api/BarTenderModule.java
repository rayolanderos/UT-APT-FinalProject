package com.hopsquad.hopsquadapp.api;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by memo on 15/11/17.
 */
@Module
public class BarTenderModule {

    public static final String BASE_URL = "http://localhost:8080/";

    @Provides
    static BeerRepository provideBeerRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(BeerRepository.class);
    }

}
