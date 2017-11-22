package com.hopsquad.hopsquadapp.fragments;


import android.app.Activity;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hopsquad.hopsquadapp.BuildConfig;
import com.hopsquad.hopsquadapp.R;
import com.hopsquad.hopsquadapp.api.Beer;
import com.hopsquad.hopsquadapp.api.BeerRepository;
import com.hopsquad.hopsquadapp.viewmodels.TapListViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TapListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private BeerAdapter mAdapter;

    private TapListViewModel viewModel;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(4);

    public TapListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        viewModel = ViewModelProviders.of(this).get(TapListViewModel.class);
        viewModel.setBeerRepository(new BeerRepository());
        viewModel.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tap_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.beerListRecyclerView);
        initializeRecyclerView();
        return v;
    }

    private void initializeRecyclerView() {
        if (mRecyclerView == null) {
            return;
        }
        Picasso.with(this.getContext()).setIndicatorsEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        final Context context = this.getContext();

        viewModel.getTapList().observe(this.getActivity(), new Observer<List<Beer>>() {

            @Override
            public void onChanged(@Nullable List<Beer> beers) {
                mAdapter = new BeerAdapter(viewModel, context);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    private static class BeerAdapter extends RecyclerView.Adapter<BeerHolder> {

        private TapListViewModel tapList;
        private Context context;

        public BeerAdapter(TapListViewModel tapList, Context context) {

            this.tapList = tapList;
            this.context = context;
        }

        @Override
        public BeerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_list_item, parent, false);

            BeerHolder holder = new BeerHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(BeerHolder holder, int position) {
            final Beer b = tapList.getTapList().getValue().get(position);
            holder.mTitleView.setText(b.name);
            holder.mSpinnerView.setSelection(tapList.getQuantityOrdered(b.id));
            holder.mSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tapList.setQuantityOrdered(b.id, position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            String thumbnail_uri = b.tap_list_image;
            if (BuildConfig.DEBUG) {
                thumbnail_uri = thumbnail_uri.replace("localhost:8080", "10.2.2.2:8080");
            }

            Picasso.with(context).load(thumbnail_uri).into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return tapList.getTapList().getValue().size();
        }
    }

    private static class BeerHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTitleView;
        public Spinner mSpinnerView;

        public BeerHolder (View view) {
            super(view);
            mImageView = view.findViewById(R.id.beerImage);
            mTitleView = view.findViewById(R.id.beerNameView);
            mSpinnerView = view.findViewById(R.id.beerSpinner);
        }
    }

}
