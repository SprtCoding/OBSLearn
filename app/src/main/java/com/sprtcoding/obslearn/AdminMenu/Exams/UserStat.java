package com.sprtcoding.obslearn.AdminMenu.Exams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sprtcoding.obslearn.Adapters.UsersAdapters.StatGridAdapter;
import com.sprtcoding.obslearn.Model.ScoreModel;
import com.sprtcoding.obslearn.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserStat extends AppCompatActivity {
    private TextView stud_name, stud_email, back_btn;
    private CircleImageView userPic;
    private FirebaseFirestore db;
    private CollectionReference userColRef;
    private LinearLayout no_data_ll;
    private List<ScoreModel> scoreModelList = new ArrayList<>();
    private GridView myGrid;
    StatGridAdapter statGridAdapter;
    String name, email, uid, gender;

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

            userColRef.document(uid)
                    .collection("MY_SCORE")
                    .addSnapshotListener((value, error) -> {
                        if(error == null && value != null) {
                            if(!value.isEmpty()) {
                                scoreModelList.clear();
                                for(QueryDocumentSnapshot doc : value) {
                                    scoreModelList.add(
                                            new ScoreModel(
                                                    doc.getString("ID"),
                                                    doc.getString("TOTAL_SCORE_NOT_PERCENT"),
                                                    doc.getLong("TOTAL_SCORE").intValue()
                                            )
                                    );
                                }
                                statGridAdapter = new StatGridAdapter(this, scoreModelList);
                                myGrid.setAdapter(statGridAdapter);
                            }else {
                                no_data_ll.setVisibility(View.VISIBLE);
                                myGrid.setVisibility(View.GONE);
                            }
                        }else {
                            Toast.makeText(this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
        stud_name = findViewById(R.id.stud_name);
        stud_email = findViewById(R.id.stud_email);
        userPic = findViewById(R.id.userPic);
        myGrid = findViewById(R.id.score_grid_view);
        no_data_ll = findViewById(R.id.no_data_ll);
        back_btn = findViewById(R.id.back_btn);
    }
}