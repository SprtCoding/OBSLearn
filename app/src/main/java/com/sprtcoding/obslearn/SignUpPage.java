package com.sprtcoding.obslearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

import java.util.HashMap;

public class SignUpPage extends AppCompatActivity {
    private TextView _loginBtn;
    private LoadingDialog _loading;
    private MaterialButton _signUpBtn;
    private TextInputEditText _firstNameET, _lastNameET, _emailET, _passET;
    FirebaseAuth mAuth;
    private FirebaseUser mUser;
    FirebaseDatabase mDB;
    private DatabaseReference userRef;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        _var();
        _loading = new LoadingDialog(this);

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDB = FirebaseDatabase.getInstance();
        userRef = mDB.getReference("UsersData");

        _signUpBtn.setOnClickListener(view -> {
            _loading.show();
            String fName = _firstNameET.getText().toString();
            String lName = _lastNameET.getText().toString();
            String email = _emailET.getText().toString();
            String pass = _passET.getText().toString();
            String accountType = "User";

            if(TextUtils.isEmpty(fName)) {
                _loading.dismiss();
                _firstNameET.setError("First Name cannot be empty!");
            }else if(TextUtils.isEmpty(lName)) {
                _loading.dismiss();
                _lastNameET.setError("Last Name cannot be empty!");
            }else if(TextUtils.isEmpty(email)) {
                _loading.dismiss();
                _emailET.setError("Email cannot be empty!");
            }else if(TextUtils.isEmpty(pass)) {
                _loading.dismiss();
                _passET.setError("Password cannot be empty!");
            }else {
                CreateEmailPassword(fName, lName, email, pass, accountType);
            }

        });

        _loginBtn.setOnClickListener(view -> {
            _loading.show();
            Handler handler = new Handler();
            Runnable runnable = () -> {
                _loading.dismiss();
                finish();
            };
            handler.postDelayed(runnable, 2000);
        });
    }

    private void CreateEmailPassword(String fName, String lName, String email, String pass, String accountType) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(authResult -> {

            DBQuery.createUserData(email, fName + " " + lName, FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    accountType, "", 0, "", new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(SignUpPage.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                            _loading.dismiss();
                            Intent gotoLoginPage = new Intent(SignUpPage.this, LoginPage.class);
                            startActivity(gotoLoginPage);
                            finish();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            _loading.dismiss();
                            Toast.makeText(SignUpPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

//            HashMap<String, Object> map = new HashMap<>();
//            map.put("Fullname", fName + " " + lName);
//            map.put("Email", email);
//            map.put("AccountType", accountType);
//            map.put("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map).addOnCompleteListener(task -> {
//                if(task.isSuccessful()) {
//                    _loading.dismiss();
//                    Intent gotoLoginPage = new Intent(SignUpPage.this, LoginPage.class);
//                    startActivity(gotoLoginPage);
//                    finish();
//                }
//            });

            DBQuery.addUserCount();

        }).addOnFailureListener(e -> {
            _loading.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void _var() {
        _loginBtn = findViewById(R.id.loginBtn);
        _signUpBtn = findViewById(R.id.signUpBtn);
        _firstNameET = findViewById(R.id.firstNameET);
        _lastNameET = findViewById(R.id.lastNameET);
        _emailET = findViewById(R.id.emailET);
        _passET = findViewById(R.id.passET);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}