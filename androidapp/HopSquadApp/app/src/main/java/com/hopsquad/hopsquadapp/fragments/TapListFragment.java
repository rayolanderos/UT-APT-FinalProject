package com.hopsquad.hopsquadapp.fragments;


import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hopsquad.hopsquadapp.R;
import com.hopsquad.hopsquadapp.api.Beer;
import com.hopsquad.hopsquadapp.api.BeerRepository;
import com.hopsquad.hopsquadapp.viewmodels.TapListViewModel;

import java.util.List;

public class TapListFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private BeerAdapter mAdapter;

    private TapListViewModel viewModel;


    public TapListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TapListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TapListFragment newInstance(String param1, String param2) {
        TapListFragment fragment = new TapListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        viewModel.getTapList().observe(this.getActivity(), new Observer<List<Beer>>() {

            @Override
            public void onChanged(@Nullable List<Beer> beers) {
                mAdapter = new BeerAdapter(viewModel);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    private static class BeerAdapter extends RecyclerView.Adapter<BeerHolder> {

        private TapListViewModel tapList;

        public BeerAdapter(TapListViewModel tapList) {
            this.tapList = tapList;
        }

        @Override
        public BeerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_list_item, parent, false);

            BeerHolder holder = new BeerHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(BeerHolder holder, int position) {
            Beer b = tapList.getTapList().getValue().get(position);
            holder.mTitleView.setText(b.name);
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
