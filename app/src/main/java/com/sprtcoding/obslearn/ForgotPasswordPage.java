package com.sprtcoding.obslearn;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

public class ForgotPasswordPage extends AppCompatActivity {
    private MaterialButton _resetBtn, _backBtn;
    private TextInputEditText _emailET;
    private LoadingDialog loadingDialog;
    FirebaseAuth mAuth;
    String email;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_page);
        _var();
        loadingDialog = new LoadingDialog(this);

        mAuth = FirebaseAuth.getInstance();

        _resetBtn.setOnClickListener(view -> {
            loadingDialog.show();
            email = _emailET.getText().toString().trim();
            if(!TextUtils.isEmpty(email)) {
                ResetPassword();
            } else {
                _emailET.setError("Email field can't empty!");
                loadingDialog.dismiss();
            }
        });

        _backBtn.setOnClickListener(view -> {
            loadingDialog.show();
            Handler handler = new Handler();
            Runnable runnable = () -> {
                loadingDialog.dismiss();
                finish();
            };
            handler.postDelayed(runnable, 1500);
        });
    }

    @SuppressLint("SetTextI18n")
    private void ResetPassword() {
        _resetBtn.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> {
            loadingDialog.dismiss();
            Toast.makeText(this, "Reset Password link has been sent to your registered Email.", Toast.LENGTH_LONG).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error: - " + e.getMessage(), Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
            _resetBtn.setVisibility(View.VISIBLE);
        });
    }

    private void _var() {
        _resetBtn = findViewById(R.id.resetBtn);
        _backBtn = findViewById(R.id.backBtn);
        _emailET = findViewById(R.id.emailET);
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}