package com.hopsquad.hopsquadapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.HistoryOrder;
import com.hopsquad.hopsquadapp.models.Order;

import java.util.List;

/**
 * Created by memoak on 11/26/2017.
 */

public class OrderHistoryViewModel extends ViewModel {

    private LiveData<List<HistoryOrder>> orderHistory;

    private WebServiceRepository webRepo;

    public OrderHistoryViewModel() {

    }

    public void setRepository(WebServiceRepository repo) {
        webRepo = repo;
    }

    public void init() {
        orderHistory = webRepo.getAllOrders(getUserId());
    }

    public LiveData<List<HistoryOrder>> getOrderHistory() {
        return orderHistory;
    }

    private String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }

}
