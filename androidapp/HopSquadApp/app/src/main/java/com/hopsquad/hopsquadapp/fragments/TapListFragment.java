package com.hopsquad.hopsquadapp.fragments;


import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardInfo;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.hopsquad.hopsquadapp.R;
import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.Order;
import com.hopsquad.hopsquadapp.viewmodels.TapListViewModel;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Arrays;

public class TapListFragment extends BaseFragment {

    public static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 38192;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private BeerAdapter mAdapter;
    private FloatingActionButton placeOrderBtn;
    private PaymentsClient mPaymentsClient;

    private TapListViewModel viewModel;

    public TapListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        // You can get some data on the user's card, such as the brand and last 4 digits
                        CardInfo info = paymentData.getCardInfo();
                        // You can also pull the user address from the PaymentData object.
                        UserAddress address = paymentData.getShippingAddress();
                        // This is the raw JSON string version of your Stripe token.
                        String rawToken = paymentData.getPaymentMethodToken().getToken();

                        registerPlacedOrder(rawToken);

                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        // Log the status for debugging
                        // Generally there is no need to show an error to
                        // the user as the Google Payment API will do that
                        break;
                    default:
                        // Do nothing.
                }
                break; // Breaks the case LOAD_PAYMENT_DATA_REQUEST_CODE
            // Handle any other startActivityForResult calls you may have made.
            default:
                // Do nothing.
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(TapListViewModel.class);
        viewModel.setRepository(new WebServiceRepository());
        viewModel.init();

        initializePaymentsClient();
    }

    private void initializePaymentsClient() {
        mPaymentsClient = Wallet.getPaymentsClient(this.getContext(), new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST).build());

        IsReadyToPayRequest request = IsReadyToPayRequest.newBuilder()
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                .build();

        Task<Boolean> task = mPaymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                try {
                    boolean result = task.getResult(ApiException.class);
                    viewModel.setIsReadyToPay(result);

                } catch (ApiException apie) {
                    apie.printStackTrace();
                }
            }
        });
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
        ConfirmOrderFragment confirmDialog = new ConfirmOrderFragment();
        confirmDialog.show(this.getFragmentManager(), "CONFIRM_ORDER");
    }

    private void initializeRecyclerView() {
        if (mRecyclerView == null) {
            return;
        }
        Picasso.with(this.getContext()).setIndicatorsEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        viewModel.getTapList().observe(this.getActivity(),(beers) -> {
                refreshBeerList();
            });
    }

    public void confirmOrder() {
        PaymentDataRequest request = createPaymentDataRequest();
        if (request != null) {
            AutoResolveHelper.resolveTask(mPaymentsClient.loadPaymentData(request),
                    this.getActivity(),
                    LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }

    private void registerPlacedOrder(String token) {
        final LiveData<Order> orderLiveData = viewModel.placeOrder(token);
        final TapListFragment tapListFragment = this;
        orderLiveData.observe(tapListFragment, new Observer<Order>() {
            @Override
            public void onChanged(@Nullable Order order) {

                if (order == null || order.invoice == null) {
                    tapListFragment.showToast(R.string.order_confirmation_generic_error);
                } else {
                    tapListFragment.showToast(R.string.order_succesfully_placed_msg);
                    viewModel.clearOrder();
                    refreshBeerList();
                    orderLiveData.removeObservers(tapListFragment);
                }
            }
        });
    }

    private void refreshBeerList() {
        mAdapter = new BeerAdapter(viewModel, this.getContext());
        mAdapter.isRefreshing = true;
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.isRefreshing = false;
    }

    private PaymentDataRequest createPaymentDataRequest() {
        String totalPrice = String.format("%.2f", viewModel.getOrderTotal());
        PaymentDataRequest.Builder request =
                PaymentDataRequest.newBuilder()
                        .setTransactionInfo(
                                TransactionInfo.newBuilder()

                                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                                        .setTotalPrice(totalPrice)
                                        .setCurrencyCode("USD")
                                        .build())
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                        .setCardRequirements(
                                CardRequirements.newBuilder()
                                        .addAllowedCardNetworks(Arrays.asList(
                                                WalletConstants.CARD_NETWORK_AMEX,
                                                WalletConstants.CARD_NETWORK_DISCOVER,
                                                WalletConstants.CARD_NETWORK_VISA,
                                                WalletConstants.CARD_NETWORK_MASTERCARD))
                                        .build());

        request.setPaymentMethodTokenizationParameters(createTokenizationParameters());
        return request.build();
    }

    private PaymentMethodTokenizationParameters createTokenizationParameters() {
        return PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .addParameter("gateway", "stripe")
                .addParameter("stripe:publishableKey", WebServiceRepository.STRIPE_PUBLISHABLE_KEY)
                .addParameter("stripe:version", "5.1.0")
                .build();
    }

    private static class BeerAdapter extends RecyclerView.Adapter<BeerHolder> {

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
