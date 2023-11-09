package com.sprtcoding.obslearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.sprtcoding.obslearn.AdminMenu.Exams.AboutUs;
import com.sprtcoding.obslearn.AdminMenu.Exams.AdminExamCat;
import com.sprtcoding.obslearn.AdminMenu.Exams.Statistics;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

public class AdminDashboardPage extends AppCompatActivity {
    private CardView _examCardView, _statisticsCardView, _aboutCardView, _signOutCardView;
    private FirebaseAuth mAuth;
    ProgressDialog _loading;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_page);
        _var();

        mAuth = FirebaseAuth.getInstance();

        _loading = new ProgressDialog(this);
        _loading.setTitle("Loading");
        _loading.setMessage("Please wait...");

        _examCardView.setOnClickListener(view -> {
            _loading.show();
            Handler handler = new Handler();
            Runnable runnable = () -> {
                _loading.dismiss();
                Intent gotoExamCat = new Intent(this, AdminExamCat.class);
                startActivity(gotoExamCat);
            };
            handler.postDelayed(runnable, 2000);
        });

        _statisticsCardView.setOnClickListener(view -> {
            _loading.show();
            Handler handler = new Handler();
            Runnable runnable = () -> {
                _loading.dismiss();
                Intent gotoStatCat = new Intent(this, Statistics.class);
                startActivity(gotoStatCat);
            };
            handler.postDelayed(runnable, 2000);
        });

        _aboutCardView.setOnClickListener(view -> {
            _loading.show();
            Handler handler = new Handler();
            Runnable runnable = () -> {
                _loading.dismiss();
                Intent gotoAboutCat = new Intent(this, AboutUs.class);
                startActivity(gotoAboutCat);
            };
            handler.postDelayed(runnable, 2000);
        });

        _signOutCardView.setOnClickListener(view -> {
            mAuth.signOut();
            Intent gotoLogin = new Intent(this, LoginPage.class);
            startActivity(gotoLogin);
            finish();
        });
    }

    private void _var() {
        _examCardView = findViewById(R.id.examCardView);
        _statisticsCardView = findViewById(R.id.statisticsCardView);
        _aboutCardView = findViewById(R.id.aboutCardView);
        _signOutCardView = findViewById(R.id.signOutCardView);
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