package com.sprtcoding.obslearn.AdminMenu.Exams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.sprtcoding.obslearn.R;

public class AboutUs extends AppCompatActivity {
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        back_btn = findViewById(R.id.back_btn_about);

        back_btn.setOnClickListener(view -> finish());
    }
}