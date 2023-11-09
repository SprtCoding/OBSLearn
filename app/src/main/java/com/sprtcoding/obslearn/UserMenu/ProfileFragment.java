package com.sprtcoding.obslearn.UserMenu;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sprtcoding.obslearn.AdminDashboardPage;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.LoginPage;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.SplashScreen;
import com.sprtcoding.obslearn.UserBasicInfo.UserBasicInformation;
import com.sprtcoding.obslearn.UserDashBoard;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import kotlin.jvm.Throws;

public class ProfileFragment extends Fragment {
    View view;
    private AutoCompleteTextView _genderCTV;
    private TextView _closeDialogBtn, _closeGenderDialogBtn, _closeDateDialogBtn, messageForAuth, closeEmailDialogBtn, user_name;
    private TextInputLayout _editDialogTIL;
    private TextInputEditText _editDialogET, currentEmailDialogET, passwordDialogET, newEmailDialogET;
    private MaterialButton _saveDialogBtn, _saveGenderDialogBtn, _dateBtn, _saveDateDialogBtn, reAuthenticateDialogBtn, updateDialogBtn;
    FirebaseAuth mAuth;
    FirebaseUser _user;
    FirebaseDatabase mDB;
    FirebaseFirestore userDB;
    DocumentReference userDocRef;
    private DatabaseReference userRef;
    private TextView _fullNameTB, _emailTB, _ageTB, _genderTB, _birthdateTB, _logoutBtn;
    private ProgressDialog loading;
    String userOldEmail;
    GoogleApiClient mGoogleApiClient;

    @SuppressLint({"MissingInflatedId", "ResourceAsColor", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        _var();

        loading = new ProgressDialog(getContext());

        loading.setMessage("Please wait...");

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        mDB = FirebaseDatabase.getInstance();
        userRef = mDB.getReference("UsersData");

        userDB = FirebaseFirestore.getInstance();

        _user = mAuth.getCurrentUser();

        if(_user != null) {
            userOldEmail = _user.getEmail();

            userDocRef = userDB.collection("USERS").document(_user.getUid());

            //realtime updates
            userDocRef.addSnapshotListener((value, error) -> {
                assert value != null;
                if(value.exists()) {
                    int age = Integer.parseInt(value.get("AGE").toString());
                    String gender = value.get("GENDER").toString();
                    String dob = value.get("DATE_OF_BIRTH").toString();
                    String name = value.get("NAME").toString();
                    String email = value.get("EMAIL_ID").toString();

                    user_name.setText(name);
                    _fullNameTB.setText(name);
                    _emailTB.setText(email);
                    _ageTB.setText(String.valueOf(age));
                    _genderTB.setText(gender);
                    _birthdateTB.setText(dob);
                }
            });

//            userRef.child(_user.getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists()) {
//                        String _name = snapshot.child("Fullname").getValue(String.class);
//                        String _email = snapshot.child("Email").getValue(String.class);
//                        String _age = snapshot.child("Age").getValue(String.class);
//                        String _gender = snapshot.child("Gender").getValue(String.class);
//                        String _birthdate = snapshot.child("DateOfBirth").getValue(String.class);
//
//                        _fullNameTB.setText(_name);
//                        _emailTB.setText(_email);
//                        _ageTB.setText(_age);
//                        _genderTB.setText(_gender);
//                        _birthdateTB.setText(_birthdate);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        //update email dialog
        View updateEmailDialog = LayoutInflater.from(getContext()).inflate(R.layout.update_email_dialog, null);
        AlertDialog.Builder updateEmailDialogBuilder = new AlertDialog.Builder(getContext());

        updateEmailDialogBuilder.setView(updateEmailDialog);

        closeEmailDialogBtn = updateEmailDialog.findViewById(R.id.closeEmailDialogBtn);
        currentEmailDialogET = updateEmailDialog.findViewById(R.id.currentEmailDialogET);
        passwordDialogET = updateEmailDialog.findViewById(R.id.passwordDialogET);
        reAuthenticateDialogBtn = updateEmailDialog.findViewById(R.id.reAuthenticateDialogBtn);
        messageForAuth = updateEmailDialog.findViewById(R.id.messageForAuth);
        newEmailDialogET = updateEmailDialog.findViewById(R.id.newEmailDialogET);
        updateDialogBtn = updateEmailDialog.findViewById(R.id.updateDialogBtn);

        //disabled update button because not authenticated yet
        updateDialogBtn.setEnabled(false);
        updateDialogBtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.grey));
        //disabled new email Edit Text because not authenticated yet
        newEmailDialogET.setEnabled(false);

        currentEmailDialogET.setText(userOldEmail);

        final AlertDialog updateEmailDialogs = updateEmailDialogBuilder.create();

        updateEmailDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateEmailDialogs.setCanceledOnTouchOutside(false);

        closeEmailDialogBtn.setOnClickListener(view -> {
            updateEmailDialogs.dismiss();
        });

        //end of update email dialog

        //edit dialog
        View editDialog = LayoutInflater.from(getContext()).inflate(R.layout.edit_profile_dialog, null);
        AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(getContext());

        editDialogBuilder.setView(editDialog);

        _closeDialogBtn = editDialog.findViewById(R.id.closeDialogBtn);
        _editDialogTIL = editDialog.findViewById(R.id.editDialogTIL);
        _editDialogET = editDialog.findViewById(R.id.editDialogET);
        _saveDialogBtn = editDialog.findViewById(R.id.saveDialogBtn);

        final AlertDialog editNewDialog = editDialogBuilder.create();

        editNewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editNewDialog.setCanceledOnTouchOutside(false);

        _closeDialogBtn.setOnClickListener(view -> {
            editNewDialog.dismiss();
        });

        //end of edit dialog

        //edit gender dialog
        View editGenderDialog = LayoutInflater.from(getContext()).inflate(R.layout.edit_gender_dialog, null);
        AlertDialog.Builder editGenderDialogBuilder = new AlertDialog.Builder(getContext());

        editGenderDialogBuilder.setView(editGenderDialog);

        _closeGenderDialogBtn = editGenderDialog.findViewById(R.id.closeGenderDialogBtn);
        _genderCTV = editGenderDialog.findViewById(R.id.genderCTV);
        _saveGenderDialogBtn = editGenderDialog.findViewById(R.id.saveGenderDialogBtn);

        final AlertDialog editGenderNewDialog = editGenderDialogBuilder.create();

        editGenderNewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editGenderNewDialog.setCanceledOnTouchOutside(false);

        _closeGenderDialogBtn.setOnClickListener(view -> {
            editGenderNewDialog.dismiss();
        });

        //gender dropdown
        String[] gender = new String[] {
                "Male",
                "Female"
        };

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.gender_dropdown_item,
                gender
        );

