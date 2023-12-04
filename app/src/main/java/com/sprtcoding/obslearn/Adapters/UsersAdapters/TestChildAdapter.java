package com.sprtcoding.obslearn.Adapters.UsersAdapters;

import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_catListModel;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_selected_cat_index;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.Model.TestCatModel;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.UserMenu.UsersChildMenu.StartTestActivity;

import java.util.List;

public class TestChildAdapter extends RecyclerView.Adapter<TestChildAdapter.ViewHolder>{
    Context context;
    List<TestCatModel> testCatList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Task<DocumentSnapshot> userDoc;

    public TestChildAdapter(Context context, List<TestCatModel> testCatList) {
        this.context = context;
        this.testCatList = testCatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.test_count.setText(testCatList.get(position).getTestID());

        if(user != null) {

            db.collection("EXAMINATION")
                    .document(g_catListModel.get(g_selected_cat_index).getCAT_ID())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if(documentSnapshot.exists()) {
                            String catName = documentSnapshot.getString("NAME");
                            userDoc = db.collection("USERS").document(user.getUid()).collection("MY_SCORE")
                                    .document(testCatList.get(position).getTestID() + "(" + catName + ")")
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if(task.isSuccessful()) {
                                            if(task.getResult().exists()) {
                                                int score = task.getResult().getLong("TOTAL_SCORE").intValue();
                                                holder.progress.setProgress(score);
                                                holder.progress_value.setText(score + " %");
                                            }
                                        }
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
        }

        holder.itemView.setOnClickListener(view -> {
            DBQuery.g_selected_test_index = position;
            Intent i = new Intent(context, StartTestActivity.class);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return testCatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView test_count, progress_value;
        CircularProgressBar progress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            test_count = itemView.findViewById(R.id.test_count);
            progress_value = itemView.findViewById(R.id.progress_value);
            progress = itemView.findViewById(R.id.my_progress);
        }
    }

    public void clearData() {
        if (testCatList != null) {
            testCatList.clear();
            notifyDataSetChanged();
        }
    }
}
