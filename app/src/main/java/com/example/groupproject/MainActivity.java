package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,Animation.AnimationListener{

    public static MediaPlayer mMediaPlayer = new MediaPlayer();//BGM
    private SoundPool sp;//sound effect for button
     int soundEffect;//sound type
     public Intent next;//to next activity
     ImageButton start,contin,score;

     Boolean isNext=false;//If user go to other intent,music will not be pause
     Timer timer=new Timer();//for showing sound effect and animation

    Animation animRotateRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//hide the action bar
        loadSound();//bgm
        loadUI();
        loadAnimation();

    }

    public void loadSound(){
        mMediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundEffect = sp.load(this, R.raw.wood_hit, 1);
    }

    public void loadUI(){
        start=findViewById(R.id.btnStart);
        start.setOnClickListener(this);
        contin=findViewById(R.id.btnContinue);
        score=findViewById(R.id.btnScore);
    }
    public void loadAnimation()
    {animRotateRight= AnimationUtils.loadAnimation(this,R.anim.rotate_right);
        animRotateRight.setAnimationListener(this);
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

        sp.play(soundEffect, 1, 1, 0, 0, 1);//wood_hit sound effect
        if(view.getId()==R.id.btnStart)start.startAnimation(animRotateRight);
      TimerTask task=new TimerTask(){
      public void run(){

          if(view.getId()==R.id.btnStart){
              next=new Intent(MainActivity.this, StageSelection.class);
              isNext=true;

              startActivity(next);
          }

      }
   };      start.startAnimation(animRotateRight);
           timer.schedule(task, 1500);//stop 0.5second for next activity
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
