package com.sprtcoding.obslearn.AdminMenu.Exams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sprtcoding.obslearn.Adapters.CatGridAdminAdapter;
import com.sprtcoding.obslearn.Adapters.UsersAdapters.CatGridAdapter;
import com.sprtcoding.obslearn.AdminDashboardPage;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.SplashScreen;
import com.sprtcoding.obslearn.UserBasicInfo.UserBasicInformation;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

public class AdminExamCat extends AppCompatActivity {
    public static GridView gridView;
    private ImageView _backBtn;
    public static CatGridAdminAdapter catGridAdminAdapter;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_exam_cat);
        inits();

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        _backBtn.setOnClickListener(view -> finish());

    }
    private void loadData() {
        DBQuery.loadCategories(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                catGridAdminAdapter = new CatGridAdminAdapter(AdminExamCat.this, DBQuery.g_catListModel);
                gridView.setAdapter(catGridAdminAdapter);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminExamCat.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inits() {
        gridView = findViewById(R.id.cat_grid_view);
        _backBtn = findViewById(R.id.backBtn);
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        //catGridAdminAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}