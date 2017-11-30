package com.hopsquad.hopsquadapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Date;

/**
 * Created by memoak on 11/26/2017.
 */

public class HistoryOrder {
    public String id;

    @SerializedName("order_total")
    public float order_total;

    @SerializedName("user_id")
    public String userId;

    public int status;

    @SerializedName("reward_id")
    public int rewardId;

    public float discount;

    @SerializedName("invoice_number")
    public String invoiceNumber;

    public Date timestamp;

    public Collection<BeerAndQuantity> details;
}
