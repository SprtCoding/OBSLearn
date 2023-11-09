package com.sprtcoding.obslearn.Adapters.UsersAdapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.Model.TestModel;
import com.sprtcoding.obslearn.R;

public class TopicsAdapter extends FirestoreRecyclerAdapter<TestModel, TopicsAdapter.TopicsViewHolder> {
    View v;
    AlertDialog.Builder closeAlertBuilder;
    LoadingDialog loadingDialog;
    CollectionReference examColRef;
    FirebaseFirestore db;

    public TopicsAdapter(@NonNull FirestoreRecyclerOptions<TestModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TopicsViewHolder holder, int position, @NonNull TestModel model) {
        holder.topic_name.setText(model.getTOPICS());
        holder.q_txt.setText(model.getQUESTION());
        db = FirebaseFirestore.getInstance();
        examColRef = db.collection("EXAMINATION");

        examColRef.whereArrayContains("TOPICS", model.getTOPICS()).addSnapshotListener((value, error) -> {
            if (error == null && value != null) {
                int topicCount = value.size() + position + 1;
                // Now you have the count of documents with the specified topic
                // You can use topicCount as needed
                holder.q_count.setText(String.valueOf(topicCount));
            } else {
                // Handle the error condition
                Toast.makeText(v.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public TopicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.v = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topics_layout, parent, false);
        return new TopicsViewHolder(view);
    }

    public static class TopicsViewHolder extends RecyclerView.ViewHolder {
        TextView topic_name,q_count,q_txt;

        public TopicsViewHolder(@NonNull View itemView) {
            super(itemView);
            topic_name = itemView.findViewById(R.id.topic_name);
            q_count = itemView.findViewById(R.id.q_count);
            q_txt = itemView.findViewById(R.id.q_txt);
        }
    }
}
