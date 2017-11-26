package com.hopsquad.hopsquadapp.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.hopsquad.hopsquadapp.R;
import com.hopsquad.hopsquadapp.fragments.SettingsFragment;
import com.hopsquad.hopsquadapp.fragments.TapListFragment;
import com.hopsquad.hopsquadapp.fragments.UserHistoryFragment;
import com.hopsquad.hopsquadapp.viewmodels.MainViewModel;

public class MainActivity extends BaseActivity {

    private MainViewModel viewModel;
    public static final String TAP_LIST_FRAGMENT_TAG = "TAP_LIST";
    public static final String USER_HISTORY_FRAGMENT_TAG = "USER_HISTORY";
    public static final String SETTINGS_FRAGMENT_TAG = "SETTINGS";

    private Fragment currentFragment = null;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            FragmentManager manager = getSupportFragmentManager();
            String tag = null;
            Class fragmentClass = null;

            if (viewModel.getSelectedNavigationItemId() == item.getItemId()) {
                return false;
            }

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    tag = TAP_LIST_FRAGMENT_TAG;
                    fragmentClass = TapListFragment.class;
                    break;
                case R.id.navigation_dashboard:
                    tag = USER_HISTORY_FRAGMENT_TAG;
                    fragmentClass = UserHistoryFragment.class;
                    break;
                case R.id.navigation_notifications:
                    tag = SETTINGS_FRAGMENT_TAG;
                    fragmentClass = SettingsFragment.class;
                    break;
            }

            fragment = manager.findFragmentByTag(tag);

            if (fragment == null) {
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }


            if (fragment != null && tag != null) {
                viewModel.setSelectedNavigationItemId(item.getItemId());
                switchFragment(fragment, tag);
                return true;
            }
            return false;
        }

    };

    private void switchFragment(Fragment fragment, String tag) {
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        int selectedNavigationItemId = viewModel.getSelectedNavigationItemId();

        if (selectedNavigationItemId == 0) {
            navigation.setSelectedItemId(R.id.navigation_home);
        } else {
            navigation.setSelectedItemId(selectedNavigationItemId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menuSignOut:
                signOut();
                return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void signOut() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
