package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,Animation.AnimationListener{

    public static MediaPlayer mMediaPlayer = new MediaPlayer();//BGM
    public   SoundPool sp;//sound effect for button
    int soundEffect;//sound type

    private static long lastClickTime = 0;//prevent fast clicking

     public Intent next;//to next activity
     ImageButton start,contin,score,login;

     Boolean isNext=false;//If user go to other intent,music will not be pause
     Timer timer=new Timer();//for showing sound effect and animation

    Animation animRotateRight;

    SharedPreferences userInfo;//user information storage
    SharedPreferences.Editor editor;


    public static int[] scoreTotal; //store all the score for a single user
    public static int[] ranking;//ranking for all the user


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//hide the action bar
        loadSound();
        loadMusic();//bgm
        loadUI();
        loadAnimation();

    }

    public void loadMusic(){
        mMediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mMediaPlayer.setVolume(0.3f, 0.3f);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    public void loadSound(){

        sp= new SoundPool(1000, AudioManager.STREAM_SYSTEM, 5);
        soundEffect = sp.load(this, R.raw.wood_hit, 1);


    }

    public void loadUI(){
        start=findViewById(R.id.btnStart);
        start.setOnClickListener(this);
        contin=findViewById(R.id.btnContinue);
        contin.setOnClickListener(this);
        score=findViewById(R.id.btnScore);
        score.setOnClickListener(this);
        login=findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
        userInfo=getSharedPreferences("userName",MODE_PRIVATE);
        editor=userInfo.edit();

    }
    public void loadAnimation()
    {animRotateRight= AnimationUtils.loadAnimation(this,R.anim.rotate_right);
        animRotateRight.setAnimationListener(this);
        }

    protected void onPause(){
        super.onPause();
        editor.commit();
        if(!isNext){
        mMediaPlayer.pause();
        //If user go to other intent,music will not be pause
        }



    }

    protected void onResume(){
        super.onResume();
        isNext=false;
        mMediaPlayer.start();

    }

    protected void onDestroy(){
        super.onDestroy();
        sp.release();//release sound pool
    }

    @Override
    public void onClick(View view) {


         if(!isFastDoubleClick()) {
             sp.play(soundEffect, 0.3f, 0.3f, 0, 0, 1);//wood_hit sound effect
             if (view.getId() == R.id.btnStart) start.startAnimation(animRotateRight);
             else if (view.getId() == R.id.btnContinue&&userInfo.getString("name","0")!="0")
                 contin.startAnimation(animRotateRight);//If not user data,no animation
             else if (view.getId() == R.id.btnScore)
                 score.startAnimation(animRotateRight); //animation

             TimerTask task = new TimerTask() {
                 public void run() {

                     if (view.getId() == R.id.btnStart) {
                         next = new Intent(MainActivity.this, UserRegister.class);
                         isNext = true;
                         startActivity(next);
                     } else if (view.getId() == R.id.btnContinue&& userInfo.getString("name","0")!="0") {
                         next = new Intent(MainActivity.this, StageSelection.class);
                         isNext = true;
                         startActivity(next);//Have not effect if there is no user before
                     } else if (view.getId() == R.id.btnScore) {
                         next = new Intent(MainActivity.this, ScoreBoard.class);
                         isNext = true;
                         startActivity(next);
                     }
                     else if(view.getId()==R.id.btnLogin){
                         next = new Intent(MainActivity.this, Login.class);
                         isNext = true;
                         startActivity(next);

                     }

                 }
             };
             timer.schedule(task, 600);
             //stop 0.6second for next activity,the value should less
             // than 2000(Max time value in isFastDoubleClick click function) for prevent fast clicking event
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

    public void cleanPrefer(){
        userInfo=getSharedPreferences("userName",MODE_PRIVATE);
        editor=userInfo.edit();
        editor.clear();
    }//pretend a new user for testing
}
