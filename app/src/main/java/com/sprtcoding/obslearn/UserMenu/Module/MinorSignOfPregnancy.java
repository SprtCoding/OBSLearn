package com.sprtcoding.obslearn.UserMenu.Module;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.sprtcoding.obslearn.R;

public class MinorSignOfPregnancy extends AppCompatActivity {
    private PDFView pdfView;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minor_sign_of_pregnancy);
        _init();

        // Load a PDF file from assets folder (change the file path as needed)
        pdfView.fromAsset("TOPIC_2.pdf")
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