package com.sprtcoding.obslearn.AdminMenu.Exams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sprtcoding.obslearn.Adapters.UsersAdapters.StatGridAdapter;
import com.sprtcoding.obslearn.Model.ScoreModel;
import com.sprtcoding.obslearn.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserStat extends AppCompatActivity {
    private TextView stud_name, stud_email, back_btn;
    private CircleImageView userPic;
    private FirebaseFirestore db;
    private CollectionReference userColRef;
    private LinearLayout no_data_ll;
    private List<ScoreModel> scoreModelList = new ArrayList<>();
    private GridView myGrid;
    private BarChart barChart;
    StatGridAdapter statGridAdapter;
    String name, email, uid, gender;
    private final ArrayList<BarEntry> entries = new ArrayList<>();
    private final List<String> xValues = new ArrayList<>();
    private final List<Integer> yValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stat);
        _init();
        _fireStoreInit();

        if(getIntent() != null) {
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            gender = getIntent().getStringExtra("gender");
            uid = getIntent().getStringExtra("uid");

            stud_name.setText(name);
            stud_email.setText(email);

            if(gender.equals("Male")) {
                Picasso.get().load(R.drawable.student_boy).into(userPic);
            } else if (gender.equals("Female")) {
                Picasso.get().load(R.drawable.student_new).into(userPic);
            }

            barChart.getAxisRight().setDrawLabels(false);

            userColRef.document(uid)
                    .collection("MY_SCORE")
                    .addSnapshotListener((value, error) -> {
                        if(error == null && value != null) {
                            if(!value.isEmpty()) {
                                scoreModelList.clear();
                                for(QueryDocumentSnapshot doc : value) {
                                    scoreModelList.add(
                                            0,
                                            new ScoreModel(
                                                    doc.getString("ID"),
                                                    doc.getString("TOTAL_SCORE_NOT_PERCENT"),
                                                    doc.getLong("TOTAL_SCORE").intValue()
                                            )
                                    );
                                    String scoreString = doc.getString("TOTAL_SCORE_NOT_PERCENT");
                                    assert scoreString != null;
                                    String[] splitString = scoreString.split("/");
                                    String scoreTxt = splitString[0];
                                    int score = Integer.parseInt(scoreTxt);

                                    xValues.add(doc.getString("ID"));
                                    yValues.add(Objects.requireNonNull(doc.getLong("TOTAL_SCORE")).intValue());
                                    for(int i = 0; i < yValues.size(); i++) {
                                        entries.add(new BarEntry(i, yValues.get(i)));
                                    }
                                }

                                YAxis yAxis = barChart.getAxisLeft();
                                yAxis.setAxisMaximum(0f);
                                yAxis.setAxisMaximum(100f);
                                yAxis.setAxisLineWidth(2f);
                                yAxis.setAxisLineColor(Color.BLACK);
                                yAxis.setLabelCount(yValues.size());

                                BarDataSet barDataSet = new BarDataSet(entries, "Grades");
                                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                                BarData barData = new BarData(barDataSet);
                                barChart.setData(barData);
                                barChart.setFitBars(true);

                                barChart.animateY(2000);
                                barChart.getDescription().setEnabled(false);
                                barChart.invalidate();

                                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
                                barChart.getXAxis().setLabelRotationAngle(-20);
                                barChart.getXAxis().setDrawLabels(true);
                                barChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
                                barChart.getXAxis().setGranularity(1f);
                                barChart.getXAxis().setGranularityEnabled(true);

                                statGridAdapter = new StatGridAdapter(this, scoreModelList);
                                statGridAdapter.notifyDataSetChanged();
                                myGrid.setAdapter(statGridAdapter);
                                myGrid.smoothScrollToPosition(0);
                            }else {
                                no_data_ll.setVisibility(View.VISIBLE);
                                myGrid.setVisibility(View.GONE);
                            }
                        }else {
                            assert error != null;
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        back_btn.setOnClickListener(view -> finish());

    }

    private void _fireStoreInit() {
        db = FirebaseFirestore.getInstance();
        userColRef = db.collection("USERS");
    }

    private void _init() {
        barChart = findViewById(R.id.barChart);
        stud_name = findViewById(R.id.stud_name);
        stud_email = findViewById(R.id.stud_email);
        userPic = findViewById(R.id.userPic);
        myGrid = findViewById(R.id.score_grid_view);
        no_data_ll = findViewById(R.id.no_data_ll);
        back_btn = findViewById(R.id.back_btn);
    }
}