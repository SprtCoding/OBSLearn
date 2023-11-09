package com.sprtcoding.obslearn.AdminMenu.Exams;

import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_catListModel;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_firestore;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_selected_cat_index;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_testList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sprtcoding.obslearn.Adapters.TestAdminAdapter;
import com.sprtcoding.obslearn.Adapters.UsersAdapters.TestChildAdapter;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.Model.TestCatModel;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.UserMenu.UsersChildMenu.TestChildCategories;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

public class ShowTestList extends AppCompatActivity {
    private TextView catName;
    private ImageView _backBtn;
    private LinearLayout _no_data_ll;
    public static RecyclerView test_rv;
    private FloatingActionButton _add_btn;
    String _cat_name, _cat_ID;
    public static LoadingDialog loadingDialog;
    public static TestAdminAdapter testAdapter;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_test_list);
        inits();

        _cat_name = getIntent().getStringExtra("cat_name");
        _cat_ID = getIntent().getStringExtra("id");

        g_firestore = FirebaseFirestore.getInstance();

        catName.setText(_cat_name);

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        test_rv.setHasFixedSize(true);
        test_rv.setLayoutManager(llm);

        _add_btn.setOnClickListener(view -> {
            Intent i = new Intent(this, AddChildTest.class);
            i.putExtra("cat_id", g_catListModel.get(g_selected_cat_index).getCAT_ID());
            i.putExtra("fieldNameID", "");
            i.putExtra("fieldNameTime", "");
            i.putExtra("testName", "");
            i.putExtra("testTime", 0);
            startActivity(i);
        });

        _backBtn.setOnClickListener(view -> {
            finish();
        });
    }

    private void loadData() {
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
                    testAdapter = new TestAdminAdapter(ShowTestList.this, DBQuery.g_testList);
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
                Toast.makeText(ShowTestList.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        loadData();
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
        catName = findViewById(R.id.cat_name);
        test_rv = findViewById(R.id.test_rv);
        _backBtn = findViewById(R.id.backBtn);
        _add_btn = findViewById(R.id.add_btn);
        _no_data_ll = findViewById(R.id.no_data_ll);
    }
}