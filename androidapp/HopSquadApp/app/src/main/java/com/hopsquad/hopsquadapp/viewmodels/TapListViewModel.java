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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by memo on 15/11/17.
 */

public class TapListViewModel extends ViewModel {

    private LiveData<List<Beer>> tapList;
    private WebServiceRepository webRepo;
    private HashMap<Beer, Integer> orderInfo;
    private MutableLiveData<Float> liveTotal;
    private boolean isReadyToPay;

    public static final float STATE_TAX = 8.25f;

    public TapListViewModel() {
        isReadyToPay = false;
        orderInfo = new HashMap<>();
        liveTotal = new MutableLiveData<>();
        liveTotal.setValue(0.0f);
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
        if (orderInfo.containsKey(beerId)) {
            return orderInfo.get(beerId);
        }

        return 0; // Maybe there's a better value?
    }

    public void setQuantityOrdered(String beerId, int quantity) {
        float currentTotal = liveTotal.getValue();

        Beer beer = getBeerById(beerId);
        Integer currentQty = orderInfo.get(beer);
        currentQty = currentQty == null ? new Integer(0) : currentQty;
        currentTotal += (quantity - currentQty.intValue()) * (beer.price * (1 + STATE_TAX / 100)) ;

        liveTotal.setValue(currentTotal);
        orderInfo.put(beer, quantity);
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

    public void clearOrder() {
        orderInfo.clear();
        liveTotal.setValue(0.0f);
    }

    public LiveData<Order> placeOrder(String token) {
        Order order = buildOrder(token);
        return webRepo.placeOrder(order);
    }

    public LiveData<Float> getLiveTotal() {
        return liveTotal;
    }

    public float getOrderTotal() {
        return liveTotal.getValue();
    }

    private Order buildOrder(String token) {
        Order order = new Order();

        order.discount = 0;
        order.invoice = token;
        order.rewardId = 0;
        order.userId = getUserId();
        order.beers = getBeersByQuantity();
        order.total = getOrderTotal();

        return order;
    }



    private String generatePseudoInvoice() {
        // TODO: Change this once Stripe is implemented.
        int length = 10;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (length-- > 0) {
            sb.append(r.nextInt(10));
        }

        return sb.toString();
    }

    private List<BeerAndQuantity> getBeersByQuantity() {
        ArrayList<BeerAndQuantity> quantities = new ArrayList<>();

        for (Map.Entry<Beer, Integer> beerAndQty : orderInfo.entrySet()) {
            Beer beer = beerAndQty.getKey();
            int quantity = beerAndQty.getValue();

            if (quantity == 0) {
                continue;
            }

            quantities.add(new BeerAndQuantity(beer.id, quantity));
        }

        return quantities;
    }

    private String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }

    public void setIsReadyToPay(boolean result) {
        this.isReadyToPay = result;
    }

    public boolean isReadyToPay() {
        return isReadyToPay;
    }
}
