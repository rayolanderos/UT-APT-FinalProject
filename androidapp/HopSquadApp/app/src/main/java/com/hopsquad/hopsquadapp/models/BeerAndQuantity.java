package com.hopsquad.hopsquadapp.models;

/**
 * Created by memo on 21/11/17.
 */

public class BeerAndQuantity {
    private String id;
    private int quantity;

    public BeerAndQuantity(String beerId, int quantity) {
        id = beerId;
        this.quantity = quantity;
    }
}
