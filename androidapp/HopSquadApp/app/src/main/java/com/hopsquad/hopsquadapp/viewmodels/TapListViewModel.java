package com.hopsquad.hopsquadapp.viewmodels;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.BeerAndQuantity;
import com.hopsquad.hopsquadapp.models.Order;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by memo on 15/11/17.
 */

public class TapListViewModel extends ViewModel {

    private LiveData<List<Beer>> tapList;
    private WebServiceRepository webRepo;

    private HashMap<String, Integer> orderList;

    private ConcurrentHashMap<String, WeakReference<Bitmap>> beerImages;

    public TapListViewModel() {
        orderList = new HashMap<>();
        beerImages = new ConcurrentHashMap<>();
    }

    public void init() {
        tapList = webRepo.getAllBeers();
    }

    public void setRepository(WebServiceRepository repo) {
        webRepo = repo;
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

    public LiveData<Order> placeOrder() {
        Order order = buildOrder();
        return webRepo.placeOrder(order);
    }

    private Order buildOrder() {
        Order order = new Order();

        order.discount = 0;
        // TODO: Change this once Stripe is implemented.
        order.invoice = "12039489384";
        order.rewardId = 0;
        order.userId = getUserId();
        // TODO: Add the rest of properties

        computeTotal(order);
        return order;
    }

    private void computeTotal(Order order) {
        float total = 0.0f;
        List<Beer> beers = tapList.getValue();
        ArrayList<BeerAndQuantity> quantitites = new ArrayList<>();

        for (Map.Entry<String, Integer> beerAndQty : orderList.entrySet()) {
            Beer beer = null;
            String beerId = beerAndQty.getKey();
            int quantity = beerAndQty.getValue();

            if (quantity == 0) {
                continue;
            }

            for(Beer b : beers) {
                if (b.id.equals(beerId)) {
                    beer = b;
                    break;
                }
            }

            if (beer == null) {
                throw new RuntimeException(String.format("Beer with id %s not found.", beerId));
            }

            quantitites.add(new BeerAndQuantity(beerId, quantity));

            total += beer.price * quantity;
        }

        order.beers = quantitites;
        order.total = total;
    }

    private String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }

}
