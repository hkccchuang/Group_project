package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static MediaPlayer mMediaPlayer = new MediaPlayer();//BGM
     public Intent next;
     ImageButton start;

     Boolean isNext=false;//If user go to other intent,music will not be pause



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//hide the action bar
        loadMusic();//bgm
        loadUI();

    }

    public void loadMusic(){
        mMediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    public void loadUI(){
        start=findViewById(R.id.btnStart);
        start.setOnClickListener(this);
    }


    protected void onPause(){
        super.onPause();

        if(!isNext){
        mMediaPlayer.pause();
        //If user go to other intent,music will not be pause
        }



    }

    protected void onResume(){
        super.onResume();
        mMediaPlayer.start();

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.btnStart){
            next=new Intent(MainActivity.this, StageSelection.class);
            isNext=true;
            startActivity(next);
        }

    }
}
