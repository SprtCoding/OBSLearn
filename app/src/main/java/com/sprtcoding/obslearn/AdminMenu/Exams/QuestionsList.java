package com.sprtcoding.obslearn.AdminMenu.Exams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sprtcoding.obslearn.Adapters.QuestionsAdapter;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.Model.QuestionModel;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.UserMenu.UsersChildMenu.StartTestActivity;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionsList extends AppCompatActivity {
    private RecyclerView question_rv;
    private ImageView backBtn;
    private LinearLayout no_data_ll, with_data_ll;
    private FloatingActionButton add_btn;
    private String testID;
    QuestionsAdapter questionsAdapter;
    private LoadingDialog loadingDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_list);
        init();

        testID = getIntent().getStringExtra("TestID");

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        question_rv.setHasFixedSize(true);
        question_rv.setLayoutManager(llm);

        add_btn.setOnClickListener(view -> {
            Intent i = new Intent(QuestionsList.this, AddNewQuestions.class);
            i.putExtra("TestID", testID);
            startActivity(i);
            finish();
        });

        backBtn.setOnClickListener(view -> finish());

    }

    public void loadData() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        DBQuery.loadQuestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                questionsAdapter = new QuestionsAdapter(QuestionsList.this, DBQuery.g_questList);
                question_rv.setAdapter(questionsAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(QuestionsList.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        }, loadingDialog, no_data_ll, with_data_ll, testID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
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

    private void init() {
        backBtn = findViewById(R.id.backBtn);
        add_btn = findViewById(R.id.add_btn);
        question_rv = findViewById(R.id.question_rv);
        no_data_ll = findViewById(R.id.no_data_ll);
        with_data_ll = findViewById(R.id.with_data_q_ll);
    }
}