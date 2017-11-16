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
import com.hopsquad.hopsquadapp.viewmodels.TapListViewModel;

public class MainActivity extends BaseActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new TapListFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new UserHistoryFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new SettingsFragment();
                    break;
            }

            if (fragment != null) {
                switchFragment(fragment);
                return true;
            }
            return false;
        }

    };

    private void switchFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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

    private void signOut() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
