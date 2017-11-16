package com.hopsquad.hopsquadapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by memo on 15/11/17.
 */

public interface Webservice {

    @GET("/api/get_all_beers")
    Call<List<Beer>> tapList();
}
