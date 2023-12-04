package com.sprtcoding.obslearn;

import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.UserBasicInfo.UserBasicInformation;
import com.sprtcoding.obslearn.Utility.NetworkChangeListener;

import java.util.Map;

public class LoginPage extends AppCompatActivity {
    private TextView _signUpBtn, _forgotPasswordBtn;
    private TextInputEditText _emailET, _passET;
    private MaterialButton _loginBtn, _googleBtn, _twitterBtn;
    private LoadingDialog _loading;
    private String email, password;
    GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    FirebaseAuth mAuth;
    private FirebaseUser mUser;
    FirebaseDatabase mDB;
    private DatabaseReference userRef;
    FirebaseFirestore userDB;
    DocumentReference userDocRef;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        _var();
        _loading = new LoadingDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        g_firestore = FirebaseFirestore.getInstance();

        mDB = FirebaseDatabase.getInstance();
        userRef = mDB.getReference("UsersData");

        userDB = FirebaseFirestore.getInstance();

        _loginBtn.setOnClickListener(view -> {
            _loading.show();
            Handler handler = new Handler();
            Runnable runnable = () -> {
                email = _emailET.getText().toString().trim();
                password = _passET.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    _loading.dismiss();
                    _emailET.setError("Email field can't empty!");
                } else if (TextUtils.isEmpty(password)) {
                    _loading.dismiss();
                    _passET.setError("Password field can't empty!");
                } else {
                    LoginEmailPassword(email, password);
                }
            };
            handler.postDelayed(runnable, 2000);
        });

        _signUpBtn.setOnClickListener(view -> {
            _loading.show();
            Handler handler = new Handler();
            Runnable runnable = () -> {
                _loading.dismiss();
                Intent gotoSignUpPage = new Intent(this, SignUpPage.class);
                startActivity(gotoSignUpPage);
            };
            handler.postDelayed(runnable, 2000);
        });

        _forgotPasswordBtn.setOnClickListener(view -> {
            _loading.show();
            Handler handler = new Handler();
            Runnable runnable = () -> {
                _loading.dismiss();
                Intent gotoSignUpPage = new Intent(this, ForgotPasswordPage.class);
                startActivity(gotoSignUpPage);
            };
            handler.postDelayed(runnable, 2000);
        });

