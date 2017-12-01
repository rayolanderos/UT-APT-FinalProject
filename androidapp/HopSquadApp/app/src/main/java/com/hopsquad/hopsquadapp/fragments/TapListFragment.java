package com.hopsquad.hopsquadapp.fragments;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.hopsquad.hopsquadapp.R;
import com.hopsquad.hopsquadapp.framework.PayWithGoogleService;
import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.Order;
import com.hopsquad.hopsquadapp.viewmodels.TapListViewModel;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

public class TapListFragment extends BaseFragment implements ConfirmOrderFragment.OnConfirmDialogOptionSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private BeerAdapter mAdapter;
    private FloatingActionButton placeOrderBtn;
    private TapListViewModel viewModel;
    private PayWithGoogleService payService;


    public TapListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(TapListViewModel.class);
        viewModel.setRepository(new WebServiceRepository());
        viewModel.init();

        payService = new PayWithGoogleService(this.getContext());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String token = null;

        if (payService.canHandleRequestCode(requestCode)) {
            payService.handleActivityResult(resultCode, data);
            token = payService.getResultToken();
        }

        if (token != null) {
            registerPlacedOrder(token);
        }
    }

    @Override
    public void onConfirmDialogOptionSelected(boolean confirmed) {
        if (confirmed) {
            payService.confirmOrder(this.getActivity(), viewModel.getOrderTotal());
        }
    }

    private void initializeViews() {
        initializePlaceOrderButton();
        initializeRecyclerView();
    }

    private void initializePlaceOrderButton() {
        viewModel.getLiveTotal().observe(this, (newTotal) -> {

            // TODO Give better feedback of the state of the button
            boolean enabled = newTotal != null && newTotal.floatValue() > 0;
            placeOrderBtn.setEnabled(enabled);
        });

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOrderConfirmation();
            }
        });
    }

    private void displayOrderConfirmation() {
        ConfirmOrderFragment confirmDialog = ConfirmOrderFragment.newInstance(viewModel.getOrderTotal());
        confirmDialog.setOnConfirmDialogOptionSelectedListener(this);
        confirmDialog.show(this.getFragmentManager(), "CONFIRM_ORDER");
    }

    private void initializeRecyclerView() {
        if (mRecyclerView == null) {
            return;
        }
        Picasso.with(this.getContext()).setIndicatorsEnabled(false);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        viewModel.getTapList().observe(this.getActivity(),(beers) -> {
                refreshBeerList();
            });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void registerPlacedOrder(String token) {
        final LiveData<Order> orderLiveData = viewModel.placeOrder(token);
        final TapListFragment tapListFragment = this;
        orderLiveData.observe(tapListFragment, order -> {
            if (order == null || order.invoice == null) {
                tapListFragment.showToast(R.string.order_confirmation_generic_error);
            } else {
                tapListFragment.showToast(R.string.order_succesfully_placed_msg);
                viewModel.clearOrder();
                refreshBeerList();
                orderLiveData.removeObservers(tapListFragment);
            }
        });
    }

    private void refreshBeerList() {
        mAdapter = new BeerAdapter(viewModel, this.getContext());
        mAdapter.isRefreshing = true;
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.isRefreshing = false;
    }

    private class BeerAdapter extends RecyclerView.Adapter<BeerHolder> {

        private TapListViewModel tapList;
        private Context context;
        private boolean isRefreshing;

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
            holder.mStyleView.setText(b.style);
            holder.mAlcoholByVolumeView.setText(String.format("%2.1f%%", b.abv));
            holder.mPriceView.setText(NumberFormat.getCurrencyInstance().format(b.price));

            final int[] check = { 0 };
            holder.mSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if ((++check[0]) > 1) {
                        tapList.setQuantityOrdered(b.id, position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            holder.mSpinnerView.setSelection(tapList.getQuantityOrdered(b.id), true);
            holder.mSpinnerView.setEnabled(b.on_tap);
            if (!b.on_tap) {
                holder.mPriceView.setText("$--.--");
            }

            String thumbnail_uri = b.tap_list_image;
            Picasso.with(context).load(thumbnail_uri).into(holder.mImageView);
            holder.mImageView.setClickable(true);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Launch beerFragment
                    BeerFragment beerFragment = BeerFragment.newInstance(b.id, b.name, b.style, b.description,
                            b.description_image, b.price, b.abv, b.ibus, b.srm, b.review );
                    launchBeerDescription(beerFragment, "BEER_FRAGMENT");

                }
            });
        }

        @Override
        public int getItemCount() {
            return tapList.getTapList().getValue().size();
        }
    }

    private void launchBeerDescription(BaseFragment fragment, String tag){
        this.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    private static class BeerHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTitleView;
        public TextView mStyleView;
        public Spinner mSpinnerView;
        public TextView mAlcoholByVolumeView;
        public TextView mPriceView;

        public BeerHolder (View view) {
            super(view);
            mImageView = view.findViewById(R.id.beerImage);
            mTitleView = view.findViewById(R.id.beerNameView);
            mStyleView = view.findViewById(R.id.beerStyleText);
            mSpinnerView = view.findViewById(R.id.beerSpinner);
            mAlcoholByVolumeView = view.findViewById(R.id.alcoholByVolumeText);
            mPriceView = view.findViewById(R.id.beerPriceText);
        }
    }

}
