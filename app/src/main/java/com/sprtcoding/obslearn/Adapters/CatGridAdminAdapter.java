package com.sprtcoding.obslearn.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sprtcoding.obslearn.AdminMenu.Exams.AddChildTest;
import com.sprtcoding.obslearn.AdminMenu.Exams.ShowTestList;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.Model.CatListModel;
import com.sprtcoding.obslearn.R;

import java.util.List;

public class CatGridAdminAdapter extends BaseAdapter {
    Context context;
    List<CatListModel> catListModels;
    LayoutInflater inflater;

    public CatGridAdminAdapter(Context context, List<CatListModel> catListModels) {
        this.context = context;
        this.catListModels = catListModels;
    }

    @Override
    public int getCount() {
        return catListModels.size();
    }

    @Override
    public Object getItem(int i) {
        return catListModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null) {
            view = inflater.inflate(R.layout.cat_grid_items, null);
        }

        CatListModel cat = catListModels.get(position);

        view.setOnClickListener(view1 -> {
            DBQuery.g_selected_cat_index = position;

            Intent i = new Intent(context, ShowTestList.class);
            i.putExtra("id", cat.getCAT_ID());
            i.putExtra("cat_name", cat.getNAME());
            context.startActivity(i);
        });

        TextView cat_name = view.findViewById(R.id.cat_name);
        TextView test_count = view.findViewById(R.id.test_count);

        cat_name.setText(cat.getNAME());
        test_count.setText(cat.getNO_OF_TEST() + " Test/s");

        return view;
    }
}
