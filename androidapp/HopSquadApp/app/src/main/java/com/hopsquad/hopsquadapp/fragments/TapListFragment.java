package com.hopsquad.hopsquadapp.fragments;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.Order;
import com.hopsquad.hopsquadapp.viewmodels.TapListViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TapListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private BeerAdapter mAdapter;
    private FloatingActionButton placeOrderBtn;

    private TapListViewModel viewModel;

    public TapListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(TapListViewModel.class);
        viewModel.setRepository(new WebServiceRepository());
        viewModel.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tap_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.beerListRecyclerView);
        placeOrderBtn = (FloatingActionButton) v.findViewById(R.id.placeOrderBtn);

        initializeViews();
        return v;
    }

    private void initializeViews() {
        initializePlaceOrderButton();
        initializeRecyclerView();
    }

    private void initializePlaceOrderButton() {
        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }

    private void placeOrder() {

        // TODO Add confirmation DialogFragment

        // TODO Add loading spinner

        final LiveData<Order> orderLiveData = viewModel.placeOrder();
        final TapListFragment thisFragment = this;
        orderLiveData.observe(this, new Observer<Order>() {
            @Override
            public void onChanged(@Nullable Order order) {
                viewModel.clearOrder();
                mRecyclerView.setAdapter(mAdapter); // Setting the adapter again refreshes the list.
                orderLiveData.removeObservers(thisFragment);
            }
        });

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
