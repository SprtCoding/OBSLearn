package com.sprtcoding.obslearn.UserMenu.UsersChildMenu;

import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_catListModel;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_questList;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_selected_cat_index;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_selected_test_index;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_testList;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

public class StartTestActivity extends AppCompatActivity {
    private TextView _cat_name, _test_no, _q_count, _timer_count;
    private MaterialButton _start_btn;
    private LinearLayout no_data_ll, with_data_ll;
    private ImageView _backBtn;
    private LoadingDialog loadingDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
        inits();

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        DBQuery.loadQuestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                setData();
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(StartTestActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        }, loadingDialog, no_data_ll, with_data_ll, g_testList.get(g_selected_test_index).getTestID());

        _backBtn.setOnClickListener(view -> {
            finish();
            g_questList.clear();
        });

        _start_btn.setOnClickListener(view -> {
            Intent i = new Intent(this, QuestionsActivity.class);
            i.putExtra("cat_name", g_catListModel.get(g_selected_cat_index).getNAME());
            i.putExtra("timer_value", String.valueOf(g_testList.get(g_selected_test_index).getTime()));
            i.putExtra("q_count", String.valueOf(g_questList.size()));
            startActivity(i);
        });
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
        _cat_name = findViewById(R.id.cat_name);
        _test_no = findViewById(R.id.test_no);
        _q_count = findViewById(R.id.q_counts);
        _timer_count = findViewById(R.id.timer_count);
        _start_btn = findViewById(R.id.start_btn);
        _backBtn = findViewById(R.id.backBtn);
        no_data_ll = findViewById(R.id.no_data_ll);
        with_data_ll = findViewById(R.id.with_data_ll);
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        _cat_name.setText(g_catListModel.get(g_selected_cat_index).getNAME());
        _test_no.setText("Test No. " + (g_selected_test_index + 1));
        _q_count.setText(String.valueOf(g_questList.size()));
        _timer_count.setText(g_testList.get(g_selected_test_index).getTime() + " m");
    }
}