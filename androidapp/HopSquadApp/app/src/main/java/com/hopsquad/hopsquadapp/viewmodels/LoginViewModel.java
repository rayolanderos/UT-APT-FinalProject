package com.hopsquad.hopsquadapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.HSUser;

import java.sql.Date;
import java.time.Instant;

/**
 * Created by memoak on 11/23/2017.
 */

public class LoginViewModel extends ViewModel {

    private LiveData<HSUser> mUser;
    private WebServiceRepository repository;

    public LiveData<HSUser> registerUser(String username, FirebaseUser user) {

        HSUser hsUser = new HSUser();
        hsUser.email = user.getEmail();
        hsUser.name = user.getDisplayName();
        hsUser.fireBaseUserId = user.getUid();
        hsUser.paymentKey = 0;

        mUser = repository.addUser(hsUser);
        return mUser;
    }

    public void setRepository(WebServiceRepository repository) {
        this.repository = repository;
    }
}
