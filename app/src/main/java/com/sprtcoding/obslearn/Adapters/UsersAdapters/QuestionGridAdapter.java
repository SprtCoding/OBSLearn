package com.sprtcoding.obslearn.Adapters.UsersAdapters;

import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.ANSWERED;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.NOT_VISITED;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.REVIEW;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.UNANSWERED;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.UserMenu.UsersChildMenu.QuestionsActivity;

public class QuestionGridAdapter extends BaseAdapter {
    private int numOfQuestion;
    private Context context;

    public QuestionGridAdapter(int numOfQuestion, Context context) {
        this.numOfQuestion = numOfQuestion;
        this.context = context;
    }

    @Override
    public int getCount() {
        return numOfQuestion;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v;

        if(view == null) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.quest_grid_item, viewGroup, false);
        } else {
            v = view;
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof QuestionsActivity)
                    ((QuestionsActivity)context).goToQuestion(i);
            }
        });

        TextView quesTV = v.findViewById(R.id.ques_num);
        quesTV.setText(String.valueOf(i + 1));

        switch (DBQuery.g_questList.get(i).getStatus()) {
            case NOT_VISITED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(v.getContext(), R.color.grey)));
                break;
            case UNANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(v.getContext(), R.color.red)));
                break;
            case ANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(v.getContext(), R.color.green)));
                break;
            case REVIEW:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(v.getContext(), R.color.pink)));
                break;
            default:
                break;
        }

        return v;
    }
}
