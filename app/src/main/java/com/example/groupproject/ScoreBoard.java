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

import java.util.Arrays;
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

    @SuppressLint("SuspiciousIndentation")
    public void sorting_UpLoad(){//topx_score the key of preferences for score storage,
                                // topx_name is the key of preferences for name storage
                                // userName store the user data which are using the app now score1-10 for each stage score
                                //ranking store all the past user data,top1-10_ for top10 user

        userScore=getSharedPreferences("userName",MODE_PRIVATE);
        userRanking=getSharedPreferences("ranking",MODE_PRIVATE);

        editorRanking=userRanking.edit();

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