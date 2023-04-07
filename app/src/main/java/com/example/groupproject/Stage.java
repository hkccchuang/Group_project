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
    public SharedPreferences userScore,userRanking;//score storage
    public SharedPreferences.Editor editor;
    private static long lastClickTime = 0;//time between double click
    boolean isNext=false;
    public SoundPool sp;
    int soundEffect;
    Animation toLeft,rotate;
    ImageButton btnBack,btnHint,btnRestart;
    TextView title;

    String ranking_name[]=new String[11]; // start
    int ranking_score[]=new int[11];
    String name;
    SharedPreferences.Editor editorRanking; //end




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
        sorting_UpLoad();//upload data to ranking
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




    public void sorting_UpLoad(){//topx_score the key of preferences for score storage,
        // topx_name is the key of preferences for name storage
        // userName store the user data which are using the app now score1-10 for each stage score
        //ranking store all the past user data,top1-10_ for top10 user

        userScore=getSharedPreferences("userName",MODE_PRIVATE);
        userRanking=getSharedPreferences("ranking",MODE_PRIVATE);

        editorRanking=userRanking.edit();
        editor=userScore.edit();

        name=userScore.getString("name","0");
        totalScore=0+userScore.getInt("score1",0)+userScore.getInt("score2",0)+userScore.getInt("score3",0)+userScore.getInt("score4",0)+userScore.getInt("score5",0)+userScore.getInt("score6",0)+userScore.getInt("score7",0)
                +userScore.getInt("score8",0)+userScore.getInt("score9",0)+userScore.getInt("score10",0);//get data from user which is playing now

        if(userRanking.getInt("top1_score",0)!=0)
            ranking_score[1]=userRanking.getInt("top1_score",0);
        if(userRanking.getInt("top2_score",0)!=0)
            ranking_score[2]=userRanking.getInt("top2_score",0);
        if(userRanking.getInt("top3_score",0)!=0)
            ranking_score[3]=userRanking.getInt("top3_score",0);
        if(userRanking.getInt("top4_score",0)!=0)
            ranking_score[4]=userRanking.getInt("top4_score",0);
        if(userRanking.getInt("top5_score",0)!=0)
            ranking_score[5]=userRanking.getInt("top5_score",0);
        if(userRanking.getInt("top6_score",0)!=0)
            ranking_score[6]=userRanking.getInt("top6_score",0);
        if(userRanking.getInt("top7_score",0)!=0)
            ranking_score[7]=userRanking.getInt("top7_score",0);
        if(userRanking.getInt("top8_score",0)!=0)
            ranking_score[8]=userRanking.getInt("top8_score",0);
        if(userRanking.getInt("top9_score",0)!=0)
            ranking_score[9]=userRanking.getInt("top9_score",0);


        if(userRanking.getString("top1_name","0")!="0")
            ranking_name[1]=userRanking.getString("top1_name","");
        if(userRanking.getString("top2_name","0")!="0")
            ranking_name[2]=userRanking.getString("top2_name","");
        if(userRanking.getString("top3_name","0")!="0")
            ranking_name[3]=userRanking.getString("top3_name","");
        if(userRanking.getString("top4_name","0")!="0")
            ranking_name[4]=userRanking.getString("top4_name","");
        if(userRanking.getString("top5_name","0")!="0")
            ranking_name[5]=userRanking.getString("top5_name","");
        if(userRanking.getString("top6_name","0")!="0")
            ranking_name[6]=userRanking.getString("top6_name","");
        if(userRanking.getString("top7_name","0")!="0")
            ranking_name[7]=userRanking.getString("top7_name","");
        if(userRanking.getString("top8_name","0")!="0")
            ranking_name[8]=userRanking.getString("top8_name","");
        if(userRanking.getString("top9_name","0")!="0")
            ranking_name[9]=userRanking.getString("top9_name","");

        //A WTF way to get top10 data from ranking.xml. if top1_ not nul,top1=top1
        // -------------------------------------------------------------------
        //Case 1:same name
        if(name==userRanking.getString("top1_name","")&&totalScore>=userRanking.getInt("top1_score",0)){
            editorRanking.putInt("top1_score", totalScore);
            editorRanking.putString("top1_name", name);
        }
        else  if(name==userRanking.getString("top2_name","")&&totalScore>=userRanking.getInt("top2_score",0)){
            editorRanking.putInt("top2_score", totalScore);
            editorRanking.putString("top2_name", name);}
        else  if(name==userRanking.getString("top3_name","")&&totalScore>=userRanking.getInt("top3_score",0)){
            editorRanking.putInt("top3_score", totalScore);
            editorRanking.putString("top3_name", name);}
        else  if(name==userRanking.getString("top4_name","")&&totalScore>=userRanking.getInt("top4_score",0)){
            editorRanking.putInt("top4_score", totalScore);
            editorRanking.putString("top4_name", name);}
        else  if(name==userRanking.getString("top5_name","")&&totalScore>=userRanking.getInt("top5_score",0)){
            editorRanking.putInt("top5_score", totalScore);
            editorRanking.putString("top5_name", name);}
        else  if(name==userRanking.getString("top6_name","")&&totalScore>=userRanking.getInt("top6_score",0)){
            editorRanking.putInt("top6_score", totalScore);
            editorRanking.putString("top6_name", name);}
        else  if(name==userRanking.getString("top7_name","")&&totalScore>=userRanking.getInt("top7_score",0)){
            editorRanking.putInt("top7_score", totalScore);
            editorRanking.putString("top7_name", name);}
        else  if(name==userRanking.getString("top8_name","")&&totalScore>=userRanking.getInt("top8_score",0)){
            editorRanking.putInt("top8_score", totalScore);
            editorRanking.putString("top8_name", name);}
        else  if(name==userRanking.getString("top9_name","")&&totalScore>=userRanking.getInt("top9_score",0)){
            editorRanking.putInt("top9_score", totalScore);
            editorRanking.putString("top9_name", name);}
        else  if(name==userRanking.getString("top10_name","")&&totalScore>=userRanking.getInt("top10_score",0)){
            editorRanking.putInt("top10_score", totalScore);
            editorRanking.putString("top10_name", name);}

        //Case2:not the same name
        else  if (userRanking.getInt("top1_score", 0) <= totalScore) {

            if(userRanking.getString("top2_name","0")!=name){
                editorRanking.putInt("top2_score", ranking_score[1]);
                editorRanking.putString("top2_name", ranking_name[1]);}

            if(userRanking.getInt("top3_score",0)!=0){
                editorRanking.putInt("top3_score", ranking_score[2]);
                editorRanking.putString("top3_name", ranking_name[2]);}

            if(userRanking.getInt("top4_score",0)!=0){
                editorRanking.putInt("top4_score", ranking_score[3]);
                editorRanking.putString("top4_name", ranking_name[3]);}

            if(userRanking.getInt("top5_score",0)!=0){
                editorRanking.putInt("top5_score", ranking_score[4]);
                editorRanking.putString("top5_name", ranking_name[4]);}

            if(userRanking.getInt("top6_score",0)!=0){
                editorRanking.putInt("top6_score", ranking_score[5]);
                editorRanking.putString("top6_name", ranking_name[5]);}

            if(userRanking.getInt("top7_score",0)!=0){
                editorRanking.putInt("top7_score", ranking_score[6]);
                editorRanking.putString("top7_name", ranking_name[6]);}

            if(userRanking.getInt("top8_score",0)!=0){
                editorRanking.putInt("top8_score", ranking_score[7]);
                editorRanking.putString("top8_name", ranking_name[7]);}

            if(userRanking.getInt("top9_score",0)!=0){
                editorRanking.putInt("top9_score", ranking_score[8]);
                editorRanking.putString("top9_name", ranking_name[8]);}

            if(userRanking.getInt("top10_score",0)!=0){
                editorRanking.putInt("top10_score", ranking_score[9]);
                editorRanking.putString("top10_name", ranking_name[9]);}

            editorRanking.putInt("top1_score", totalScore);
            editorRanking.putString("top1_name", name);
        }
        else if (userRanking.getInt("top2_score", 0) <= totalScore) {

            if(userRanking.getString("top3_name","0")!=name){
                editorRanking.putInt("top3_score", ranking_score[2]);
                editorRanking.putString("top3_name", ranking_name[2]);}

            if(userRanking.getInt("top4_score",0)!=0){
                editorRanking.putInt("top4_score", ranking_score[3]);
                editorRanking.putString("top4_name", ranking_name[3]);}

            if(userRanking.getInt("top5_score",0)!=0){
                editorRanking.putInt("top5_score", ranking_score[4]);
                editorRanking.putString("top5_name", ranking_name[4]);}

            if(userRanking.getInt("top6_score",0)!=0){
                editorRanking.putInt("top6_score", ranking_score[5]);
                editorRanking.putString("top6_name", ranking_name[5]);}

            if(userRanking.getInt("top7_score",0)!=0){
                editorRanking.putInt("top7_score", ranking_score[6]);
                editorRanking.putString("top7_name", ranking_name[6]);}

            if(userRanking.getInt("top8_score",0)!=0){
                editorRanking.putInt("top8_score", ranking_score[7]);
                editorRanking.putString("top8_name", ranking_name[7]);}

            if(userRanking.getInt("top9_score",0)!=0){
                editorRanking.putInt("top9_score", ranking_score[8]);
                editorRanking.putString("top9_name", ranking_name[8]);}

            if(userRanking.getInt("top10_score",0)!=0){
                editorRanking.putInt("top10_score", ranking_score[9]);
                editorRanking.putString("top10_name", ranking_name[9]);}


            editorRanking.putInt("top2_score", totalScore);
            editorRanking.putString("top2_name", name);
        } else if (userRanking.getInt("top3_score", 0) <= totalScore) {

            if(userRanking.getString("top4_name","0")!=name){
                editorRanking.putInt("top4_score", ranking_score[3]);
                editorRanking.putString("top4_name", ranking_name[3]);}

            if(userRanking.getInt("top5_score",0)!=0){
                editorRanking.putInt("top5_score", ranking_score[4]);
                editorRanking.putString("top5_name", ranking_name[4]);}

            if(userRanking.getInt("top6_score",0)!=0){
                editorRanking.putInt("top6_score", ranking_score[5]);
                editorRanking.putString("top6_name", ranking_name[5]);}

            if(userRanking.getInt("top7_score",0)!=0){
                editorRanking.putInt("top7_score", ranking_score[6]);
                editorRanking.putString("top7_name", ranking_name[6]);}

            if(userRanking.getInt("top8_score",0)!=0){
                editorRanking.putInt("top8_score", ranking_score[7]);
                editorRanking.putString("top8_name", ranking_name[7]);}

            if(userRanking.getInt("top9_score",0)!=0){
                editorRanking.putInt("top9_score", ranking_score[8]);
                editorRanking.putString("top9_name", ranking_name[8]);}

            if(userRanking.getInt("top10_score",0)!=0){
                editorRanking.putInt("top10_score", ranking_score[9]);
                editorRanking.putString("top10_name", ranking_name[9]);}


            editorRanking.putInt("top3_score", totalScore);
            editorRanking.putString("top3_name", name);
        } else if (userRanking.getInt("top4_score", 0) <= totalScore) {

            if(userRanking.getString("top5_name","0")!=name){
                editorRanking.putInt("top5_score", ranking_score[4]);
                editorRanking.putString("top5_name", ranking_name[4]);}

            if(userRanking.getInt("top6_score",0)!=0){
                editorRanking.putInt("top6_score", ranking_score[5]);
                editorRanking.putString("top6_name", ranking_name[5]);}

            if(userRanking.getInt("top7_score",0)!=0){
                editorRanking.putInt("top7_score", ranking_score[6]);
                editorRanking.putString("top7_name", ranking_name[6]);}

            if(userRanking.getInt("top8_score",0)!=0){
                editorRanking.putInt("top8_score", ranking_score[7]);
                editorRanking.putString("top8_name", ranking_name[7]);}

            if(userRanking.getInt("top9_score",0)!=0){
                editorRanking.putInt("top9_score", ranking_score[8]);
                editorRanking.putString("top9_name", ranking_name[8]);}

            if(userRanking.getInt("top10_score",0)!=0){
                editorRanking.putInt("top10_score", ranking_score[9]);
                editorRanking.putString("top10_name", ranking_name[9]);}

            editorRanking.putInt("top4_score", totalScore);
            editorRanking.putString("top4_name", name);
        } else if (userRanking.getInt("top5_score", 0) <= totalScore) {

            if(userRanking.getString("top6_name","0")!=name){
                editorRanking.putInt("top6_score", ranking_score[5]);
                editorRanking.putString("top6_name", ranking_name[5]);}

            if(userRanking.getInt("top7_score",0)!=0){
                editorRanking.putInt("top7_score", ranking_score[6]);
                editorRanking.putString("top7_name", ranking_name[6]);}

            if(userRanking.getInt("top8_score",0)!=0){
                editorRanking.putInt("top8_score", ranking_score[7]);
                editorRanking.putString("top8_name", ranking_name[7]);}

            if(userRanking.getInt("top9_score",0)!=0){
                editorRanking.putInt("top9_score", ranking_score[8]);
                editorRanking.putString("top9_name", ranking_name[8]);}

            if(userRanking.getInt("top10_score",0)!=0){
                editorRanking.putInt("top10_score", ranking_score[9]);
                editorRanking.putString("top10_name", ranking_name[9]);}

            editorRanking.putInt("top5_score", totalScore);
            editorRanking.putString("top5_name", name);
        } else if (userRanking.getInt("top6_score", 0) <= totalScore) {

            if(userRanking.getString("top7_name","0")!=name){
                editorRanking.putInt("top7_score", ranking_score[6]);
                editorRanking.putString("top7_name", ranking_name[6]);}

            if(userRanking.getInt("top8_score",0)!=0){
                editorRanking.putInt("top8_score", ranking_score[7]);
                editorRanking.putString("top8_name", ranking_name[7]);}

            if(userRanking.getInt("top9_score",0)!=0){
                editorRanking.putInt("top9_score", ranking_score[8]);
                editorRanking.putString("top9_name", ranking_name[8]);}

            if(userRanking.getInt("top10_score",0)!=0){
                editorRanking.putInt("top10_score", ranking_score[9]);
                editorRanking.putString("top10_name", ranking_name[9]);}

            editorRanking.putInt("top6_score", totalScore);
            editorRanking.putString("top6_name", name);
        } else if (userRanking.getInt("top7score", 0) <= totalScore) {

            if(userRanking.getString("top8_name","0")!=name){
                editorRanking.putInt("top8_score", ranking_score[7]);
                editorRanking.putString("top8_name", ranking_name[7]);}

            if(userRanking.getInt("top9_score",0)!=0){
                editorRanking.putInt("top9_score", ranking_score[8]);
                editorRanking.putString("top9_name", ranking_name[8]);}

            if(userRanking.getInt("top10_score",0)!=0){
                editorRanking.putInt("top10_score", ranking_score[9]);
                editorRanking.putString("top10_name", ranking_name[9]);}

            editorRanking.putInt("top7_score", totalScore);
            editorRanking.putString("top7_name", name);
        } else if (userRanking.getInt("top8_score", 0) <= totalScore) {

            if(userRanking.getString("top9_name","0")!=name){
                editorRanking.putInt("top9_score", ranking_score[8]);
                editorRanking.putString("top9_name", ranking_name[8]);}

            if(userRanking.getInt("top10_score",0)!=0){
                editorRanking.putInt("top10_score", ranking_score[9]);
                editorRanking.putString("top10_name", ranking_name[9]);}

            editorRanking.putInt("top8_score", totalScore);
            editorRanking.putString("top8_name", name);
        } else if (userRanking.getInt("top9_score", 0) <= totalScore) {

            if(userRanking.getInt("top10_score",0)!=0){
                editorRanking.putInt("top10_score", ranking_score[9]);
                editorRanking.putString("top10_name", ranking_name[9]);}

            editorRanking.putInt("top9_score", totalScore);
            editorRanking.putString("top9_name", name);

        } else if (userRanking.getInt("top10_score", 0) <= totalScore) {
            editorRanking.putInt("top10_score", totalScore);
            editorRanking.putString("top10_name", name);
        }


        //-----------------------------------------------------------------------------------------------------
        // A WTF way for sorting,
        // if the user score >=top1 then user become top1
        // original 1->2 2->3 3->4 4>5......
        //if the user score <top1 and >=top2,then user become top2
        // original 2->3 3->4 4->5.......


        editorRanking.commit();
    editor.commit();}


}