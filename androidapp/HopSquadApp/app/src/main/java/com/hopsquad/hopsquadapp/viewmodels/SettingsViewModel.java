package com.hopsquad.hopsquadapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.models.HSUser;

/**
 * Created by rayolanderos on 11/29/17.
 */

public class SettingsViewModel extends ViewModel {

    private LiveData<HSUser> user;
    private FirebaseUser fbUser;

    private WebServiceRepository webRepo;

    public SettingsViewModel() {

    }

    public void init() {
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        user = webRepo.getUser(fbUser.getUid());
    }

    public void setRepository(WebServiceRepository repo) {
        webRepo = repo;
    }

    public LiveData<HSUser> getUser() {
        return user;
    }

    private String getUserId() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        return fbUser.getUid();
    }

}