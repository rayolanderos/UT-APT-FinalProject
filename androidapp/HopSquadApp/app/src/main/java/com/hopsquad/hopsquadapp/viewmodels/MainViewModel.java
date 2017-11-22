package com.hopsquad.hopsquadapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by memo on 15/11/17.
 */

public class MainViewModel extends ViewModel {
    
    private int selectedNavigationItemId = 0;
    
    public int getSelectedNavigationItemId() {
        return selectedNavigationItemId;
    }

    public void setSelectedNavigationItemId(int selectedItemId) {
        selectedNavigationItemId = selectedItemId;
    }
}
