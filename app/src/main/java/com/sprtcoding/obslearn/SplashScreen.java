package com.sprtcoding.obslearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.UserBasicInfo.UserBasicInformation;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    FirebaseAuth mAuth;
    private FirebaseUser mUser;
    FirebaseDatabase mDB;
    FirebaseFirestore userDB;
    DocumentReference userDocRef;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        mDB = FirebaseDatabase.getInstance();
        userRef = mDB.getReference("UsersData");

        userDB = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if (mUser != null) {
            userDocRef = userDB.collection("USERS").document(mUser.getUid());
            userDocRef.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.contains("ACCOUNT_TYPE")) {
                        String type = document.getString("ACCOUNT_TYPE");
                        int age = document.getLong("AGE").intValue();
                        String gender = document.getString("GENDER");
                        String dob = document.getString("DATE_OF_BIRTH");
                        String section = document.getString("SECTION");

                        assert type != null;
                        if(type.equals("User")) {
                            if(age == 0 && Objects.equals(gender, "") && Objects.equals(dob, "") || Objects.equals(section, "")) {

                                DBQuery.loadCategories(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent gotoUserBasicInfo = new Intent(SplashScreen.this, UserBasicInformation.class);
                                        startActivity(gotoUserBasicInfo);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(SplashScreen.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else {
                                DBQuery.loadCategories(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent gotoUserDashboard = new Intent(SplashScreen.this, UserDashBoard.class);
                                        startActivity(gotoUserDashboard);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(SplashScreen.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else if (type.equals("Admin")) {
                            DBQuery.loadCategories(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    Intent gotoAdminDashboard = new Intent(SplashScreen.this, AdminDashboardPage.class);
                                    startActivity(gotoAdminDashboard);
                                    finish();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(SplashScreen.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });
        } else {
            Intent i = new Intent(SplashScreen.this, Overview.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}