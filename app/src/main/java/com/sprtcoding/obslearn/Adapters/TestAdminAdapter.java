package com.sprtcoding.obslearn.Adapters;

import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_catListModel;
import static com.sprtcoding.obslearn.FireStoreDB.DBQuery.g_selected_cat_index;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sprtcoding.obslearn.Adapters.UsersAdapters.TestChildAdapter;
import com.sprtcoding.obslearn.AdminMenu.Exams.AddChildTest;
import com.sprtcoding.obslearn.AdminMenu.Exams.AddNewQuestions;
import com.sprtcoding.obslearn.AdminMenu.Exams.AdminExamCat;
import com.sprtcoding.obslearn.AdminMenu.Exams.QuestionsList;
import com.sprtcoding.obslearn.FireStoreDB.DBQuery;
import com.sprtcoding.obslearn.FireStoreDB.MyCompleteListener;
import com.sprtcoding.obslearn.Loadings.LoadingDialog;
import com.sprtcoding.obslearn.Model.TestCatModel;
import com.sprtcoding.obslearn.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAdminAdapter extends RecyclerView.Adapter<TestAdminAdapter.ViewHolder> {
    Context context;
    List<TestCatModel> testCatList;
    private LoadingDialog loadingDialog;
    AlertDialog.Builder deleteAlertBuilder;

    public TestAdminAdapter(Context context, List<TestCatModel> testCatList) {
        this.context = context;
        this.testCatList = testCatList;
    }

    @NonNull
    @Override
    public TestAdminAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_list_admin, parent, false);
        return new TestAdminAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TestAdminAdapter.ViewHolder holder, int position) {
        TestCatModel test = testCatList.get(position);

        loadingDialog = new LoadingDialog(context);
        deleteAlertBuilder = new AlertDialog.Builder(context);

        holder.test_count.setText(test.getTestID());
        holder.test_timer.setText(test.getTime() + " min");

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context, QuestionsList.class);
            i.putExtra("TestID", test.getTestID());
            context.startActivity(i);
        });

        String catID = g_catListModel.get(g_selected_cat_index).getCAT_ID();

        holder.edit_btn.setOnClickListener(view -> {
            Intent i = new Intent(context, AddChildTest.class);
            i.putExtra("cat_id", catID);
            i.putExtra("fieldNameID", test.getFieldNameID());
            i.putExtra("fieldNameTime", test.getFieldNameTime());
            i.putExtra("testName", test.getTestID());
            i.putExtra("testTime", test.getTime());
            context.startActivity(i);
        });

        holder.delete_btn.setOnClickListener(view -> {
            deleteAlertBuilder.setTitle("Delete")
                    .setMessage("Are you sure want to delete " + test.getTestID() + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        loadingDialog.show();
                        Handler handler = new Handler();
                        Runnable runnable = () -> {
                            DBQuery.deleteTest(
                                    catID,
                                    test.getFieldNameID(),
                                    test.getFieldNameTime(),
                                    test.getTestID(),
                                    new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            AdminExamCat.catGridAdminAdapter.notifyDataSetChanged();
                                            Toast.makeText(context, test.getTestID()+" Removed successfully.", Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismiss();
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismiss();
                                        }
                                    }
                            );
                        };
                        handler.postDelayed(runnable, 3000);
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                        loadingDialog.dismiss();
                    })
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return testCatList.size();
    }

    public void clearData() {
        if (testCatList != null) {
            testCatList.clear();
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView test_count, test_timer, edit_btn, delete_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            test_count = itemView.findViewById(R.id.test_count);
            test_timer = itemView.findViewById(R.id.test_timer);
            edit_btn = itemView.findViewById(R.id.edit_btn);
            delete_btn = itemView.findViewById(R.id.delete_btn);
        }
    }
}
