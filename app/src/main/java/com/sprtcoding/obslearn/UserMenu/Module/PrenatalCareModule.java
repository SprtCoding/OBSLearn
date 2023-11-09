package com.sprtcoding.obslearn.UserMenu.Module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sprtcoding.obslearn.R;

public class PrenatalCareModule extends AppCompatActivity {
    private PDFView pdfView;
    private ImageView back_btn;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prenatal_care_module);
        _init();

        // Load a PDF file from assets folder (change the file path as needed)
        pdfView.fromAsset("prenatal_care.pdf")
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();

        back_btn.setOnClickListener(view -> finish());
    }
    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void _init() {
        back_btn = findViewById(R.id.back_btn);
        pdfView = findViewById(R.id.pdfView);
    }
}