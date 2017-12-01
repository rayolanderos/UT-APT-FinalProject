package com.hopsquad.hopsquadapp.viewmodels;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.BeerAndQuantity;
import com.hopsquad.hopsquadapp.models.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by rayolanderos on 23/11/17.
 */

public class BeerViewModel extends ViewModel {

    private LiveData<Beer> beer;
    private int beerId;
    private WebServiceRepository webRepo;

    public BeerViewModel(int beerId) {
        beerId = beerId;
    }

    public void init() {
        beer = webRepo.getSingleBeer(beerId);
    }

    public void setRepository(WebServiceRepository repo) {
        webRepo = repo;
    }

    public LiveData<Beer> getSingleBeer(int beerId) {
        return beer;
    }

    private String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }

}
