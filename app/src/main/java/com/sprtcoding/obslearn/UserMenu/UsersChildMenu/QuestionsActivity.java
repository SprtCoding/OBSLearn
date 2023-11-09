package com.sprtcoding.obslearn.UserMenu.UsersChildMenu;

import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.ANSWERED;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.NOT_VISITED;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.REVIEW;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.UNANSWERED;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_catListModel;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_questList;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_selected_cat_index;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_selected_test_index;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_testList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sprtcoding.obslearn.Adapters.UsersAdapters.QuestionGridAdapter;
import com.sprtcoding.obslearn.Adapters.UsersAdapters.QuestionsAdapter;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {
    private TextView _q_count, _timer, _cat_name, _clear, _mark;
    private RecyclerView _q_rv;
    private MaterialButton _submit_btn;
    private ImageView _fav, _q_list, _left_arrow, _right_arrow, close_drawer, img_marked;
    private DrawerLayout drawerLayout;
    private GridView quest_list_gv;
    private QuestionGridAdapter questionGridAdapter;
    private CountDownTimer countDownTimer;
    private LoadingDialog loadingDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    QuestionsAdapter questionsAdapter;
    String cat_name, timer_value, q_count;
    int questionID, correctQ = 0, finalScore = 0;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.right_drawer_layout);
        inits();
        questionID = 0;

        db = FirebaseFirestore.getInstance();

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        loadingDialog = new LoadingDialog(this);

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        _q_rv.setHasFixedSize(true);
        _q_rv.setLayoutManager(llm);

        questionsAdapter = new QuestionsAdapter(this, g_questList);
        _q_rv.setAdapter(questionsAdapter);

        questionGridAdapter = new QuestionGridAdapter(g_questList.size(), this);
        quest_list_gv.setAdapter(questionGridAdapter);

        setSnapHelper();

        _q_count.setText("1/"+ g_questList.size());
        _cat_name.setText(g_catListModel.get(g_selected_cat_index).getNAME());

        setClickListener();
        startTimer();

        _mark.setOnClickListener(view -> {
            if(img_marked.getVisibility() != View.VISIBLE) {
                img_marked.setVisibility(View.VISIBLE);

                g_questList.get(questionID).setStatus(REVIEW);
            } else {
                img_marked.setVisibility(View.GONE);
                if(g_questList.get(questionID).getSelectedAnswer() != -1) {
                    g_questList.get(questionID).setStatus(ANSWERED);
                } else {
                    g_questList.get(questionID).setStatus(UNANSWERED);
                }
            }
        });
    }

    public void goToQuestion(int position) {
        _q_rv.smoothScrollToPosition(position);

        if(drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawer(GravityCompat.END);
    }

    private void startTimer() {
        long totalTime = (long) g_testList.get(g_selected_test_index).getTime() *60*1000;

        countDownTimer = new CountDownTimer(totalTime + 1000, 1000) {
            @Override
            public void onTick(long remainingTime) {
                @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                        );
                _timer.setText(time);
                // Check if there are 10 seconds remaining and show a toast
                if (remainingTime <= 10000) {
                    _timer.setTextColor(ColorStateList.valueOf(Color.RED));
                }
            }

            @Override
            public void onFinish() {
                openCompleteDialog();
            }
        };

        countDownTimer.start();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setClickListener() {
        _left_arrow.setOnClickListener(view -> {
            if(questionID > 0) {
                _q_rv.smoothScrollToPosition(questionID - 1);
            }
        });

        _right_arrow.setOnClickListener(view -> {
            if(questionID < g_questList.size() - 1) {
                _q_rv.smoothScrollToPosition(questionID + 1);
            } else {
                // This means you've reached the last item in the list
                Toast.makeText(getApplicationContext(), "Time to submit your test.", Toast.LENGTH_SHORT).show();
            }
        });

        _clear.setOnClickListener(view -> {
            g_questList.get(questionID).setSelectedAnswer(-1);
            g_questList.get(questionID).setStatus(UNANSWERED);
            img_marked.setVisibility(View.GONE);
            questionsAdapter.notifyDataSetChanged();
        });

        _q_list.setOnClickListener(view -> {
            if(!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                questionGridAdapter.notifyDataSetChanged();
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        close_drawer.setOnClickListener(view -> {
            if(drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        });

        _submit_btn.setOnClickListener(view -> submitTest());
    }

    private void submitTest() {
        countDownTimer.cancel();
        openCompleteDialog();
    }

    @SuppressLint("SetTextI18n")
    private void openCompleteDialog() {
        View testCompleteDialog = LayoutInflater.from(QuestionsActivity.this).inflate(R.layout.test_complete_layout, null);
        AlertDialog.Builder testCompleteDialogBuilder = new AlertDialog.Builder(QuestionsActivity.this);

        testCompleteDialogBuilder.setView(testCompleteDialog);

        LottieAnimationView anim = testCompleteDialog.findViewById(R.id.anim);
        MaterialButton exit = testCompleteDialog.findViewById(R.id.exit_btn);
        TextView score = testCompleteDialog.findViewById(R.id.score);
        TextView score_total = testCompleteDialog.findViewById(R.id.score_total);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView message = testCompleteDialog.findViewById(R.id.message_lbl);
        TextView title = testCompleteDialog.findViewById(R.id.title);

        final AlertDialog testCompletedDialog = testCompleteDialogBuilder.create();

        testCompletedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        testCompletedDialog.setCanceledOnTouchOutside(false);

        for(int i = 0; i < g_questList.size(); i++) {
            if(g_questList.get(i).getSelectedAnswer() == g_questList.get(i).getCorrectAnswer()) {
                correctQ++;
            }

            finalScore = (correctQ*100)/g_questList.size();
            score_total.setText(correctQ + "/" + g_questList.size());
            score.setText(finalScore + "%");

            if(finalScore <= 74) {
                title.setText("Failed");
                message.setText("Better study again!");
                anim.setAnimation(R.raw.sad);
                anim.animate();
                anim.loop(false);
            }else if(finalScore <= 79) {
                title.setText("Fair");
                message.setText("Your great just keep studying!");
                anim.setAnimation(R.raw.sad);
                anim.animate();
                anim.loop(false);
            }else if(finalScore <= 85) {
                title.setText("Good");
                message.setText("Keep it up!");
                anim.setAnimation(R.raw.congrats);
                anim.animate();
                anim.loop(false);
            }else if(finalScore <= 89) {
                title.setText("Very Good");
                message.setText("Exceptional achievement!");
                anim.setAnimation(R.raw.congrats);
                anim.animate();
                anim.loop(false);
            }else if(finalScore <= 95) {
                title.setText("Very Good");
                message.setText("That was amazing!");
                anim.setAnimation(R.raw.congrats);
                anim.animate();
                anim.loop(false);
            }else if(finalScore <= 100) {
                title.setText("Excellent");
                message.setText("Congratulation, that was great!");
                anim.setAnimation(R.raw.congrats);
                anim.animate();
                anim.loop(false);
            }
        }

        exit.setOnClickListener(view -> {
            loadingDialog.show();
            if(user != null) {
                DBQuery.saveResult(user.getUid(), finalScore, new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        loadingDialog.dismiss();
                        testCompletedDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(QuestionsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });
            }
        });

        testCompletedDialog.show();
    }

    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(_q_rv);

        _q_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View v = snapHelper.findSnapView(recyclerView.getLayoutManager());
                questionID = recyclerView.getLayoutManager().getPosition(v);

                if(g_questList.get(questionID).getStatus() == NOT_VISITED)
                    g_questList.get(questionID).setStatus(UNANSWERED);

                if(g_questList.get(questionID).getStatus() == REVIEW) {
                    img_marked.setVisibility(View.VISIBLE);
                } else {
                    img_marked.setVisibility(View.GONE);
                }

                _q_count.setText(questionID + 1 + "/" + g_questList.size());

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void inits() {
        drawerLayout = findViewById(R.id.drawer_layout);
        _q_count = findViewById(R.id.q_count);
        _timer = findViewById(R.id.timer);
        _cat_name = findViewById(R.id.cat_name);
        _q_rv = findViewById(R.id.q_rv);
        _clear = findViewById(R.id.clear);
        _mark = findViewById(R.id.mark);
        _submit_btn = findViewById(R.id.submit_btn);
        _fav = findViewById(R.id.fav);
        _q_list = findViewById(R.id.q_list);
        _left_arrow = findViewById(R.id.left_arrow);
        _right_arrow = findViewById(R.id.right_arrow);
        close_drawer = findViewById(R.id.close_drawer);
        quest_list_gv = findViewById(R.id.quest_list_gv);
        img_marked = findViewById(R.id.img_marked);

        g_questList.get(0).setStatus(UNANSWERED);
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
}