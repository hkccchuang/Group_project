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
    Intent intent;

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


        scoreInfo=getSharedPreferences("userName",MODE_PRIVATE);

        if(scoreInfo.getInt("score1",0)!=0){
            btnStage2.setAlpha(0.0f);
        }
        if(scoreInfo.getInt("score2",0)!=0){
            btnStage3.setAlpha(0.0f);
        }
        if(scoreInfo.getInt("score3",0)!=0){
            btnStage4.setAlpha(0.0f);
        }
         if(scoreInfo.getInt("score4",0)!=0){
            btnStage5.setAlpha(0.0f);
        }
        if(scoreInfo.getInt("score5",0)!=0){
            btnStage6.setAlpha(0.0f);
        }
         if(scoreInfo.getInt("score6",0)!=0){
            btnStage7.setAlpha(0.0f);
        }
         if(scoreInfo.getInt("score7",0)!=0){
            btnStage8.setAlpha(0.0f);
        }
         if(scoreInfo.getInt("score8",0)!=0){
            btnStage9.setAlpha(0.0f);
        }
         if(scoreInfo.getInt("score9",0)!=0){
            btnStage10.setAlpha(0.0f);
        }

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
        sp= new SoundPool(1000, AudioManager.STREAM_SYSTEM, 5);
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
    protected void onDestroy(){
        super.onDestroy();
        sp.release();//release sound pool
    }
    @Override
    public void onClick(View view) {

        scoreInfo=getSharedPreferences("userName",MODE_PRIVATE);


        if(!isFastDoubleClick()){

            sp.play(soundEffect, 0.3f, 0.3f, 0, 0, 1);
            if (view.getId() ==R.id.btnBack){btnBack.startAnimation(rotate);scrollView.startAnimation(toRight);}

        TimerTask task = new TimerTask() {
            public void run() {

                      if(view.getId()==R.id.btnBack){finish();}
                else  if(view.getId()==R.id.btnStage1){
                    isNext=true;
                    intent= new Intent(StageSelection.this,Stage1.class);
                    startActivity(intent);
                    finish();
                }
                      else  if(view.getId()==R.id.btnStage2&&scoreInfo.getInt("score1",0)!=0){
                          isNext=true;
                          intent= new Intent(StageSelection.this,Stage2.class);
                          startActivity(intent);
                          finish();
                      }
                      else  if(view.getId()==R.id.btnStage3&&scoreInfo.getInt("score2",0)!=0){
                          isNext=true;
                          intent= new Intent(StageSelection.this,Stage3.class);
                          startActivity(intent);
                          finish();
                      }
                      else  if(view.getId()==R.id.btnStage4&&scoreInfo.getInt("score3",0)!=0){
                          isNext=true;
                          intent= new Intent(StageSelection.this,Stage4.class);
                          startActivity(intent);
                          finish();
                      }
                      else  if(view.getId()==R.id.btnStage5&&scoreInfo.getInt("score4",0)!=0){
                          isNext=true;
                          intent= new Intent(StageSelection.this,Stage5.class);
                          startActivity(intent);
                          finish();
                      }
                      else  if(view.getId()==R.id.btnStage6&&scoreInfo.getInt("score5",0)!=0){
                          isNext=true;
                          intent= new Intent(StageSelection.this,Stage6.class);
                          startActivity(intent);
                          finish();
                      }
                      else  if(view.getId()==R.id.btnStage7&&scoreInfo.getInt("score6",0)!=0){
                          isNext=true;
                          intent= new Intent(StageSelection.this,Stage7.class);
                          startActivity(intent);
                          finish();
                      }
                      else  if(view.getId()==R.id.btnStage8&&scoreInfo.getInt("score7",0)!=0){
                          isNext=true;
                          intent= new Intent(StageSelection.this,Stage8.class);
                          startActivity(intent);
                          finish();
                      }
                      else  if(view.getId()==R.id.btnStage9&&scoreInfo.getInt("score8",0)!=0){
                          isNext=true;
                          intent= new Intent(StageSelection.this,Stage9.class);
                          startActivity(intent);
                          finish();
                      }
                      else  if(view.getId()==R.id.btnStage10&&scoreInfo.getInt("score9",0)!=0){
                          isNext=true;
                          intent= new Intent(StageSelection.this,Stage10.class);
                          startActivity(intent);
                          finish();
                      }//add stage lock if score=0 or not exist

            }
        };
        timer.schedule(task, 500);}


    }

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 900) {
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