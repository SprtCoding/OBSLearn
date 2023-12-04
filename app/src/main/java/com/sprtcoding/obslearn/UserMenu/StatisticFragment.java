package com.sprtcoding.obslearn.UserMenu;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sprtcoding.obslearn.Adapters.UsersAdapters.StatGridAdapter;
import com.sprtcoding.obslearn.Model.ScoreModel;
import com.sprtcoding.obslearn.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatisticFragment extends Fragment {
    View view;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private FirebaseFirestore db;
    private CollectionReference userColRef;
    private TextView _fullNameTB, _emailTB;
    private GridView myGrid;
    StatGridAdapter statGridAdapter;
    private List<ScoreModel> scoreModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_statistic, container, false);
        _var();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();
        userColRef = db.collection("USERS");

        if(mUser != null) {
            userColRef.document(mUser.getUid())
                    .addSnapshotListener((value, error) -> {
                        if(error == null && value != null) {
                            if(value.exists()) {
                                String name = value.getString("NAME");
                                String email = value.getString("EMAIL_ID");

                                _fullNameTB.setText(name);
                                _emailTB.setText(email);
                            }
                        }else {
                            Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            userColRef.document(mUser.getUid())
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
                                statGridAdapter = new StatGridAdapter(getContext(), scoreModelList);
                                myGrid.setAdapter(statGridAdapter);
                            }else {
                                Toast.makeText(getContext(), "No Data!", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        return view;
    }

    private void _var() {
        _fullNameTB = view.findViewById(R.id.fullNameTB);
        _emailTB = view.findViewById(R.id.emailTB);
        myGrid = view.findViewById(R.id.score_grid_view);
    }
}