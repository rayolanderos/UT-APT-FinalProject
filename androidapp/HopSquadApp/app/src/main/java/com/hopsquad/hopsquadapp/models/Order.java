package com.hopsquad.hopsquadapp.models;

import java.util.Collection;
import java.util.Date;

/**
 * Created by memo on 20/11/17.
 */

public class Order {

    public String userId;
    public float total;
    public String invoice;
    public int status;
    public float discount;
    public int rewardId;
    public Date timestamp;
    public Collection<BeerAndQuantity> beers;
}
