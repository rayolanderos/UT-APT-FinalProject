package com.hopsquad.hopsquadapp.viewmodels;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.BeerAndQuantity;
import com.hopsquad.hopsquadapp.models.Order;
import com.hopsquad.hopsquadapp.models.Review;

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

    public BeerViewModel() {

    }

    public void init() {

    }

    public void setRepository(WebServiceRepository repo) {
        webRepo = repo;
    }

    public LiveData<Beer> getSingleBeer(int beerId) {
        return beer;
    }

    public LiveData<Review> addReview(String beer_id, int reviewNum){

        Review review = convertDatatoReview(beer_id, reviewNum);

        LiveData<Review> newReview = webRepo.addReview(review);
        return newReview;
    }

    private String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }

    private Review convertDatatoReview(String beer_id, int reviewNum){
        Review review = new Review();
        review.beer_id = beer_id;
        review.review = reviewNum;

        return review;
    }

}
