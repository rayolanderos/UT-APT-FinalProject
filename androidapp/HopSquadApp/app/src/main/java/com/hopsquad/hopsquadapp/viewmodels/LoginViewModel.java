package com.hopsquad.hopsquadapp.viewmodels;

import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.widget.DatePicker;

import com.google.firebase.auth.FirebaseUser;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.HSUser;

import java.util.Calendar;
import java.util.Date;
import java.time.Instant;

/**
 * Created by memoak on 11/23/2017.
 */

public class LoginViewModel extends ViewModel implements DatePickerDialog.OnDateSetListener {

    private LiveData<HSUser> mUser;
    private WebServiceRepository repository;

    private MutableLiveData<Date> userDateOfBirth;

    public LoginViewModel() {
        userDateOfBirth = new MutableLiveData<>();
    }

    public LiveData<HSUser> registerUser(FirebaseUser user) {

        HSUser hsUser = new HSUser();
        hsUser.email = user.getEmail();
        hsUser.name = user.getDisplayName();
        hsUser.fireBaseUserId = user.getUid();
        hsUser.paymentKey = "DummyPayMentKey";
        hsUser.dateOfBirth = userDateOfBirth.getValue();

        mUser = repository.addUser(hsUser);
        return mUser;
    }

    public void setRepository(WebServiceRepository repository) {
        this.repository = repository;
    }

    public void setUserBirthdate(Date date) {
        userDateOfBirth.setValue(date);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day );
        calendar.set(Calendar.MONTH, month);
        Date dateOfBirth = calendar.getTime();
        setUserBirthdate(dateOfBirth);
    }

    public LiveData<Date> getLiveUserDateOfBirth() {
        return userDateOfBirth;
    }

    public Date getUserDateOfBirth() {
        return userDateOfBirth.getValue();
    }


}
