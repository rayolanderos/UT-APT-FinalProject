package com.hopsquad.hopsquadapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hopsquad.hopsquadapp.R;
import com.hopsquad.hopsquadapp.activities.MainActivity;
import com.hopsquad.hopsquadapp.models.Order;
import com.hopsquad.hopsquadapp.viewmodels.TapListViewModel;

import java.text.NumberFormat;

public class ConfirmOrderFragment extends DialogFragment {

    public ConfirmOrderFragment() {
        // Required empty public constructor
    }

    TapListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);

        viewModel = ViewModelProviders.of(getTapListFragment()).get(TapListViewModel.class);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_confirm_order, null);

        TextView totalTextView = dialogView.findViewById(R.id.totalConfirmationDialogText);
        totalTextView.setText(NumberFormat.getCurrencyInstance().format(viewModel.getOrderTotal()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView)
                .setTitle(R.string.confirm_order_dialog_title)
                .setPositiveButton(R.string.confirm_order_dialog_button, new DialogClickHandler(this, UserAction.ACCEPTED))
                .setNegativeButton(R.string.cancel_order_button_dialog, new DialogClickHandler(this, UserAction.CANCELED));
        return builder.create();
    }

    private TapListFragment getTapListFragment() {
        return (TapListFragment) getFragmentManager().findFragmentByTag(MainActivity.TAP_LIST_FRAGMENT_TAG);
    }

    public void confirmOrder() {

        // TODO Add loading spinner

        final LiveData<Order> orderLiveData = viewModel.placeOrder();
        final TapListFragment tapListFragment = getTapListFragment();
        orderLiveData.observe(tapListFragment, new Observer<Order>() {
            @Override
            public void onChanged(@Nullable Order order) {

                if (order == null || order.invoice == null) {
                    tapListFragment.showToast(R.string.order_confirmation_generic_error);
                } else {
                    tapListFragment.showToast(R.string.order_succesfully_placed_msg);
                    viewModel.clearOrder();
                    orderLiveData.removeObservers(tapListFragment);
                }
            }
        });
    }

    private enum UserAction { CANCELED, ACCEPTED };

    private static class DialogClickHandler implements DialogInterface.OnClickListener {
        private UserAction action;
        private ConfirmOrderFragment fragment;

        public DialogClickHandler(ConfirmOrderFragment fragment, UserAction action) {
            this.fragment = fragment;
            this.action = action;
        }

        private boolean wasCanceled() {
            return action == UserAction.CANCELED;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (wasCanceled()) {
                dialogInterface.cancel();
            } else {
                fragment.confirmOrder();
            }
        }
    }
}
