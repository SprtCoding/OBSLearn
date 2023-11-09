package com.sprtcoding.obslearn.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sprtcoding.obslearn.AdminMenu.Exams.UserStat;
import com.sprtcoding.obslearn.Model.ScoreModel;
import com.sprtcoding.obslearn.Model.UserModel;
import com.sprtcoding.obslearn.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserGridAdapter extends BaseAdapter {
    Context context;
    List<UserModel> userList;
    LayoutInflater inflater;

    public UserGridAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return userList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null) {
            view = inflater.inflate(R.layout.student_list_grid, null);
        }

        UserModel user = userList.get(position);

        TextView stud_name = view.findViewById(R.id.stud_name);
        TextView stud_email = view.findViewById(R.id.stud_email);
        CircleImageView userPic = view.findViewById(R.id.userPic);

        if(user.getGENDER().equals("Male")) {
            Picasso.get().load(R.drawable.student_boy).into(userPic);
        }else if(user.getGENDER().equals("Female")) {
            Picasso.get().load(R.drawable.student_new).into(userPic);
        }

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
}
