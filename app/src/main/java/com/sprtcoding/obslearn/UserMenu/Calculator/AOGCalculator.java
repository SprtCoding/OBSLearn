package com.sprtcoding.obslearn.UserMenu.Calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.sprtcoding.obslearn.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AOGCalculator extends AppCompatActivity {
    private TextView date_of_visit, lmp, expected_date_confinement, aog_in_days, aog_in_weeks, _trimester, clear;
    private ImageView back_btn;
    MaterialDatePicker<Long> dateOfVisitPickerDialog, dateOfLmpPickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aogcalculator);
        _init();

        date_of_visit.setOnClickListener(view -> showDatePickerDialog());
        lmp.setOnClickListener(view -> showDateOfLmpPickerDialog());

        clear.setOnClickListener(view -> clearFields());

        back_btn.setOnClickListener(view -> finish());

    }

    private void showDatePickerDialog() {
        // Create a date picker builder
        dateOfVisitPickerDialog = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date of Visit")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        dateOfVisitPickerDialog.addOnPositiveButtonClickListener(selection -> {
            String date = new SimpleDateFormat("LLL dd yyyy", Locale.getDefault()).format(new Date(selection));
            date_of_visit.setText(date);
        });
        dateOfVisitPickerDialog.show(getSupportFragmentManager(), "date");
    }

    private void showDateOfLmpPickerDialog() {
        // Create a date picker builder
        dateOfLmpPickerDialog = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date of LMP")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        dateOfLmpPickerDialog.addOnPositiveButtonClickListener(selection -> {
            String date = new SimpleDateFormat("LLL dd yyyy", Locale.getDefault()).format(new Date(selection));
            lmp.setText(date);
        });
        dateOfLmpPickerDialog.show(getSupportFragmentManager(), "date");
    }

    @SuppressLint("SetTextI18n")
    private void calculatePregnancyData() {
        if (dateOfVisitPickerDialog.getSelection() != null && dateOfLmpPickerDialog.getSelection() != null) {
            long visitTime = dateOfVisitPickerDialog.getSelection();
            long lmpTime = dateOfLmpPickerDialog.getSelection();

            Calendar visitCalendar = Calendar.getInstance();
            visitCalendar.setTimeInMillis(visitTime);

            Calendar lmpCalendar = Calendar.getInstance();
            lmpCalendar.setTimeInMillis(lmpTime);

            long daysDifference = (visitCalendar.getTimeInMillis() - lmpCalendar.getTimeInMillis()) / (1000 * 60 * 60 * 24);
            int weeks = (int) (daysDifference / 7);
            int remainingDays = (int) (daysDifference % 7);
            int trimester = (weeks < 13) ? 1 : (weeks < 27) ? 2 : 3; // Assuming a 40-week pregnancy.

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            // Calculate EDC by adding 280 days to the LMP date
            Calendar edcCalendar = Calendar.getInstance();
            edcCalendar.setTimeInMillis(lmpTime);
            edcCalendar.add(Calendar.DAY_OF_YEAR, 280);
            Date confinementDate = edcCalendar.getTime();

            // Create a Unicode character for the fraction
            String fractionalDays = String.format(Locale.getDefault(), "%d\u2044%d", remainingDays, 7);

//            String weeksAndDays = weeks + " weeks";
//            if (remainingDays > 0) {
//                weeksAndDays += " & " + remainingDays + " days";
//            }

            String weeksAndDays = weeks + " weeks";
            if (remainingDays > 0) {
                weeksAndDays += " & " + fractionalDays + " days";
            }

            expected_date_confinement.setText(sdf.format(confinementDate));
            String aogDays = String.format(Locale.getDefault(), "%d %s", daysDifference, fractionalDays);
            aog_in_days.setText(aogDays);
            //aog_in_days.setText(daysDifference + " Days");
            aog_in_weeks.setText(weeksAndDays);
            _trimester.setText(trimester + " Trimester");
        }
    }

    @SuppressLint("SetTextI18n")
    private void clearFields() {
        // Clear text fields
        expected_date_confinement.setText("");
        aog_in_days.setText("");
        aog_in_weeks.setText("");
        _trimester.setText("");
    }

    private void _init() {
        back_btn = findViewById(R.id.back_btn);
        date_of_visit = findViewById(R.id.date_of_visit);
        lmp = findViewById(R.id.lmp);
        expected_date_confinement = findViewById(R.id.expected_date_confinement);
        aog_in_days = findViewById(R.id.aog_in_days);
        aog_in_weeks = findViewById(R.id.aog_in_weeks);
        _trimester = findViewById(R.id.trimester);
        clear = findViewById(R.id.clear);

        lmp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculatePregnancyData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}