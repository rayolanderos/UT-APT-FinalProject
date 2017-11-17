package com.hopsquad.hopsquadapp.viewmodels;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.hopsquad.hopsquadapp.api.Beer;
import com.hopsquad.hopsquadapp.api.BeerRepository;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by memo on 15/11/17.
 */

public class TapListViewModel extends ViewModel {

    private LiveData<List<Beer>> tapList;
    private BeerRepository beerRepo;

    private HashMap<String, Integer> orderList;

    public TapListViewModel() {
        orderList = new HashMap<>();
    }

    public void init() {
        tapList = beerRepo.getAllBeers();
    }

    public void setBeerRepository(BeerRepository repo) {
        beerRepo = repo;
    }

    public LiveData<List<Beer>> getTapList() {
        return tapList;
    }

    public int getQuantityOrdered(String beerId) {
        if (orderList.containsKey(beerId)) {
            return orderList.get(beerId);
        }

        return 0;
    }

    public void setQuantityOrdered(String beerId, int quantity) {
        orderList.put(beerId, quantity);
    }

    public void clearOrder() {
        orderList.clear();
    }

}