//        _twitterBtn.setOnClickListener(view -> {
//            Intent gotoTwitter = new Intent(this, TwitterLoginPage.class);
//            startActivity(gotoTwitter);
//        });

        //google sign in

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        _googleBtn.setOnClickListener(view -> {
            _loading.show();
            SignInGoogle();
        });
    }

    private void LoginEmailPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            userDocRef = userDB.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userDocRef.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.contains("ACCOUNT_TYPE")) {
                        String type = document.get("ACCOUNT_TYPE").toString();
                        int age = Integer.parseInt(document.get("AGE").toString());
                        String gender = document.get("GENDER").toString();
                        String dob = document.get("DATE_OF_BIRTH").toString();

                        if(type.equals("User")) {
                            if(age == 0 && gender.equals("") && dob.equals("")) {
                                DBQuery.loadCategories(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent gotoUserBasicInfo = new Intent(LoginPage.this, UserBasicInformation.class);
                                        startActivity(gotoUserBasicInfo);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(LoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                DBQuery.loadCategories(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent gotoUserDashboard = new Intent(LoginPage.this, UserDashBoard.class);
                                        startActivity(gotoUserDashboard);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(LoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else if (type.equals("Admin")) {
                            DBQuery.loadCategories(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    Intent gotoAdminDashboard = new Intent(LoginPage.this, AdminDashboardPage.class);
                                    startActivity(gotoAdminDashboard);
                                    finish();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(LoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });

//            userRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists()) {
//                        if (snapshot.hasChild("AccountType")) {
//                            String accountType = snapshot.child("AccountType").getValue(String.class);
//                            if (accountType.equals("User")) {
//                                if(!snapshot.hasChild("Age") && !snapshot.hasChild("Gender") && !snapshot.hasChild("DateOfBirth")) {
//                                    Intent gotoUserBasicInfo = new Intent(LoginPage.this, UserBasicInformation.class);
//                                    startActivity(gotoUserBasicInfo);
//                                    finish();
//                                }else {
//                                    Intent gotoUserDashboard = new Intent(LoginPage.this, UserDashBoard.class);
//                                    startActivity(gotoUserDashboard);
//                                    finish();
//                                }
//                            } else if (accountType.equals("Admin")) {
//                                Intent gotoAdminDashboard = new Intent(LoginPage.this, AdminDashboardPage.class);
//                                startActivity(gotoAdminDashboard);
//                                finish();
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    _loading.dismiss();
//                    Toast.makeText(LoginPage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }).addOnFailureListener(e -> {
            _loading.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void SignInGoogle() {
        Intent googleIntent = gsc.getSignInIntent();
        startActivityForResult(googleIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        userDocRef = userDB.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                                            if(documentSnapshot.exists()) {
                                                if(documentSnapshot.contains("ACCOUNT_TYPE")) {
                                                    String type = documentSnapshot.getString("ACCOUNT_TYPE");
                                                    if(type.equals("User")) {
                                                        if(documentSnapshot.contains("GENDER") && documentSnapshot.contains("AGE") && documentSnapshot.contains("DATE_OF_BIRTH")) {
                                                            String gender = documentSnapshot.getString("GENDER");
                                                            int age = documentSnapshot.getLong("AGE").intValue();
                                                            String bday = documentSnapshot.getString("DATE_OF_BIRTH");
                                                            if(gender.equals("") && age == 0 && bday.equals("")) {
                                                                DBQuery.loadCategories(new MyCompleteListener() {
                                                                    @Override
                                                                    public void onSuccess() {
                                                                        Intent gotoUserBasicInfo = new Intent(LoginPage.this, UserBasicInformation.class);
                                                                        startActivity(gotoUserBasicInfo);
                                                                        finish();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Exception e) {
                                                                        Toast.makeText(LoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            } else {
                                                                DBQuery.loadCategories(new MyCompleteListener() {
                                                                    @Override
                                                                    public void onSuccess() {
                                                                        Intent gotoUserDashboard = new Intent(LoginPage.this, UserDashBoard.class);
                                                                        startActivity(gotoUserDashboard);
                                                                        finish();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Exception e) {
                                                                        Toast.makeText(LoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                }
                                            }else {
                                                DBQuery.setUserData(account.getEmail(), account.getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                                        "User", "", 0, "", new MyCompleteListener() {
                                                            @Override
                                                            public void onSuccess() {
                                                                _loading.dismiss();
                                                                Toast.makeText(LoginPage.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                                                                Intent gotoUserBasicInfo = new Intent(LoginPage.this, UserBasicInformation.class);
                                                                startActivity(gotoUserBasicInfo);
                                                                finish();
                                                            }

                                                            @Override
                                                            public void onFailure(Exception e) {
                                                                _loading.dismiss();
                                                                Toast.makeText(LoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }).addOnFailureListener(e -> {
                                            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                                    } else {
                                        Toast.makeText(this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        _loading.dismiss();
                                    }
                                });
            } catch (ApiException e) {
                _loading.dismiss();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void gotoUserDashboard() {
        userDocRef = userDB.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()) {
                if(documentSnapshot.contains("ACCOUNT_TYPE")) {
                    String type = documentSnapshot.getString("ACCOUNT_TYPE");
                    if(type.equals("User")) {
                        if(documentSnapshot.contains("GENDER") && documentSnapshot.contains("AGE") && documentSnapshot.contains("DATE_OF_BIRTH")) {
                            String gender = documentSnapshot.getString("GENDER");
                            int age = documentSnapshot.getLong("AGE").intValue();
                            String bday = documentSnapshot.getString("DATE_OF_BIRTH");
                            if(gender.equals("") && age == 0 && bday.equals("")) {
                                DBQuery.loadCategories(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent gotoUserBasicInfo = new Intent(LoginPage.this, UserBasicInformation.class);
                                        startActivity(gotoUserBasicInfo);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(LoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                DBQuery.loadCategories(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent gotoUserDashboard = new Intent(LoginPage.this, UserDashBoard.class);
                                        startActivity(gotoUserDashboard);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(LoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void _var() {
        _signUpBtn = findViewById(R.id.signUpBtn);
        _emailET = findViewById(R.id.emailET);
        _passET = findViewById(R.id.passET);
        _loginBtn = findViewById(R.id.loginBtn);
        _googleBtn = findViewById(R.id.googleLoginBtn);
        //_twitterBtn = findViewById(R.id.twitterLoginBtn);
        _forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}