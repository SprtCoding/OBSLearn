package com.sprtcoding.obslearn.UserBasicInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.UserDashBoard;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

import java.util.Calendar;
import java.util.TimeZone;

public class UserBasicInformation extends AppCompatActivity {
    private AutoCompleteTextView _genderCTV, _sectionCTV;
    private MaterialButton _dateBtn, _saveBtn;
    private TextInputEditText _ageET;
    private LoadingDialog _loading;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDb;
    private DatabaseReference userRef;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_basic_information);
        _var();

        _loading = new LoadingDialog(this);

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        //firebase authentication instance
        mAuth = FirebaseAuth.getInstance();
        //firebase database and reference
        mDb = FirebaseDatabase.getInstance();
        userRef = mDb.getReference("UsersData");

        String[] section = new String[] {
                "A",
                "B",
                "C",
                "D",
                "E",
                "F"
        };

        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(
                this,
                R.layout.gender_dropdown_item,
                section
        );

        _sectionCTV.setAdapter(sectionAdapter);

        //gender dropdown
        String[] gender = new String[] {
                "Male",
                "Female"
        };

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this,
                R.layout.gender_dropdown_item,
                gender
        );

        _genderCTV.setAdapter(genderAdapter);//end of gender dropdown

        //date picker
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        long today = MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder<Long> datePickerBuilder = MaterialDatePicker.Builder.datePicker();
        datePickerBuilder.setTitleText("Select Date of Birth");
        datePickerBuilder.setSelection(today);
        final MaterialDatePicker<Long> materialDatePicker = datePickerBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> _dateBtn.setText(materialDatePicker.getHeaderText()));

        _dateBtn.setOnClickListener(view -> {
            materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        });

        _saveBtn.setOnClickListener(view -> {
            _loading.show();
            String _age = _ageET.getText().toString();
            String _gender = _genderCTV.getText().toString();
            String _section = _sectionCTV.getText().toString();
            String _bod = _dateBtn.getText().toString();

            if(TextUtils.isEmpty(_age)) {
                Toast.makeText(this, "Age cannot be empty!", Toast.LENGTH_SHORT).show();
                _loading.dismiss();
            }else if(TextUtils.isEmpty(_gender)) {
                Toast.makeText(this, "Please select your gender!", Toast.LENGTH_SHORT).show();
                _loading.dismiss();
            }else if(TextUtils.isEmpty(_section)) {
                Toast.makeText(this, "Please select your section!", Toast.LENGTH_SHORT).show();
                _loading.dismiss();
            }else if(TextUtils.isEmpty(_bod)) {
                Toast.makeText(this, "Please select your Birthdate!", Toast.LENGTH_SHORT).show();
                _loading.dismiss();
            }else {
                DBQuery.updateUsers(_bod, Integer.parseInt(_age), _gender, _section, new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(UserBasicInformation.this, "Account information saved!", Toast.LENGTH_SHORT).show();
                        _loading.dismiss();
                        Intent gotoDashboard = new Intent(UserBasicInformation.this, UserDashBoard.class);
                        startActivity(gotoDashboard);
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        _loading.dismiss();
                        Toast.makeText(UserBasicInformation.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    private void _var() {
        _genderCTV = findViewById(R.id.genderCTV);
        _dateBtn = findViewById(R.id.dateBtn);
        _saveBtn = findViewById(R.id.saveBtn);
        _ageET = findViewById(R.id.ageET);
        _sectionCTV = findViewById(R.id.sectionCTV);
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
}