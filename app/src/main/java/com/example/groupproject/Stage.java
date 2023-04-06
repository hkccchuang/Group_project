package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Image;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Timer;

public class Stage extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {
     // Only a Class for extends,Not a real Stage
    int basicScore=200,bonusScore=200;//Max score = 400 per stage,one second=bonusScore-2
    int totalScore=0;
    public SharedPreferences userScore;//score storage
    public SharedPreferences.Editor editor;
    private static long lastClickTime = 0;//time between double click
    boolean isNext=false;
    public SoundPool sp;
    int soundEffect;
    Animation toLeft,rotate;
    ImageButton btnBack,btnHint,btnRestart;
    TextView title;




    public Chronometer chronometer;//timer
    public boolean timeRunning;//timer
    public long pauseOffset;//timer

    String scoreNumber;//key of value in share preferences example-score1
    int stageNumber;//key of ranking
    Intent intent;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        isNext=false;
    }

    public void loadAnimation(){

    }


    public void loadSound(){

    }

    public void loadUI(){

    }

    public void startChronometer(){
        if(!timeRunning){
            chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
            chronometer.start();
            timeRunning=true;
        }

    }//start timer

    public void pauseChronometer(){
        if(timeRunning){
            chronometer.stop();
            pauseOffset=SystemClock.elapsedRealtime()-chronometer.getBase();
            timeRunning=false;
        }

    }//pause timer
    public void resetChronometer(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset=0;
    }//reset timer for restart

    protected void onPause(){
        super.onPause();
        pauseChronometer();
        sp.release();
        //editor.commit;
        if(!isNext){
            MainActivity.mMediaPlayer.pause();
            //If user go to other intent,music will not be pause
        }
    }

    protected void onResume(){
        super.onResume();
        loadSound();
        startChronometer();
        MainActivity.mMediaPlayer.start();
        isNext=false;

    }
    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public void onClick(View view) {

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

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }//Ban back

    public int countHighestScore(){

        int elapsed = (int)(SystemClock.elapsedRealtime()-chronometer.getBase())/1000;
        bonusScore=bonusScore-elapsed;
        if(bonusScore>0){totalScore=bonusScore+basicScore;}
        else if(bonusScore<=0){return basicScore;}
        return totalScore;

    }//score = bonus+basic ,bonus < 0 then only return basic score

    public void beforeNextStage(){
        userScore=getSharedPreferences("userName",MODE_PRIVATE);
        editor=userScore.edit();
        if(countHighestScore()>userScore.getInt(getScoreNumber(),0)){
        editor.putInt(getScoreNumber(),countHighestScore());
        editor.commit();
        } //only record the highest score for a stage

    }

    public void resetStage(){
        isNext=true;
        intent=new Intent(getApplicationContext(),this.getClass());
        finish();
        overridePendingTransition(0,0);//no animation for reset,can add a animation
        startActivity(intent);

    }

    public String getScoreNumber(){
        return scoreNumber;// for upload to preferences
    }

    public int getStageNumber(){
        return  stageNumber;
    }

    public void giveHint(String hint){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This is a Hint");
            builder.setIcon(R.mipmap.ic_launcher_round); //icon
            builder.setMessage(hint);


            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });

            builder.create().show();

    }//give a dialog for show some hint

    public void nextStageDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congratulation!!");  //設置標題
        builder.setIcon(R.mipmap.ic_launcher_round); //標題前面那個小圖示
        builder.setMessage("Your Score is "+ String.valueOf(countHighestScore())); //提示訊息
        builder.setCancelable(false);
        pauseChronometer();


        //確定 取消 一般 這三種按鈕就看你怎麼發揮你想置入的功能囉
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isNext=true;
                finish();
                overridePendingTransition(0,0);//no animation for reset,can add a animation
                startActivity(intent);
                dialogInterface.dismiss();
            }
        });

        builder.create().show();


    }//give a dialog for show some hint







}