package com.hopsquad.hopsquadapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.widget.DatePicker;

import com.hopsquad.hopsquadapp.activities.LoginActivity;
import com.hopsquad.hopsquadapp.activities.MainActivity;
import com.hopsquad.hopsquadapp.viewmodels.LoginViewModel;
import com.hopsquad.hopsquadapp.viewmodels.TapListViewModel;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by memoak on 11/25/2017.
 */

public class DatePickerFragment extends BaseDialogFragment{

    public final int LEGAL_DRINKING_AGE = 21;

    private LoginViewModel viewModel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        LoginActivity activity = (LoginActivity) getActivity();
        viewModel = ViewModelProviders.of(activity).get(LoginViewModel.class);
        int year = c.get(Calendar.YEAR) - LEGAL_DRINKING_AGE;
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), viewModel, year, month, day);
    }
}