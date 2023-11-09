package com.sprtcoding.obslearn;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class Overview extends AppCompatActivity {
    private ImageView next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        _init();

        next_btn.setOnClickListener(view -> {
            YoYo.with(Techniques.SlideOutRight)
                    .duration(700)
                    .repeat(0)
                    .onEnd(animator -> {
                        Intent i = new Intent(this, LoginPage.class);
                        startActivity(i);
                        finish();
                    })
                    .playOn(next_btn);
        });

    }

    private void _init() {
        next_btn = findViewById(R.id.nextBtn);
    }
}