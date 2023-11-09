package com.sprtcoding.obslearn;

import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.UserBasicInfo.UserBasicInformation;

import java.util.Map;

public class TwitterLoginPage extends LoginPage {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        // Localize to French.
        provider.addCustomParameter("lang", "en");

        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            authResult -> {
                                String name = authResult.getUser().getDisplayName();
                                String email = authResult.getUser().getEmail();

                                Map<String, Object> userData = new ArrayMap<>();
                                userData.put("USER_ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                userData.put("EMAIL_ID", email);
                                userData.put("NAME", name);
                                userData.put("ACCOUNT_TYPE", "User");

                                DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                userDoc.update(userData).addOnSuccessListener(unused -> {
                                    Toast.makeText(TwitterLoginPage.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                                    gotoUserDashboard();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(TwitterLoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            })
                    .addOnFailureListener(
                            e -> Toast.makeText(TwitterLoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            firebaseAuth
                    .startActivityForSignInWithProvider(/* activity= */ this, provider.build())
                    .addOnSuccessListener(
                            authResult -> {
                                String name = authResult.getUser().getDisplayName();
                                String email = authResult.getUser().getEmail();

                                Map<String, Object> userData = new ArrayMap<>();
                                userData.put("USER_ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                userData.put("EMAIL_ID", email);
                                userData.put("NAME", name);
                                userData.put("ACCOUNT_TYPE", "User");

                                DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                userDoc.update(userData).addOnSuccessListener(unused -> {
                                    Toast.makeText(TwitterLoginPage.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                                    gotoUserDashboard();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(TwitterLoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            })
                    .addOnFailureListener(
                            e -> {
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
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
                                        Intent gotoUserBasicInfo = new Intent(TwitterLoginPage.this, UserBasicInformation.class);
                                        startActivity(gotoUserBasicInfo);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(TwitterLoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                DBQuery.loadCategories(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent gotoUserDashboard = new Intent(TwitterLoginPage.this, UserDashBoard.class);
                                        startActivity(gotoUserDashboard);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(TwitterLoginPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
}