        _genderCTV.setAdapter(genderAdapter);//end of gender dropdown

        //end of edit gender dialog

        //edit date dialog
        View editDateDialog = LayoutInflater.from(getContext()).inflate(R.layout.edit_bdate_dialog, null);
        AlertDialog.Builder editDateDialogBuilder = new AlertDialog.Builder(getContext());

        editDateDialogBuilder.setView(editDateDialog);

        _closeDateDialogBtn = editDateDialog.findViewById(R.id.closeDateDialogBtn);
        _dateBtn = editDateDialog.findViewById(R.id.dateBtn);
        _saveDateDialogBtn = editDateDialog.findViewById(R.id.saveDateDialogBtn);

        final AlertDialog editNewDateDialog = editDateDialogBuilder.create();

        editNewDateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editNewDateDialog.setCanceledOnTouchOutside(false);

        //date picker
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        long today = MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder datePickerBuilder = MaterialDatePicker.Builder.datePicker();
        datePickerBuilder.setTitleText("Select Date of Birth");
        datePickerBuilder.setSelection(today);
        final MaterialDatePicker materialDatePicker = datePickerBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> _dateBtn.setText(materialDatePicker.getHeaderText()));

        _dateBtn.setOnClickListener(view -> {
            materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");
        });

        _closeDateDialogBtn.setOnClickListener(view -> {
            editNewDateDialog.dismiss();
        });

        //end of edit date dialog

        _fullNameTB.setOnClickListener(view -> {
            editNewDialog.show();
            _editDialogTIL.setHint("Fullname");
            _saveDialogBtn.setOnClickListener(view1 -> {
                loading.show();
                if(!TextUtils.isEmpty(_editDialogET.getText().toString())) {
                    String name = _editDialogET.getText().toString();
                    DBQuery.updateName(name, new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            loading.dismiss();
                            editNewDialog.dismiss();
                            Toast.makeText(getContext(), "Full Name updated successfully.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            try {
                                throw e;
                            } catch (Exception ex) {
                                loading.dismiss();
                                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    loading.dismiss();
                    Toast.makeText(getContext(), "Full Name is empty! Try Again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        _emailTB.setOnClickListener(view -> {
            updateEmailDialogs.show();
            reAuthenticateDialogBtn.setOnClickListener(view1 -> {
                if(!TextUtils.isEmpty(passwordDialogET.getText().toString())) {
                    loading.show();

                    AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail, passwordDialogET.getText().toString());

                    _user.reauthenticate(credential).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            loading.dismiss();
                            Toast.makeText(getContext(), "Password has been verified. You can update email now.", Toast.LENGTH_SHORT).show();

                            messageForAuth.setText("You are authenticated. You can update email now.");
                            messageForAuth.setTextColor(Color.GREEN);

                            //disabled button
                            reAuthenticateDialogBtn.setEnabled(false);
                            passwordDialogET.setEnabled(false);
                            //enabled update
                            newEmailDialogET.setEnabled(true);
                            updateDialogBtn.setEnabled(true);

                            updateDialogBtn.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.secondColor));

                            updateDialogBtn.setOnClickListener(view2 -> {
                                loading.show();
                                if(TextUtils.isEmpty(newEmailDialogET.getText().toString())) {
                                    loading.dismiss();
                                    Toast.makeText(getContext(), "New email is required.", Toast.LENGTH_SHORT).show();
                                    newEmailDialogET.setError("Please enter new email");
                                    newEmailDialogET.requestFocus();
                                }else if(!Patterns.EMAIL_ADDRESS.matcher(newEmailDialogET.getText().toString()).matches()) {
                                    loading.dismiss();
                                    Toast.makeText(getContext(), "Please enter valid email.", Toast.LENGTH_SHORT).show();
                                    newEmailDialogET.setError("Please provide valid email");
                                    newEmailDialogET.requestFocus();
                                }else if(userOldEmail.matches(newEmailDialogET.getText().toString())) {
                                    loading.dismiss();
                                    Toast.makeText(getContext(), "New email cannot be same as old email.", Toast.LENGTH_SHORT).show();
                                    newEmailDialogET.setError("Please enter new email");
                                    newEmailDialogET.requestFocus();
                                }else {
                                    String email = newEmailDialogET.getText().toString();

                                    _user.updateEmail(email).addOnCompleteListener(task1 -> {
                                        if(task1.isComplete()) {
                                            DBQuery.updateEmail(email, new MyCompleteListener() {
                                                @Override
                                                public void onSuccess() {
                                                    loading.dismiss();
                                                    updateEmailDialogs.dismiss();
                                                    Toast.makeText(getContext(), "Email updated successfully.", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Exception e) {
                                                    loading.dismiss();
                                                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(e -> {
                                        loading.dismiss();
                                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                loading.dismiss();
                                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(e -> {
                        loading.dismiss();
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }else {
                    loading.dismiss();
                    Toast.makeText(getContext(), "Password is need t continue.", Toast.LENGTH_SHORT).show();
                    passwordDialogET.setError("Please enter your password to continue.");
                    passwordDialogET.requestFocus();
                }
            });
        });

        _ageTB.setOnClickListener(view -> {
            editNewDialog.show();
            _editDialogTIL.setHint("Age");
            _saveDialogBtn.setOnClickListener(view1 -> {
                loading.show();
                if(!TextUtils.isEmpty(_editDialogET.getText().toString())) {

                    DBQuery.updateAge(Integer.parseInt(_editDialogET.getText().toString()), new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            loading.dismiss();
                            editNewDialog.dismiss();
                            Toast.makeText(getContext(), "Age updated successfully.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            loading.dismiss();
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    loading.dismiss();
                    Toast.makeText(getContext(), "Age is empty! Try Again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        _genderTB.setOnClickListener(view -> {
            editGenderNewDialog.show();
            _saveGenderDialogBtn.setOnClickListener(view1 -> {
                loading.show();
                if(!TextUtils.isEmpty(_genderCTV.getText().toString())) {

                    DBQuery.updateGender(_genderCTV.getText().toString(), new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            loading.dismiss();
                            editGenderNewDialog.dismiss();
                            Toast.makeText(getContext(), "Gender updated successfully.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            loading.dismiss();
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    loading.dismiss();
                    Toast.makeText(getContext(), "Gender is empty! Try Again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        _birthdateTB.setOnClickListener(view -> {
            editNewDateDialog.show();
            _saveDateDialogBtn.setOnClickListener(view1 -> {
                loading.show();
                if(!_dateBtn.getText().toString().equals("Date of Birth")) {
                    DBQuery.updateDateOfBirth(_dateBtn.getText().toString(), new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            loading.dismiss();
                            editNewDateDialog.dismiss();
                            Toast.makeText(getContext(), "Date of Birth updated successfully.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            loading.dismiss();
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    loading.dismiss();
                    Toast.makeText(getContext(), "Date of Birth is empty! Try Again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Initialize GoogleSignInOptions
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with the options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        _logoutBtn.setOnClickListener(view -> {
            loading.show();
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        status -> {
                            mAuth.signOut();
                            Intent gotoLogin = new Intent(getContext(), LoginPage.class);
                            startActivity(gotoLogin);
                            loading.dismiss();
                        });
            } else {
                // Handle the case when the GoogleApiClient is not yet connected
                // You may want to display a message or take other appropriate action
                Toast.makeText(getContext(), "Something went wrong! Try Again.", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });

        return view;
    }

    private void _var() {
        user_name = view.findViewById(R.id.user_name);
        _fullNameTB = view.findViewById(R.id.fullnameTB);
        _emailTB = view.findViewById(R.id.emailTB);
        _ageTB = view.findViewById(R.id.ageTB);
        _genderTB = view.findViewById(R.id.genderTB);
        _birthdateTB = view.findViewById(R.id.birthdateTB);
        _logoutBtn = view.findViewById(R.id.logoutBtn);
    }

    // Connect the GoogleApiClient in onResume or another appropriate lifecycle method
    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    // Disconnect the GoogleApiClient in onPause or another appropriate lifecycle method
    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}