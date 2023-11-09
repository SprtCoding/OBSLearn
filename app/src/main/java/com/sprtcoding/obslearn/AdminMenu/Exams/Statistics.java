package com.sprtcoding.obslearn.AdminMenu.Exams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
                                        doc.getString("GENDER")
                                ));
                            }
                            userGridAdapter = new UserGridAdapter(this, userList);
                            myGrid.setAdapter(userGridAdapter);
                        }
                    }else {
                        Toast.makeText(this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        back_btn.setOnClickListener(view -> finish());
    }

    private void _init() {
        myGrid = findViewById(R.id.student_grid_view);
        back_btn = findViewById(R.id.back_btn);
    }
}