package com.sprtcoding.obslearn.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sprtcoding.obslearn.AdminMenu.Exams.UserStat;
import com.sprtcoding.obslearn.Model.ScoreModel;
import com.sprtcoding.obslearn.Model.UserModel;
import com.sprtcoding.obslearn.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserGridAdapter extends BaseAdapter implements Filterable {
    Context context;
    List<UserModel> userList;
    private final List<UserModel> filteredList;
    LayoutInflater inflater;

    public UserGridAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
        this.filteredList = new ArrayList<>(userList);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null) {
            view = inflater.inflate(R.layout.student_list_grid, null);
        }

        UserModel user = filteredList.get(position);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userColRef = db.collection("USERS");

        ImageView removeUser = view.findViewById(R.id.removeUser);
        TextView stud_name = view.findViewById(R.id.stud_name);
        TextView stud_section = view.findViewById(R.id.stud_section);
        TextView stud_email = view.findViewById(R.id.stud_email);
        CircleImageView userPic = view.findViewById(R.id.userPic);

        String userID = user.getUSER_ID();

        if(user.getGENDER().equals("Male")) {
            Picasso.get().load(R.drawable.student_boy).into(userPic);
        }else if(user.getGENDER().equals("Female")) {
            Picasso.get().load(R.drawable.student_new).into(userPic);
        }

        if(user.getSECTION().equals("")) {
            stud_section.setText("No section yet");
            stud_section.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(207, 53, 59)));
        }
        else {
            stud_section.setText("Section " + user.getSECTION());
            stud_section.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(46, 139, 87)));
        }

        ProgressDialog loading = new ProgressDialog(context);
        loading.setMessage("Please wait...");

        removeUser.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Remove User")
                    .setMessage("Are you sure you want to remove " + user.getNAME() + "?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        loading.show();
                        userColRef.document(userID)
                                .delete()
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful()) {
                                        loading.dismiss();
                                        Toast.makeText(context, "Student removed successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    loading.dismiss();
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton(android.R.string.no, null) // No listener needed, just dismisses the dialog
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        view.setOnClickListener(view1 -> {
            Intent i = new Intent(context, UserStat.class);
            i.putExtra("name", user.getNAME());
            i.putExtra("email", user.getEMAIL_ID());
            i.putExtra("gender", user.getGENDER());
            i.putExtra("uid", user.getUSER_ID());
            context.startActivity(i);
        });

        stud_name.setText(user.getNAME());
        stud_email.setText(user.getEMAIL_ID());

        return view;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                filteredList.clear();

                if (filterPattern.isEmpty()) {
                    filteredList.addAll(userList);
                } else {
                    for (UserModel user : userList) {
                        if (user.getNAME().toLowerCase().contains(filterPattern)
                                || user.getEMAIL_ID().toLowerCase().contains(filterPattern)) {
                            filteredList.add(user);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetChanged();
            }
        };
    }
    public Filter getFilterSection() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                filteredList.clear();

                if (filterPattern.isEmpty()) {
                    filteredList.addAll(userList);
                } else {
                    for (UserModel user : userList) {
                        if (user.getSECTION().toLowerCase().contains(filterPattern)) {
                            filteredList.add(user);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetChanged();
            }
        };
    }
}
