package com.hopsquad.hopsquadapp.viewmodels;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.hopsquad.hopsquadapp.api.Beer;
import com.hopsquad.hopsquadapp.api.BeerRepository;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

/**
 * Created by memo on 15/11/17.
 */

public class TapListViewModel extends ViewModel {

    private LiveData<List<Beer>> tapList;
    private BeerRepository beerRepo;

    private HashMap<String, Integer> orderList;

    private ConcurrentHashMap<String, WeakReference<Bitmap>> beerImages;

    public TapListViewModel() {
        orderList = new HashMap<>();
        beerImages = new ConcurrentHashMap<>();
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

    public void getAndImageFromUrl(String url, ImageView view) {



    }

//    // based on: https://android-developers.googleblog.com/2010/07/multithreading-for-performance.html
//    static class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
//        private final WeakReference<ImageView> imageViewRef;
//
//        public BitmapDownloaderTask(ImageView imageView) {
//            imageViewRef = new WeakReference<ImageView>(imageView);
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            URL url = new URL(params[0]);
//
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//
//            if (imageViewRef.get() != null) {
//                imageViewRef.get().setImageBitmap(result);
//            }
//        }
//    }

}
