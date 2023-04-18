package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScoreBoard extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {//Do sorting

    SharedPreferences userScore,userRanking;//user information storage
    SharedPreferences.Editor editorRanking;
    int totalScore;
    String name;
   String ranking_name[]=new String[11];
   int ranking_score[]=new int[11];

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        getSupportActionBar().hide();
        loadUI();
        loadAnimation();
        loadSound();
        setRanking();//IF RANKGKING IS NOT CHANGE THAN UPLOAD
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

        btnBack.setOnClickListener(this);


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







    public void setRanking(){

        userRanking=getSharedPreferences("ranking",MODE_PRIVATE);

        number1.setText(userRanking.getString("top1_name","")+"    "+String.valueOf(userRanking.getInt("top1_score",0)));
        number2.setText(userRanking.getString("top2_name","")+"    "+String.valueOf(userRanking.getInt("top2_score",0)));
        number3.setText(userRanking.getString("top3_name","")+"    "+String.valueOf(userRanking.getInt("top3_score",0)));
        number4.setText(userRanking.getString("top4_name","")+"    "+String.valueOf(userRanking.getInt("top4_score",0)));
        number5.setText(userRanking.getString("top5_name","")+"    "+String.valueOf(userRanking.getInt("top5_score",0)));
        number6.setText(userRanking.getString("top6_name","")+"    "+String.valueOf(userRanking.getInt("top6_score",0)));
        number7.setText(userRanking.getString("top7_name","")+"    "+String.valueOf(userRanking.getInt("top7_score",0)));
        number8.setText(userRanking.getString("top8_name","")+"    "+String.valueOf(userRanking.getInt("top8_score",0)));
        number9.setText(userRanking.getString("top9_name","")+"    "+String.valueOf(userRanking.getInt("top9_score",0)));
        number10.setText(userRanking.getString("top10_name","")+"    "+String.valueOf(userRanking.getInt("top10_score",0)));
       //set the ranking

    }

}