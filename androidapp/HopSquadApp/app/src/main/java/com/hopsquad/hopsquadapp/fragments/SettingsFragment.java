package com.hopsquad.hopsquadapp.fragments;

import android.app.Fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hopsquad.hopsquadapp.R;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.HSUser;
import com.hopsquad.hopsquadapp.viewmodels.OrderHistoryViewModel;
import com.hopsquad.hopsquadapp.viewmodels.SettingsViewModel;

import java.text.SimpleDateFormat;


public class SettingsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match if needed
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SettingsViewModel viewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        viewModel.setRepository(new WebServiceRepository());
        viewModel.init();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        viewModel.getUser().observe(this, (userData)-> {
            refreshUserSettings(userData, settingsView);
        });

        // Inflate the layout for this fragment
        return settingsView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings, menu);
    }

    private void refreshUserSettings(HSUser user, View settingsView){
        TextView mNameView = settingsView.findViewById(R.id.nameField);
        TextView mEmailView = settingsView.findViewById(R.id.emailField);
        TextView mBirthDateView = settingsView.findViewById(R.id.birthDateField);

        if(user != null) {
            mNameView.setText(user.name);
            mEmailView.setText(user.email);
            mBirthDateView.setText(new SimpleDateFormat("MM-dd-yyyy").format(user.dateOfBirth));
        }
    }

}
