package com.sprtcoding.obslearn.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sprtcoding.obslearn.AdminMenu.Exams.AddNewQuestions;
import com.sprtcoding.obslearn.AdminMenu.Exams.AdminExamCat;
import com.sprtcoding.obslearn.AdminMenu.Exams.QuestionsList;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.Model.QuestionModel;
import com.sprtcoding.obslearn.R;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder>{
    Context context;
    List<QuestionModel> questionModels;
    AlertDialog.Builder deleteAlertBuilder;
    ProgressDialog loading;

    public QuestionsAdapter(Context context, List<QuestionModel> questionModels) {
        this.context = context;
        this.questionModels = questionModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list, parent, false);
        return new QuestionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionModel questions = questionModels.get(position);

        DBQuery.g_firestore = FirebaseFirestore.getInstance();
        deleteAlertBuilder = new AlertDialog.Builder(context);
        loading = new ProgressDialog(context);

        loading.setTitle("Delete");
        loading.setMessage("Please wait...");

        holder.question.setText(questions.getQuestion());

        holder.edit.setOnClickListener(view -> {
            Intent i = new Intent(context, AddNewQuestions.class);
            i.putExtra("isUpdate", true);
            i.putExtra("QuestionID", questions.getQuestion_ID());
            i.putExtra("Question", questions.getQuestion());
            i.putExtra("Option1", questions.getOptionA());
            i.putExtra("Option2", questions.getOptionB());
            i.putExtra("Option3", questions.getOptionC());
            i.putExtra("Option4", questions.getOptionD());
            i.putExtra("ANSWER", questions.getCorrectAnswer());
            i.putExtra("TEST_ID", questions.getTest_ID());
            context.startActivity(i);
        });

        holder.remove.setOnClickListener(view -> {
            deleteAlertBuilder.setTitle("Delete")
                    .setMessage("Are you sure want to delete this question?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        loading.show();
                        Handler handler = new Handler();
                        Runnable runnable = () -> {
                            DBQuery.removeQuestions(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(context, "Question removed.", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            }, questions.getQuestion_ID());
                        };
                        handler.postDelayed(runnable, 3000);
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                        loading.dismiss();
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return questionModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        TextView remove, edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            remove = itemView.findViewById(R.id.remove);
            edit = itemView.findViewById(R.id.edit_btn);
        }
    }
}
