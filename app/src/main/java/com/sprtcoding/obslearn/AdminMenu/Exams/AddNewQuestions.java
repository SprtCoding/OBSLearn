package com.sprtcoding.obslearn.AdminMenu.Exams;

import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_catListModel;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_selected_cat_index;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.setQuestions;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.R;

import java.util.HashMap;
import java.util.Map;

public class AddNewQuestions extends AppCompatActivity {
    private String catID, testID, QuestionID, Question, Option1, Option2, Option3, Option4, TEST_ID;
    private ImageView backBtn;
    private MaterialButton save_btn;
    private TextView cat_name;
    private TextInputEditText question_ET, option1_ET, option2_ET, option3_ET, option4_ET;
    private AutoCompleteTextView answerCTV;
    private ProgressDialog loading;
    private boolean isUpdate;
    private int myAnswer = 0, Answer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_questions);
        init();

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        catID = g_catListModel.get(g_selected_cat_index).getCAT_ID();
        testID = getIntent().getStringExtra("TestID");

        loading = new ProgressDialog(this);
        loading.setTitle("Updating");
        loading.setMessage("Please wait...");

        if(getIntent() != null) {
            isUpdate = getIntent().getBooleanExtra("isUpdate", false);
            QuestionID = getIntent().getStringExtra("QuestionID");
            Question = getIntent().getStringExtra("Question");
            Option1 = getIntent().getStringExtra("Option1");
            Option2 = getIntent().getStringExtra("Option2");
            Option3 = getIntent().getStringExtra("Option3");
            Option4 = getIntent().getStringExtra("Option4");
            Answer = getIntent().getIntExtra("ANSWER", 0);
            TEST_ID = getIntent().getStringExtra("TEST_ID");

            if(isUpdate) {
                cat_name.setText("Update Question (" + TEST_ID + ")");
                question_ET.setText(Question);
                option1_ET.setText(Option1);
                option2_ET.setText(Option2);
                option3_ET.setText(Option3);
                option4_ET.setText(Option4);

                answerCTV.setText(String.valueOf(Answer));
                save_btn.setText("Update");
            } else {
                cat_name.setText("Add Question");
                save_btn.setText("Save");
            }
        }

        //gender dropdown
        String[] answer = new String[] {
                "Option 1",
                "Option 2",
                "Option 3",
                "Option 4"
        };

        ArrayAdapter<String> answerAdapter = new ArrayAdapter<>(
                this,
                R.layout.gender_dropdown_item,
                answer
        );

        answerCTV.setAdapter(answerAdapter);//end of gender dropdown
        answerCTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).toString().equals("Option 1")) {
                    myAnswer = 1;
                }else if(adapterView.getItemAtPosition(i).toString().equals("Option 2")) {
                    myAnswer = 2;
                }else if(adapterView.getItemAtPosition(i).toString().equals("Option 3")) {
                    myAnswer = 3;
                }else if(adapterView.getItemAtPosition(i).toString().equals("Option 4")) {
                    myAnswer = 4;
                }
            }
        });

        if(save_btn.getText().toString().equals("Save")) {
            save_btn.setOnClickListener(view -> {
                String question = question_ET.getText().toString();
                String option1 = option1_ET.getText().toString();
                String option2 = option2_ET.getText().toString();
                String option3 = option3_ET.getText().toString();
                String option4 = option4_ET.getText().toString();
                String answers = answerCTV.getText().toString();

                if(TextUtils.isEmpty(question)) {
                    question_ET.setError("Question is required!");
                    question_ET.requestFocus();
                }else if(TextUtils.isEmpty(option1)) {
                    option1_ET.setError("Option 1 is required!");
                    option1_ET.requestFocus();
                }else if(TextUtils.isEmpty(option2)) {
                    option2_ET.setError("Option 2 is required!");
                    option2_ET.requestFocus();
                }else if(TextUtils.isEmpty(option3)) {
                    option3_ET.setError("Option 3 is required!");
                    option3_ET.requestFocus();
                }else if(TextUtils.isEmpty(option4)) {
                    option4_ET.setError("Option 4 is required!");
                    option4_ET.requestFocus();
                }else if(TextUtils.isEmpty(answers)) {
                    answerCTV.setError("Answer is required!");
                    answerCTV.requestFocus();
                }else {
                    setQuestion(question, option1, option2, option3, option4, myAnswer);
                }

            });
        } else {
            save_btn.setOnClickListener(view -> {
                loading.show();

                String question = question_ET.getText().toString();
                String option1 = option1_ET.getText().toString();
                String option2 = option2_ET.getText().toString();
                String option3 = option3_ET.getText().toString();
                String option4 = option4_ET.getText().toString();
                String answers = answerCTV.getText().toString();

                if(TextUtils.isEmpty(question)) {
                    question_ET.setError("Question is required!");
                    question_ET.requestFocus();
                    loading.dismiss();
                }else if(TextUtils.isEmpty(option1)) {
                    option1_ET.setError("Option 1 is required!");
                    option1_ET.requestFocus();
                    loading.dismiss();
                }else if(TextUtils.isEmpty(option2)) {
                    option2_ET.setError("Option 2 is required!");
                    option2_ET.requestFocus();
                    loading.dismiss();
                }else if(TextUtils.isEmpty(option3)) {
                    option3_ET.setError("Option 3 is required!");
                    option3_ET.requestFocus();
                    loading.dismiss();
                }else if(TextUtils.isEmpty(option4)) {
                    option4_ET.setError("Option 4 is required!");
                    option4_ET.requestFocus();
                    loading.dismiss();
                }else if(TextUtils.isEmpty(answers)) {
                    answerCTV.setError("Answer is required!");
                    answerCTV.requestFocus();
                    loading.dismiss();
                }else {
                    updateQuestion(question, option1, option2, option3, option4, myAnswer);
                }
            });
        }

        backBtn.setOnClickListener(view -> finish());
    }

    private void updateQuestion(String question, String option1, String option2, String option3, String option4, int myAnswer) {
        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        CollectionReference qColRef = DBQuery.g_firestore.collection("QUESTIONS");

        Map<String, Object> qData = new HashMap<>();

        qData.put("QUESTION", question);
        qData.put("A", option1);
        qData.put("B", option2);
        qData.put("C", option3);
        qData.put("D", option4);
        qData.put("ANSWER", myAnswer);
        qData.put("QUESTION_ID", QuestionID);

        qColRef.document(QuestionID)
                .update(qData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Question updated successfully.", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void setQuestion(String question, String option1, String option2, String option3, String option4, int myAnswer) {
        setQuestions(catID,
                testID,
                question,
                option1,
                option2,
                option3,
                option4,
                myAnswer,
                new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AddNewQuestions.this, "Added.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AddNewQuestions.this, QuestionsList.class);
                        i.putExtra("TestID", testID);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AddNewQuestions.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void init() {
        cat_name = findViewById(R.id.cat_name);
        backBtn = findViewById(R.id.backBtn);
        save_btn = findViewById(R.id.save_btn);
        question_ET = findViewById(R.id.question_ET);
        option1_ET = findViewById(R.id.option1_ET);
        option2_ET = findViewById(R.id.option2_ET);
        option3_ET = findViewById(R.id.option3_ET);
        option4_ET = findViewById(R.id.option4_ET);
        answerCTV = findViewById(R.id.answerCTV);
    }
}