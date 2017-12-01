package com.hopsquad.hopsquadapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.models.BeerAndQuantity;
import com.hopsquad.hopsquadapp.models.HistoryOrder;
import com.hopsquad.hopsquadapp.models.Order;

import java.util.ArrayList;
import java.util.List;

import static com.hopsquad.hopsquadapp.viewmodels.TapListViewModel.STATE_TAX;

/**
 * Created by memoak on 11/26/2017.
 */

public class OrderHistoryViewModel extends ViewModel {

    private LiveData<List<HistoryOrder>> orderHistory;
    private MutableLiveData<HistoryOrder> historyOrderSelected;

    private WebServiceRepository webRepo;
    private LiveData<List<Beer>> tapList;

    public OrderHistoryViewModel() {
        historyOrderSelected = new MutableLiveData<>();
    }

    public void setRepository(WebServiceRepository repo) {
        webRepo = repo;
    }

    public void init() {
        tapList = webRepo.getAllBeers();
    }

    public LiveData<List<HistoryOrder>> getOrderHistory() {
        orderHistory = webRepo.getAllOrdersByUser(getUserId());
        return orderHistory;
    }

    public void setHistoryOrderSelected(HistoryOrder order) {
        historyOrderSelected.setValue(order);
    }

    public LiveData<HistoryOrder> getSelectedOrder() {
        return historyOrderSelected;
    }

    private String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }

    public LiveData<Order> placeOrder(String token) {
        HistoryOrder historyOrder = historyOrderSelected.getValue();

        Order order = convertHistoryOrderToOrder(historyOrder);
        order.invoice = token;

        Log.d("OrderHistoryViewModel", "token: "+ token.toString());

        LiveData<Order> newOrder = webRepo.placeOrder(order);
        return newOrder;
    }

    public Order convertHistoryOrderToOrder(HistoryOrder historyOrder) {
        Order order = new Order();
        float newTotal = 0.0f;
        ArrayList<BeerAndQuantity> beerList = new ArrayList<>();
        // No discounts re-applied :(
        for(BeerAndQuantity beerAndQuantity : historyOrder.details) {
            Beer beer = beerAndQuantity.id == null ? getBeerByName(beerAndQuantity.beerName) : getBeerById(beerAndQuantity.id);
            // Tap list might have changed.
            if (beer != null && beer.on_tap) {
                newTotal += (beer.price * (1 + STATE_TAX / 100)) * beerAndQuantity.quantity;
                // Fix null beerAndQuantity.id
                beerAndQuantity.id = beer.id;
                beerList.add(beerAndQuantity);
            }

        }
        order.total = newTotal;
        order.beers = beerList;
        order.userId = historyOrder.userId;
        return order;
    }

    private Beer getBeerById(String id) {
        List<Beer> currentTapList = tapList.getValue();
        for (Beer beer : currentTapList) {
            if (beer.id.equals(id)) {
                return beer;
            }
        }
        return null;
    }

    private Beer getBeerByName(String name) {
        List<Beer> currentTapList = tapList.getValue();
        for (Beer beer : currentTapList) {
            if (beer.name.equals(name)) {
                return beer;
            }
        }
        return null;
    }

}
