package com.hopsquad.hopsquadapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memo on 21/11/17.
 * Used to serialize an order description into JSON.
 */

public class BeerAndQuantity {
    private String id;
    public int quantity;

    @SerializedName("beer_name")
    public String beerName;

    public BeerAndQuantity(String beerId, int quantity) {
        id = beerId;
        this.quantity = quantity;
    }
}
