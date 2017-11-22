package com.hopsquad.hopsquadapp.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by memo on 15/11/17.
 */

public class BaseFragment extends Fragment {

    protected void showToast(int resourceId) {
        Context context = getActivity().getApplicationContext();
        String message = context.getResources().getString(resourceId);
        Toast.makeText(context, message, Toast.LENGTH_SHORT);
    }
}
