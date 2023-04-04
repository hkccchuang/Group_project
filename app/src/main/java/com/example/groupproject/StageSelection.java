package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Image;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;

import java.util.Timer;
import java.util.TimerTask;

public class StageSelection extends AppCompatActivity implements View.OnClickListener,Animation.AnimationListener{
    ImageButton btnStage1,btnStage2,btnStage3,btnStage4,btnStage5,btnStage6,btnStage7,btnStage8,btnStage9,btnStage10,btnBack;
    ScrollView scrollView;

    SharedPreferences scoreInfo;//for stage lock,if there is no score of the stage then user can not go to the stage.


    Timer timer=new Timer();

    boolean isNext=false;

    private static long lastClickTime = 0;

    public SoundPool sp;//sound effect
    int soundEffect;

    Animation toRight,toLeft,rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_selection);
        MainActivity.mMediaPlayer.start();
        getSupportActionBar().hide();
        loadUI();
        loadSound();
        loadAnimation();
    }

    private void loadUI(){
       btnStage1=findViewById(R.id.btnStage1);
       btnStage2=findViewById(R.id.btnStage2);
       btnStage3=findViewById(R.id.btnStage3);
       btnStage4=findViewById(R.id.btnStage4);
       btnStage5=findViewById(R.id.btnStage5);
       btnStage6=findViewById(R.id.btnStage6);
       btnStage7=findViewById(R.id.btnStage7);
       btnStage8=findViewById(R.id.btnStage8);
       btnStage9=findViewById(R.id.btnStage9);
       btnStage10=findViewById(R.id.btnStage10);
       btnBack=findViewById(R.id.btnBack);
       scrollView=findViewById(R.id.scroll);


        btnStage1.setOnClickListener(this);
        btnStage2.setOnClickListener(this);
        btnStage3.setOnClickListener(this);
        btnStage4.setOnClickListener(this);
        btnStage5.setOnClickListener(this);
        btnStage6.setOnClickListener(this);
        btnStage7.setOnClickListener(this);
        btnStage8.setOnClickListener(this);
        btnStage9.setOnClickListener(this);
        btnStage10.setOnClickListener(this);
        btnBack.setOnClickListener(this);

      //remember set visibility for stage lock

    }

    private void loadAnimation(){

        toRight= AnimationUtils.loadAnimation(this,R.anim.left_right);
        toRight.setAnimationListener(this);
        toRight.setDuration(700);
        toLeft= AnimationUtils.loadAnimation(this,R.anim.right_left);
        toLeft.setAnimationListener(this);
        rotate=AnimationUtils.loadAnimation(this,R.anim.rotate_left);
        rotate.setAnimationListener(this);

    }

    private void loadSound(){
        sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundEffect = sp.load(this, R.raw.wood_hit, 1);//confirm.mp3
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }//Ban Back button

    protected void onPause(){
        super.onPause();
        if(!isNext){
            MainActivity.mMediaPlayer.pause();
            //If user go to other intent,music will not be pause
        }
    }

    protected void onResume(){
        super.onResume();
        MainActivity.mMediaPlayer.start();
        isNext=false;
    }
    protected void onDestroy(){
        super.onDestroy();
        sp.release();//release sound pool
    }
    @Override
    public void onClick(View view) {


        if(!isFastDoubleClick()){

            sp.play(soundEffect, 0.3f, 0.3f, 0, 0, 1);
            if (view.getId() ==R.id.btnBack){btnBack.startAnimation(rotate);scrollView.startAnimation(toRight);}
        TimerTask task = new TimerTask() {
            public void run() {

                if(view.getId()==R.id.btnBack){finish();}

            }
        };
        timer.schedule(task, 500);}


    }

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
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