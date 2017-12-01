package com.hopsquad.hopsquadapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hopsquad.hopsquadapp.R;

import java.text.NumberFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddReviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReviewFragment extends BaseDialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String BEER_ID = "beer_id";
    private AddReviewFragment.OnConfirmDialogOptionSelectedListener mListener;
    private String beer_id;
    private int review = 1;
    ImageButton mRateBtn1, mRateBtn2, mRateBtn3, mRateBtn4, mRateBtn5;

    public AddReviewFragment() {
        // Required empty public constructor
    }

    public interface OnConfirmDialogOptionSelectedListener {

        void onConfirmDialogOptionSelected(boolean confirmed, String beer_id, int review);
    }

    public void setOnConfirmDialogOptionSelectedListener(AddReviewFragment.OnConfirmDialogOptionSelectedListener listener) {
        this.mListener = listener;
    }

    public static AddReviewFragment newInstance(String beer_id) {
        AddReviewFragment fragment = new AddReviewFragment();
        Bundle args = new Bundle();
        args.putString(BEER_ID, beer_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            beer_id = getArguments().getString(BEER_ID);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_add_review, null);

        mRateBtn1 = (ImageButton) dialogView.findViewById(R.id.rateBtn1);
        mRateBtn1.setOnClickListener(this);
        mRateBtn2 = (ImageButton) dialogView.findViewById(R.id.rateBtn2);
        mRateBtn2.setOnClickListener(this);
        mRateBtn3 = (ImageButton) dialogView.findViewById(R.id.rateBtn3);
        mRateBtn3.setOnClickListener(this);
        mRateBtn4 = (ImageButton) dialogView.findViewById(R.id.rateBtn4);
        mRateBtn4.setOnClickListener(this);
        mRateBtn5 = (ImageButton) dialogView.findViewById(R.id.rateBtn5);
        mRateBtn5.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView)
                .setTitle(R.string.add_review_dialog_title)
                .setPositiveButton(R.string.confirm_add_review_dialog_button, (dialogInterface, i) -> {
                    mListener.onConfirmDialogOptionSelected(true, beer_id, review);
                })
                .setNegativeButton(R.string.cancel_add_review_dialog_button, (dialogInterface, i) -> {
                    mListener.onConfirmDialogOptionSelected(false, beer_id, review);
                });
        return builder.create();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public void onClick(View view) {

        mRateBtn1.setImageResource(android.R.drawable.btn_star_big_on);

        switch (view.getId()){
            case (R.id.rateBtn1):
                mRateBtn2.setImageResource(android.R.drawable.btn_star_big_off);
                mRateBtn3.setImageResource(android.R.drawable.btn_star_big_off);
                mRateBtn4.setImageResource(android.R.drawable.btn_star_big_off);
                mRateBtn5.setImageResource(android.R.drawable.btn_star_big_off);
                break;
            case (R.id.rateBtn2):
                mRateBtn2.setImageResource(android.R.drawable.btn_star_big_on);
                mRateBtn3.setImageResource(android.R.drawable.btn_star_big_off);
                mRateBtn4.setImageResource(android.R.drawable.btn_star_big_off);
                mRateBtn5.setImageResource(android.R.drawable.btn_star_big_off);
                review = 2;
                break;
            case (R.id.rateBtn3):
                mRateBtn2.setImageResource(android.R.drawable.btn_star_big_on);
                mRateBtn3.setImageResource(android.R.drawable.btn_star_big_on);
                mRateBtn4.setImageResource(android.R.drawable.btn_star_big_off);
                mRateBtn5.setImageResource(android.R.drawable.btn_star_big_off);
                review = 3;
                break;
            case (R.id.rateBtn4):
                mRateBtn2.setImageResource(android.R.drawable.btn_star_big_on);
                mRateBtn3.setImageResource(android.R.drawable.btn_star_big_on);
                mRateBtn4.setImageResource(android.R.drawable.btn_star_big_on);
                mRateBtn5.setImageResource(android.R.drawable.btn_star_big_off);
                review = 4;
                break;
            default:
                mRateBtn2.setImageResource(android.R.drawable.btn_star_big_on);
                mRateBtn3.setImageResource(android.R.drawable.btn_star_big_on);
                mRateBtn4.setImageResource(android.R.drawable.btn_star_big_on);
                mRateBtn5.setImageResource(android.R.drawable.btn_star_big_on);
                review = 5;

        }

    }
}
