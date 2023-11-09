package com.sprtcoding.obslearn.UserMenu.UsersChildMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;

import com.sprtcoding.obslearn.Adapters.UsersAdapters.CatGridAdapter;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

public class ExamCategories extends AppCompatActivity {
    private GridView gridView;
    private ImageView _backBtn;
    CatGridAdapter catGridAdapter;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_categories);
        inits();

        catGridAdapter = new CatGridAdapter(this, DBQuery.g_catListModel);
        gridView.setAdapter(catGridAdapter);

        _backBtn.setOnClickListener(view -> finish());

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void inits() {
        gridView = findViewById(R.id.cat_grid_view);
        _backBtn = findViewById(R.id.backBtn);
    }
}