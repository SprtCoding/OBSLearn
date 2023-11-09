package com.sprtcoding.obslearn.Adapters.UsersAdapters;

import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.ANSWERED;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.REVIEW;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.UNANSWERED;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_questList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.Model.QuestionModel;
import com.sprtcoding.obslearn.R;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    private Context context;
    private List<QuestionModel> questionList;
    TextView _previousSelectedBtn;

    public QuestionsAdapter(Context context, List<QuestionModel> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.questions_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionModel question = questionList.get(position);

        holder._questions.setText(question.getQuestion());
        holder._optionA.setText(question.getOptionA());
        holder._optionB.setText(question.getOptionB());
        holder._optionC.setText(question.getOptionC());
        holder._optionD.setText(question.getOptionD());

        setListenerForOption(holder, position);
    }

    private void setListenerForOption(ViewHolder holder, int position) {
        _previousSelectedBtn = null;

        setOption(holder._optionA, 1, position);
        setOption(holder._optionB, 2, position);
        setOption(holder._optionC, 3, position);
        setOption(holder._optionD, 4, position);

        holder._optionA.setOnClickListener(view -> {
            selectOption(holder._optionA, 1, position);
        });
        holder._optionB.setOnClickListener(view -> {
            selectOption(holder._optionB, 2, position);
        });
        holder._optionC.setOnClickListener(view -> {
            selectOption(holder._optionC, 3, position);
        });
        holder._optionD.setOnClickListener(view -> {
            selectOption(holder._optionD, 4, position);
        });
    }

    private void setOption(TextView btn, int option_num, int questID) {
        if(DBQuery.g_questList.get(questID).getSelectedAnswer() == option_num) {
            btn.setBackgroundResource(R.drawable.selected_bg);
        }else {
            btn.setBackgroundResource(R.drawable.unselected_bg);
        }
    }

    private void selectOption(TextView btn, int option_num, int questionID) {
        if(_previousSelectedBtn == null) {
            btn.setBackgroundResource(R.drawable.selected_bg);
            DBQuery.g_questList.get(questionID).setSelectedAnswer(option_num);

            changeStatus(questionID, ANSWERED);

            _previousSelectedBtn = btn;
        }else {
            if(_previousSelectedBtn.getId() == btn.getId()) {
                btn.setBackgroundResource(R.drawable.unselected_bg);
                DBQuery.g_questList.get(questionID).setSelectedAnswer(-1);

                changeStatus(questionID, UNANSWERED);

                _previousSelectedBtn = null;
            }else {
                _previousSelectedBtn.setBackgroundResource(R.drawable.unselected_bg);
                btn.setBackgroundResource(R.drawable.selected_bg);

                DBQuery.g_questList.get(questionID).setSelectedAnswer(option_num);

                changeStatus(questionID, ANSWERED);

                _previousSelectedBtn = btn;
            }
        }
    }

    private void changeStatus(int questionID, int answered) {
        if(g_questList.get(questionID).getStatus() != REVIEW) {
            g_questList.get(questionID).setStatus(answered);
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView _questions, _optionA, _optionB, _optionC, _optionD;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _questions = itemView.findViewById(R.id.questions);
            _optionA = itemView.findViewById(R.id.optionA);
            _optionB = itemView.findViewById(R.id.optionB);
            _optionC = itemView.findViewById(R.id.optionC);
            _optionD = itemView.findViewById(R.id.optionD);
        }
    }
}
