package com.sprtcoding.obslearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sprtcoding.obslearn.UserMenu.AboutFragment;
import com.sprtcoding.obslearn.UserMenu.HomeFragment;
import com.sprtcoding.obslearn.UserMenu.LearnFragment;
import com.sprtcoding.obslearn.UserMenu.ProfileFragment;
import com.sprtcoding.obslearn.UserMenu.StatisticFragment;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

public class UserDashBoard extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);
        _var();

        replaceFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.bottom_statistics:
                    replaceFragment(new StatisticFragment());
                    break;
                case R.id.bottom_learn:
                    replaceFragment(new LearnFragment());
                    break;
                case R.id.bottom_about:
                    replaceFragment(new AboutFragment());
                    break;
                case R.id.bottom_profile:
                    replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void _var() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}