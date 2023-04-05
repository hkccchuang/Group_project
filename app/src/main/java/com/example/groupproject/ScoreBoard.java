package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Image;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ScoreBoard extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {//Do sorting

    SharedPreferences userScore;//user information storage
    SharedPreferences.Editor editor;
    ImageButton btnBack;
    public   SoundPool sp;//sound effect for button
    int soundEffect;//sound type
    private static long lastClickTime = 0;
    Boolean isNext=false;

    ScrollView scrollView;
    Animation toRight,rotate;
    Intent intent;
    Timer timer=new Timer();
    TextView number1,number2,number3,number4,number5,number6,number7,number8,number9,number10;
    public static int[][] ranking;//ranking for all the user
                                 // example["Charlie"] ["340"] ["250"] ["222"] ["333"] ["444"] ["555"]
                                 //        ["Chris"] ["320"] ["450"] ["212"] ["343"]
                                 //remember do sorting in Start (when delete data,store first)too
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        getSupportActionBar().hide();
        loadUI();
        loadAnimation();
        loadSound();
        sorting_UpLoad();
    }


    public void loadUI(){
        btnBack=findViewById(R.id.btnBack);
        scrollView=findViewById(R.id.scrollScore);
        number1=findViewById(R.id.number1);
        number2=findViewById(R.id.number2);
        number3=findViewById(R.id.number3);
        number4=findViewById(R.id.number4);
        number5=findViewById(R.id.number5);
        number6=findViewById(R.id.number6);
        number7=findViewById(R.id.number7);
        number8=findViewById(R.id.number8);
        number9=findViewById(R.id.number9);
        number10=findViewById(R.id.number10);


    }

    public void loadAnimation(){

        toRight= AnimationUtils.loadAnimation(this,R.anim.left_right);
        toRight.setAnimationListener(this);
        toRight.setDuration(700);
        rotate=AnimationUtils.loadAnimation(this,R.anim.rotate_left);
        rotate.setAnimationListener(this);



    }

    public void loadSound(){
        sp= new SoundPool(1000, AudioManager.STREAM_SYSTEM, 5);
        soundEffect = sp.load(this, R.raw.wood_hit, 1);
    }


    @Override
    public void onClick(View view) {

        if(!isFastDoubleClick()){
            sp.play(soundEffect, 0.3f, 0.3f, 0, 0, 1);
            if (view.getId() ==R.id.btnBack){btnBack.startAnimation(rotate);scrollView.startAnimation(toRight);}

            TimerTask task = new TimerTask() {
                public void run() {

                    finish();
                }
            };
            timer.schedule(task, 500);



        }

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

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }// A function detected fast clicking,

    @Override
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
        sp.release();
        if(!isNext){
            MainActivity.mMediaPlayer.pause();
            //If user go to other intent,music will not be pause
        }



    }

    protected void onResume(){
        super.onResume();
        loadSound();
        MainActivity.mMediaPlayer.start();
        isNext=false;

    }

    public void sorting_UpLoad(){

    }

}