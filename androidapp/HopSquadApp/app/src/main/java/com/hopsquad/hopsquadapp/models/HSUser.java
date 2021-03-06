package com.hopsquad.hopsquadapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by memoak on 11/23/2017.
 */

public class HSUser {

    @SerializedName("userName")
    public String name;

    @SerializedName("userEmail")
    public String email;

    @SerializedName("userFbUserId")
    public String fireBaseUserId;

    @SerializedName("userPaymentKey")
    public String paymentKey;

    @SerializedName("userDateOfBirth")
    public Date dateOfBirth;
}
