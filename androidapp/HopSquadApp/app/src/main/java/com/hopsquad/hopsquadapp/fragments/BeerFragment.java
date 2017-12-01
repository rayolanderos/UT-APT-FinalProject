package com.hopsquad.hopsquadapp.fragments;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hopsquad.hopsquadapp.R;
import com.hopsquad.hopsquadapp.models.Beer;
import com.hopsquad.hopsquadapp.viewmodels.BeerViewModel;
import com.hopsquad.hopsquadapp.viewmodels.TapListViewModel;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BeerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BeerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeerFragment extends BaseFragment {

    private ScrollView mScrollView;
    private BeerViewModel viewModel;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_BEER_NAME = "beer_name";
    private static final String ARG_BEER_STYLE = "beer_style";
    private static final String ARG_BEER_DESCRIPTION = "beer_description";
    private static final String ARG_BEER_ABV = "beer_abv";
    private static final String ARG_BEER_IBUS = "beer_ibus";
    private static final String ARG_BEER_SRM = "beer_srm";
    private static final String ARG_BEER_PRICE = "beer_price";
    private static final String ARG_BEER_REVIEW = "beer_review";
    private static final String ARG_BEER_DESCRIPTION_IMAGE = "description_image";


    public String name;
    public String style;
    public String description;
    public String description_image;
    public double price;
    public double abv;
    public int ibus;
    public int srm;
    public double review;

    private OnFragmentInteractionListener mListener;

    public BeerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BeerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BeerFragment newInstance(String beer_name,
                                           String beer_style,
                                           String beer_description,
                                           String beer_description_image,
                                           double beer_price,
                                           double beer_abv,
                                           int beer_ibus,
                                           int beer_srm,
                                           double beer_review) {
        BeerFragment fragment = new BeerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BEER_NAME, beer_name);
        args.putString(ARG_BEER_STYLE, beer_style);
        args.putString(ARG_BEER_DESCRIPTION, beer_description);
        args.putString(ARG_BEER_DESCRIPTION_IMAGE, beer_description_image);
        args.putDouble(ARG_BEER_PRICE, beer_price);
        args.putDouble(ARG_BEER_ABV, beer_abv);
        args.putDouble(ARG_BEER_REVIEW, beer_review);
        args.putInt(ARG_BEER_IBUS, beer_ibus);
        args.putInt(ARG_BEER_SRM, beer_srm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_BEER_NAME);
            style = getArguments().getString(ARG_BEER_STYLE);
            description = getArguments().getString(ARG_BEER_DESCRIPTION);
            description_image = getArguments().getString(ARG_BEER_DESCRIPTION_IMAGE);
            price = getArguments().getDouble(ARG_BEER_PRICE);
            abv = getArguments().getDouble(ARG_BEER_ABV);
            review = getArguments().getDouble(ARG_BEER_REVIEW);
            ibus = getArguments().getInt(ARG_BEER_IBUS);
            srm = getArguments().getInt(ARG_BEER_SRM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View beerView = inflater.inflate(R.layout.fragment_beer, container, false);

        Picasso.with(this.getContext()).setIndicatorsEnabled(true);

        ImageView mDescriptionImageView = beerView.findViewById(R.id.beerDescriptionImageView);
        TextView mNameView = beerView.findViewById(R.id.beerNameTextView);
        TextView mStyleView = beerView.findViewById(R.id.beerStyleTextView);
        TextView mAbvView = beerView.findViewById(R.id.beerAbvTextView);
        TextView mIbusView = beerView.findViewById(R.id.beerIbusTextView);
        TextView mSrmView = beerView.findViewById(R.id.beerSrmTextView);
        TextView mPriceView = beerView.findViewById(R.id.beerPriceTextView);
        TextView mReviewView = beerView.findViewById(R.id.beerReviewTextView);
        TextView mDescriptionView = beerView.findViewById(R.id.beerDescriptionTextView);


        mStyleView.setText(style);
        mNameView.setText(name);
        mAbvView.setText(String.format("%2.1f%%", abv));
        mIbusView.setText(Double.toString(ibus));
        mSrmView.setText(Integer.toString(srm));
        mPriceView.setText(NumberFormat.getCurrencyInstance().format(price));
        mReviewView.setText(Double.toString(review));
        mDescriptionView.setText(description);

        String thumbnail_uri = description_image;
        final Context context = this.getContext();

        Picasso.with(context).load(thumbnail_uri).into(mDescriptionImageView);

        return beerView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
}
