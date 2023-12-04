package com.sprtcoding.obslearn.AdminMenu.Exams;

import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_firestore;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sprtcoding.obslearn.Adapters.CatGridAdminAdapter;
import com.sprtcoding.obslearn.Adapters.TestAdminAdapter;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddChildTest extends AppCompatActivity {
    private TextInputEditText _test_cat_name_ET, _test_time_ET;
    private ImageView backBtn;
    private MaterialButton _save_btn;
    private TextView title;
    private LoadingDialog loadingDialog;
    String cat_ID, fieldNameID, fieldNameTime, testName;
    int testTime;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_test);
        init();
        cat_ID = getIntent().getStringExtra("cat_id");
        fieldNameID = getIntent().getStringExtra("fieldNameID");
        fieldNameTime = getIntent().getStringExtra("fieldNameTime");
        testName = getIntent().getStringExtra("testName");
        testTime = getIntent().getIntExtra("testTime", 0);

        if(!fieldNameID.equals("") && !fieldNameTime.equals("") && !testName.equals("") && testTime != 0) {
            title.setText("Update Test");
            _save_btn.setText("Update");

            _test_cat_name_ET.setText(testName);
            _test_time_ET.setText(String.valueOf(testTime));
        }

        loadingDialog = new LoadingDialog(this);

        if(!_save_btn.getText().toString().equals("Update")) {
            _save_btn.setOnClickListener(view -> {
                loadingDialog.show();
                String name = _test_cat_name_ET.getText().toString();
                String time = _test_time_ET.getText().toString();

                if(TextUtils.isEmpty(name)) {
                    _test_cat_name_ET.setError("Required!");
                    _test_cat_name_ET.requestFocus();
                    loadingDialog.dismiss();
                } else if (TextUtils.isEmpty(time)) {
                    _test_time_ET.setError("Required!");
                    _test_time_ET.requestFocus();
                    loadingDialog.dismiss();
                } else {
                    setTest(name, time);
                }
            });
        }else {
            _save_btn.setOnClickListener(view -> {
                loadingDialog.show();
                String name = _test_cat_name_ET.getText().toString();
                String time = _test_time_ET.getText().toString();

                if(TextUtils.isEmpty(name)) {
                    _test_cat_name_ET.setError("Required!");
                    _test_cat_name_ET.requestFocus();
                    loadingDialog.dismiss();
                } else if (TextUtils.isEmpty(time)) {
                    _test_time_ET.setError("Required!");
                    _test_time_ET.requestFocus();
                    loadingDialog.dismiss();
                } else {
                    updateTest(name, time);
                }
            });
        }

        backBtn.setOnClickListener(view -> finish());
    }

    private void updateTest(String name, String time) {
        if(!DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTestID().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))) {
            DBQuery.updateTestName(
                    cat_ID,
                    fieldNameID,
                    fieldNameTime,
                    testName,
                    name,
                    Integer.parseInt(time),
                    new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            AdminExamCat.catGridAdminAdapter.notifyDataSetChanged();
                            Toast.makeText(AddChildTest.this, testName+" Updated successfully.", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                            finish();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(AddChildTest.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    }
            );
        } else {
            Toast.makeText(this, "Test already exist!", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setTest(String name, String time) {
        if (!DBQuery.g_testList.isEmpty() && DBQuery.g_selected_test_index < DBQuery.g_testList.size()) {
            if (!DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTestID().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))) {
                DBQuery.setTestInfo(
                        cat_ID,
                        name,
                        Integer.parseInt(time),
                        new MyCompleteListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onSuccess() {
                                AdminExamCat.catGridAdminAdapter.notifyDataSetChanged();
                                ShowTestList.testAdapter.notifyDataSetChanged();
                                Toast.makeText(AddChildTest.this, "Added.", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                                Intent i = new Intent(AddChildTest.this, AdminExamCat.class);
                                startActivity(i);
                                finish();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(AddChildTest.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Test already exists!", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        } else {
            // Handle the case where g_testList is empty or g_selected_test_index is out of bounds
            // You might want to show an error message or take appropriate action
            // Toast.makeText(this, "Test list is empty or index out of bounds.", Toast.LENGTH_SHORT).show();
            // loadingDialog.dismiss();

            DocumentReference testDoc = g_firestore.collection("EXAMINATION")
                    .document(cat_ID)
                    .collection("TESTS_LIST")
                    .document("TESTS_INFO");

            // Construct the field names for the new test
            String nextTestIDField = "TEST1_ID";
            String nextTestTimeField = "TEST1_TIME";

            Map<String, Object> testData = new HashMap<>();

            // Add the new test ID and time to the map
            testData.put(nextTestIDField, name);
            testData.put(nextTestTimeField, Integer.parseInt(time));

            // Update the Firestore document with the new or modified test IDs
            testDoc.set(testData)
                    .addOnSuccessListener(unused -> {
                        DocumentReference testDocUpdate = g_firestore.collection("EXAMINATION")
                                .document(cat_ID);

                        Map<String, Object> map = new ArrayMap<>();
                        map.put("NO_OF_TEST", 1);

                        testDocUpdate.update(map)
                                .addOnSuccessListener(unused1 -> {
                                    AdminExamCat.catGridAdminAdapter.notifyDataSetChanged();
                                    ShowTestList.testAdapter.notifyDataSetChanged();
                                    Toast.makeText(AddChildTest.this, "Added.", Toast.LENGTH_SHORT).show();
                                    loadingDialog.dismiss();
                                    Intent i = new Intent(AddChildTest.this, AdminExamCat.class);
                                    startActivity(i);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(AddChildTest.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    loadingDialog.dismiss();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddChildTest.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    });
        }
    }

    private void init() {
        backBtn = findViewById(R.id.backBtn);
        _test_cat_name_ET = findViewById(R.id.test_cat_name_ET);
        _test_time_ET = findViewById(R.id.test_time_ET);
        _save_btn = findViewById(R.id.save_btn);
        title = findViewById(R.id.title);
    }
}