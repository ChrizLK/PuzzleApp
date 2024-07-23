package com.example.cross1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView marqueeText = findViewById(R.id.marqueeText);
        ScrollView scrollView = findViewById(R.id.scrollView);
        TranslateAnimation animation = new TranslateAnimation(0,20, scrollView.getHeight(), -marqueeText.getHeight());
        animation.setDuration(3000);
        animation.setFillAfter(true);
        marqueeText.startAnimation(animation);
            }
        };

