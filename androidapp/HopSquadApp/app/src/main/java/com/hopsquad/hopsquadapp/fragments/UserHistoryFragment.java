package com.hopsquad.hopsquadapp.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hopsquad.hopsquadapp.R;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.framework.PayWithGoogleService;
import com.hopsquad.hopsquadapp.models.BeerAndQuantity;
import com.hopsquad.hopsquadapp.models.HistoryOrder;
import com.hopsquad.hopsquadapp.models.Order;
import com.hopsquad.hopsquadapp.viewmodels.OrderHistoryViewModel;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

public class UserHistoryFragment extends BaseFragment implements ConfirmOrderFragment.OnConfirmDialogOptionSelectedListener {

    private RecyclerView mOrderHistoryView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OrderHistoryViewModel viewModel;
    private OrderHistoryAdapter mOrderAdapter;
    private LinearLayoutManager mLayoutManager;
    private PayWithGoogleService payService;

    public UserHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHistoryFragment newInstance(String param1, String param2) {
        UserHistoryFragment fragment = new UserHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel.class);
        viewModel.setRepository(new WebServiceRepository());
        viewModel.init();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        payService = new PayWithGoogleService(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_user_history, container, false);
        mOrderHistoryView = inflatedView.findViewById(R.id.userOrderHistoryList);

        mOrderHistoryView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mOrderHistoryView.setLayoutManager(mLayoutManager);

        viewModel.getOrderHistory().observe(this, (orders) -> {
            mOrderAdapter = new OrderHistoryAdapter(this, orders);
            mOrderHistoryView.setAdapter(mOrderAdapter);
            mOrderAdapter.notifyDataSetChanged();
        });

        // Inflate the layout for this fragment
        return inflatedView;
    }

    @Override
    public void onConfirmDialogOptionSelected(boolean confirmed) {
        if (confirmed) {
            payService.confirmOrder(getActivity(), viewModel.getSelectedOrder().getValue().order_total);
        }
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

    private void registerPlacedOrder(String token) {
        final LiveData<Order> orderLiveData = viewModel.placeOrder(token);
        orderLiveData.observe(this, order -> {
            if (order == null || order.invoice == null) {
                this.showToast(R.string.order_confirmation_generic_error);
            } else {
                this.showToast(R.string.order_succesfully_placed_msg);
                viewModel.setHistoryOrderSelected(null);
                // Todo Refresh orders list
                orderLiveData.removeObservers(this);
            }
        });
    }

    private static class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryHolder> {

        private List<HistoryOrder> orderHistory;
        private UserHistoryFragment fragment;

        public OrderHistoryAdapter(UserHistoryFragment fragment, List<HistoryOrder> orderHistory) {
            this.orderHistory = orderHistory;
            this.fragment = fragment;
        }

        @Override
        public OrderHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);

            OrderHistoryHolder holder = new OrderHistoryHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(OrderHistoryHolder holder, int position) {
            HistoryOrder order = orderHistory.get(position);

            holder.mOrderAgainButton.setOnClickListener(view -> fragment.orderAgain(order));

            StringBuilder orderDetails = new StringBuilder();
            for (BeerAndQuantity beerAndQuantity : order.details) {
                orderDetails.append(String.format("%s x %d\n", beerAndQuantity.beerName, beerAndQuantity.quantity));
            }

            holder.mMainBeerNameView.setText(orderDetails.toString());
            holder.mTimeAgoView.setText(timeAgo(order.timestamp.getTime() / 1000));
        }

        @Override
        public int getItemCount() {
            return orderHistory.size();
        }
    }

    private void orderAgain(HistoryOrder order) {
        viewModel.setHistoryOrderSelected(order);
        Order newOrder = viewModel.convertHistoryOrderToOrder(order);
        ConfirmOrderFragment confirmDialog = ConfirmOrderFragment.newInstance(newOrder.total);
        confirmDialog.setOnConfirmDialogOptionSelectedListener(this);
        confirmDialog.show(this.getFragmentManager(), "CONFIRM_ORDER");
    }

    // Totally inspired from: https://stackoverflow.com/questions/42508736/how-to-change-string-time-stamp-into-human-readable-date-format#42509214
    private static String timeAgo(long time_ago) {
        long cur_time = (Calendar.getInstance().getTimeInMillis()) / 1000;
        long time_elapsed = cur_time - time_ago;
        long seconds = time_elapsed;
        int minutes = Math.round(time_elapsed / 60);
        int hours = Math.round(time_elapsed / 3600);
        int days = Math.round(time_elapsed / 86400);
        int weeks = Math.round(time_elapsed / 604800);
        int months = Math.round(time_elapsed / 2600640);
        int years = Math.round(time_elapsed / 31207680);

        // Seconds
        if (seconds <= 60) {
            return "just now";
        }
        //Minutes
        else if (minutes <= 60) {
            if (minutes == 1) {
                return "one minute ago";
            } else {
                return minutes + " minutes ago";
            }
        }
        //Hours
        else if (hours <= 24) {
            if (hours == 1) {
                return "an hour ago";
            } else {
                return hours + " hrs ago";
            }
        }
        //Days
        else if (days <= 7) {
            if (days == 1) {
                return "yesterday";
            } else {
                return days + " days ago";
            }
        }
        //Weeks
        else if (weeks <= 4.3) {
            if (weeks == 1) {
                return "a week ago";
            } else {
                return weeks + " weeks ago";
            }
        }
        //Months
        else if (months <= 12) {
            if (months == 1) {
                return "a month ago";
            } else {
                return months + " months ago";
            }
        }
        //Years
        else {
            if (years == 1) {
                return "one year ago";
            } else {
                return years + " years ago";
            }
        }
    }

    private static class OrderHistoryHolder extends RecyclerView.ViewHolder {

        private TextView mMainBeerNameView;
        private TextView mTimeAgoView;
        private Button mOrderAgainButton;

        public OrderHistoryHolder(View itemView) {
            super(itemView);
            mMainBeerNameView = itemView.findViewById(R.id.mainBeerNameView);
            mTimeAgoView = itemView.findViewById(R.id.timeAgoView);
            mOrderAgainButton = itemView.findViewById(R.id.orderAgainBtn);
        }

    }
}
