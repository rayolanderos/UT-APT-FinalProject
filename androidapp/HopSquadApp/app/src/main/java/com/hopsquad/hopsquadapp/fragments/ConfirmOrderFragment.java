package com.hopsquad.hopsquadapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hopsquad.hopsquadapp.R;

import java.text.NumberFormat;

public class ConfirmOrderFragment extends BaseDialogFragment {

    private static final String TOTAL_PRICE_TO_PAY = "TOTAL_PRICE";
    private OnConfirmDialogOptionSelectedListener mListener;
    private float mTotal;

    public interface OnConfirmDialogOptionSelectedListener {

        void onConfirmDialogOptionSelected(boolean confirmed);
    }

    public static ConfirmOrderFragment newInstance(float total) {
        ConfirmOrderFragment fragment = new ConfirmOrderFragment();
        Bundle args = new Bundle();
        args.putFloat(TOTAL_PRICE_TO_PAY, total);
        fragment.setArguments(args);
        return fragment;
    }

    public ConfirmOrderFragment() {
        // Required empty public constructor
    }

    public void setOnConfirmDialogOptionSelectedListener(OnConfirmDialogOptionSelectedListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        mTotal = getArguments().getFloat(TOTAL_PRICE_TO_PAY);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_confirm_order, null);

        TextView totalTextView = dialogView.findViewById(R.id.totalConfirmationDialogText);
        totalTextView.setText(NumberFormat.getCurrencyInstance().format(mTotal));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView)
                .setTitle(R.string.confirm_order_dialog_title)
                .setPositiveButton(R.string.confirm_order_dialog_button, (dialogInterface, i) -> {
                    mListener.onConfirmDialogOptionSelected(true);
                })
                .setNegativeButton(R.string.cancel_order_button_dialog, (dialogInterface, i) -> {
                    mListener.onConfirmDialogOptionSelected(false);
                });
        return builder.create();
    }
}
