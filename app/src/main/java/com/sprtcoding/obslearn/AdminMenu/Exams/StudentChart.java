package com.sprtcoding.obslearn.AdminMenu.Exams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sprtcoding.obslearn.R;

import java.util.ArrayList;

public class StudentChart extends AppCompatActivity {
    private TextView _testTitle, _backBtn;
    private PieChart pieChart;
    private ArrayList<PieEntry> pieEntries = new ArrayList<>();
    String id, scoreString;
    float scorePercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_chart);
        _init();

        if(getIntent() != null) {
            id = getIntent().getStringExtra("id");
            scoreString = getIntent().getStringExtra("total_score_num");
            scorePercent = getIntent().getFloatExtra("total_score", 0);

            _testTitle.setText(id);

            String[] parts = scoreString.split("/");
            String scoreTxt = parts[0];
            String ItemsTxt = parts[1];
            int score = Integer.parseInt(scoreTxt);
            int items = Integer.parseInt(ItemsTxt);
            int wrong = items - score;

            pieEntries.add(new PieEntry(score, "Correct"));
            pieEntries.add(new PieEntry(wrong, "Mistake"));

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Scores");
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);

            pieChart.getDescription().setEnabled(false);
            pieChart.animateY(1000);
            pieChart.invalidate();
        }

        _backBtn.setOnClickListener(view -> finish());
    }

    private void _init() {
        _backBtn = findViewById(R.id.back_btn);
        _testTitle = findViewById(R.id.testTitle);
        pieChart = findViewById(R.id.pieChart);
    }
}