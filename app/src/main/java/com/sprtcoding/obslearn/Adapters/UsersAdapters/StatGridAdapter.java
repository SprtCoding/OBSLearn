package com.sprtcoding.obslearn.Adapters.UsersAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sprtcoding.obslearn.AdminMenu.Exams.StudentChart;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.Model.CatListModel;
import com.sprtcoding.obslearn.Model.ScoreModel;
import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.UserMenu.UsersChildMenu.TestChildCategories;

import java.util.List;

public class StatGridAdapter extends BaseAdapter {
    Context context;
    List<ScoreModel> scoreModelList;
    LayoutInflater inflater;

    public StatGridAdapter(Context context, List<ScoreModel> scoreModelList) {
        this.context = context;
        this.scoreModelList = scoreModelList;
    }

    @Override
    public int getCount() {
        return scoreModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return scoreModelList.size();
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null) {
            view = inflater.inflate(R.layout.stat_grid_items, null);
        }

        ScoreModel scoreModel = scoreModelList.get(position);

        TextView cat_name = view.findViewById(R.id.cat_name);
        TextView progress_value = view.findViewById(R.id.progress_value);
        TextView scoreNum = view.findViewById(R.id.score);
        CircularProgressBar my_progress = view.findViewById(R.id.my_progress);

        cat_name.setText(scoreModel.getID());
        progress_value.setText(scoreModel.getTOTAL_SCORE() + "%");
        my_progress.setProgressMax(100f);
        my_progress.setProgress(scoreModel.getTOTAL_SCORE());

        scoreNum.setText(scoreModel.getScoreNum());

        view.setOnClickListener(v -> {
            Intent i = new Intent(context, StudentChart.class);
            i.putExtra("id", scoreModel.getID());
            i.putExtra("total_score", scoreModel.getTOTAL_SCORE());
            i.putExtra("total_score_num", scoreModel.getScoreNum());
            context.startActivity(i);
        });

        return view;
    }
}
