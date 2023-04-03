package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StageSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_selection);
        MainActivity.mMediaPlayer.start();
        getSupportActionBar().hide();
    }
}