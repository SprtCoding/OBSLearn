package com.sprtcoding.obslearn.AdminMenu.Exams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sprtcoding.obslearn.Adapters.UserGridAdapter;
import com.sprtcoding.obslearn.Model.UserModel;
import com.sprtcoding.obslearn.R;

import java.util.ArrayList;
import java.util.List;

public class Statistics extends AppCompatActivity {
    private GridView myGrid;
    private TextView back_btn;
    private SearchView search_student;
    private AutoCompleteTextView _sectionCTV;
    private List<UserModel> userList = new ArrayList<>();
    UserGridAdapter userGridAdapter;
    private FirebaseFirestore db;
    private CollectionReference userColRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        _init();

        db = FirebaseFirestore.getInstance();
        userColRef = db.collection("USERS");

        String[] section = new String[] {
                "All section",
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

        _sectionCTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(_sectionCTV.getText().toString().equals("All section")) {
                    userGridAdapter.getFilterSection().filter("");
                } else {
                    userGridAdapter.getFilterSection().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userColRef
                .whereEqualTo("ACCOUNT_TYPE", "User")
                .addSnapshotListener((value, error) -> {
                    if(error == null && value != null) {
                        if(!value.isEmpty()) {
                            userList.clear();
                            for(QueryDocumentSnapshot doc : value) {
                                userList.add(new UserModel(
                                        doc.getString("NAME"),
                                        doc.getString("EMAIL_ID"),
                                        doc.getString("USER_ID"),
                                        doc.getString("GENDER"),
                                        doc.getString("SECTION")
                                ));
                            }
                            userGridAdapter = new UserGridAdapter(this, userList);
                            myGrid.setAdapter(userGridAdapter);
                        }
                    }else {
                        assert error != null;
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        search_student.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userGridAdapter.getFilter().filter(newText);
                return true;
            }
        });

        back_btn.setOnClickListener(view -> finish());
    }

    private void _init() {
        _sectionCTV = findViewById(R.id.sectionCTV);
        search_student = findViewById(R.id.search_student);
        myGrid = findViewById(R.id.student_grid_view);
        back_btn = findViewById(R.id.back_btn);
    }
}