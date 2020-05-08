package com.dlbz.studyjam2020.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dlbz.studyjam2020.R;

public class StudyJamActivity extends AppCompatActivity {

    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_jam);

        ivImage = findViewById(R.id.ivImage);

        Glide.with(this).asGif().load(R.raw.source).into(ivImage);
    }
}
