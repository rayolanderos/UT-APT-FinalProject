package com.hopsquad.hopsquadapp.viewmodels;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.hopsquad.hopsquadapp.api.Beer;
import com.hopsquad.hopsquadapp.api.BeerRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by memo on 15/11/17.
 */

public class TapListViewModel extends ViewModel {

    private LiveData<List<Beer>> tapList;
    private BeerRepository beerRepo;

    public TapListViewModel() {
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

}
