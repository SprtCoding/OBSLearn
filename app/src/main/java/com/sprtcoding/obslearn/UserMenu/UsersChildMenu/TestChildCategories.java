package com.sprtcoding.obslearn.UserMenu.UsersChildMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sprtcoding.obslearn.Adapters.UsersAdapters.TestChildAdapter;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

public class TestChildCategories extends AppCompatActivity {
    private TextView catName;
    private ImageView _backBtn;
    private RecyclerView test_rv;
    private LinearLayout _no_data_ll;
    String _cat_name, _cat_ID;
    LoadingDialog loadingDialog;
    TestChildAdapter testAdapter;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private Handler handler;
    private final int delayMillis = 6000; // 6 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_child_categories);
        inits();

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        _cat_name = getIntent().getStringExtra("cat_name");
        _cat_ID = getIntent().getStringExtra("id");

        catName.setText(_cat_name);

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        test_rv.setHasFixedSize(true);
        test_rv.setLayoutManager(llm);
        handler = new Handler();

        // Schedule loadData() to be called every 6 seconds
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadData();
//                handler.postDelayed(this, delayMillis);
//            }
//        }, delayMillis);

        _backBtn.setOnClickListener(view -> {
            finish();
        });

    }

    public void loadData() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        // Clear the existing data in the RecyclerView
        if (testAdapter != null) {
            DBQuery.g_testList.clear();
            testAdapter.clearData();
        }

        DBQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                if (DBQuery.g_testList != null && !DBQuery.g_testList.isEmpty()) {
                    testAdapter = new TestChildAdapter(TestChildCategories.this, DBQuery.g_testList);
                    test_rv.setAdapter(testAdapter);
                } else {
                    // Handle the case where the list is empty
                    // You might want to show a message to the user or take appropriate action
                    Log.d("TEST", "empty test list");
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(TestChildCategories.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        }, _no_data_ll, test_rv, loadingDialog);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onDestroy() {
        // Remove any pending callbacks to avoid memory leaks
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//        }
        super.onDestroy();
    }

    private void inits() {
        catName = findViewById(R.id.cat_name);
        test_rv = findViewById(R.id.test_rv);
        _backBtn = findViewById(R.id.backBtn);
        _no_data_ll = findViewById(R.id.no_data_ll);
    }
}