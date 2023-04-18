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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

public class Stage extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {
     // Only a Class for extends,Not a real Stage
    int basicScore=200,bonusScore=400;//Max score = 400 per stage,one second=bonusScore-2
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

    float x,y,dx,dy;//for drag event,change position for image






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        isNext=false;
    }

    public void loadAnimation(){

    }


    public void loadSound(){

        this.sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        this.soundEffect = this.sp.load(this, R.raw.cheer, 1);

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
        sorting_UpLoad();
       //upload data to ranking
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

        sp.play(soundEffect, 0.3f, 0.3f, 0, 0, 1);
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

        name=userScore.getString("name","0");
        totalScore=0+userScore.getInt("score1",0)+userScore.getInt("score2",0)+userScore.getInt("score3",0)+userScore.getInt("score4",0)+userScore.getInt("score5",0)+userScore.getInt("score6",0)+userScore.getInt("score7",0)
                +userScore.getInt("score8",0)+userScore.getInt("score9",0)+userScore.getInt("score10",0);//get data from user which is playing now





        if(userRanking.getInt("top1_score",0)!=0)
            ranking_score[1]=userRanking.getInt("top1_score",0);
        else ranking_score[1]=0;

        if(userRanking.getInt("top2_score",0)!=0)
            ranking_score[2]=userRanking.getInt("top2_score",0);
        else ranking_score[2]=0;

        if(userRanking.getInt("top3_score",0)!=0)
            ranking_score[3]=userRanking.getInt("top3_score",0);
        else ranking_score[3]=0;

        if(userRanking.getInt("top4_score",0)!=0)
            ranking_score[4]=userRanking.getInt("top4_score",0);
        else ranking_score[4]=0;

        if(userRanking.getInt("top5_score",0)!=0)
            ranking_score[5]=userRanking.getInt("top5_score",0);
        else ranking_score[5]=0;

        if(userRanking.getInt("top6_score",0)!=0)
            ranking_score[6]=userRanking.getInt("top6_score",0);
        else ranking_score[6]=0;

        if(userRanking.getInt("top7_score",0)!=0)
            ranking_score[7]=userRanking.getInt("top7_score",0);
        else ranking_score[7]=0;

        if(userRanking.getInt("top8_score",0)!=0)
            ranking_score[8]=userRanking.getInt("top8_score",0);
        else ranking_score[8]=0;

        if(userRanking.getInt("top9_score",0)!=0)
            ranking_score[9]=userRanking.getInt("top9_score",0);
        else ranking_score[9]=0;

        if(userRanking.getInt("top10_score",0)!=0)
            ranking_score[10]=userRanking.getInt("top10_score",0);
        else ranking_score[10]=0;


        if(userRanking.getString("top1_name","0")!="0")
            ranking_name[1]=userRanking.getString("top1_name","");
        else ranking_name[1]="";

        if(userRanking.getString("top2_name","0")!="0")
            ranking_name[2]=userRanking.getString("top2_name","");
        else ranking_name[2]="";

        if(userRanking.getString("top3_name","0")!="0")
            ranking_name[3]=userRanking.getString("top3_name","");
        else ranking_name[3]="";

        if(userRanking.getString("top4_name","0")!="0")
            ranking_name[4]=userRanking.getString("top4_name","");
        else ranking_name[4]="";

        if(userRanking.getString("top5_name","0")!="0")
            ranking_name[5]=userRanking.getString("top5_name","");
        else ranking_name[5]="";

        if(userRanking.getString("top6_name","0")!="0")
            ranking_name[6]=userRanking.getString("top6_name","");
        else ranking_name[6]="";

        if(userRanking.getString("top7_name","0")!="0")
            ranking_name[7]=userRanking.getString("top7_name","");
        else ranking_name[7]="";

        if(userRanking.getString("top8_name","0")!="0")
            ranking_name[8]=userRanking.getString("top8_name","");
        else ranking_name[8]="";

        if(userRanking.getString("top9_name","0")!="0")
            ranking_name[9]=userRanking.getString("top9_name","");
        else ranking_name[9]="";

        if(userRanking.getString("top10_name","0")!="0")
            ranking_name[10]=userRanking.getString("top10_name","");
        else ranking_name[10]="";





       User user1 = new User( ranking_name[1],  ranking_score[1]);//top1
       User user2 = new User( ranking_name[2],  ranking_score[2]);
       User user3 = new User( ranking_name[3],  ranking_score[3]);
       User user4 = new User( ranking_name[4],  ranking_score[4]);
       User user5 = new User( ranking_name[5],  ranking_score[5]);
       User user6 = new User( ranking_name[6],  ranking_score[6]);
       User user7 = new User( ranking_name[7],  ranking_score[7]);
       User user8 = new User( ranking_name[8],  ranking_score[8]);
       User user9 = new User( ranking_name[9],  ranking_score[9]);
       User user10 = new User( ranking_name[10],  ranking_score[10]);
       User user11 = new User(name,  totalScore);//top10



        List<User> users = new ArrayList<>();

        //if it is a new user
        if(     !ranking_name[1].equals(name)&&
                !ranking_name[2].equals(name)&&
                !ranking_name[3].equals(name)&&
                !ranking_name[4].equals(name)&&
                !ranking_name[5].equals(name)&&
                !ranking_name[6].equals(name)&&
                !ranking_name[7].equals(name)&&
                !ranking_name[8].equals(name)&&
                !ranking_name[9].equals(name)&&
                !ranking_name[10].equals(name)){
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        users.add(user7);
        users.add(user8);
        users.add(user9);
        users.add(user10);
        users.add(user11);
        } else if (ranking_name[1].equals(name)) { // not new
            user1.setName(name);
            user1.setScore(totalScore);
            users.add(user1);
            users.add(user2);
            users.add(user3);
            users.add(user4);
            users.add(user5);
            users.add(user6);
            users.add(user7);
            users.add(user8);
            users.add(user9);
            users.add(user10);
        }
        else if (ranking_name[2].equals(name)) {
            user2.setName(name);
            user2.setScore(totalScore);
            users.add(user1);
            users.add(user2);
            users.add(user3);
            users.add(user4);
            users.add(user5);
            users.add(user6);
            users.add(user7);
            users.add(user8);
            users.add(user9);
            users.add(user10);
        }
        else if (ranking_name[3].equals(name)) {
            user3.setName(name);
            user3.setScore(totalScore);
            users.add(user1);
            users.add(user2);
            users.add(user3);
            users.add(user4);
            users.add(user5);
            users.add(user6);
            users.add(user7);
            users.add(user8);
            users.add(user9);
            users.add(user10);
        }
        else if (ranking_name[4].equals(name)) {
            user4.setName(name);
            user4.setScore(totalScore);
            users.add(user1);
            users.add(user2);
            users.add(user3);
            users.add(user4);
            users.add(user5);
            users.add(user6);
            users.add(user7);
            users.add(user8);
            users.add(user9);
            users.add(user10);
        }
        else if (ranking_name[5].equals(name)) {
            user5.setName(name);
            user5.setScore(totalScore);
            users.add(user1);
            users.add(user2);
            users.add(user3);
            users.add(user4);
            users.add(user5);
            users.add(user6);
            users.add(user7);
            users.add(user8);
            users.add(user9);
            users.add(user10);
        }
        else if (ranking_name[6].equals(name)) {
            user6.setName(name);
            user6.setScore(totalScore);
            users.add(user1);
            users.add(user2);
            users.add(user3);
            users.add(user4);
            users.add(user5);
            users.add(user6);
            users.add(user7);
            users.add(user8);
            users.add(user9);
            users.add(user10);
        }
        else if (ranking_name[7].equals(name)) {
            user7.setName(name);
            user7.setScore(totalScore);
            users.add(user1);
            users.add(user2);
            users.add(user3);
            users.add(user4);
            users.add(user5);
            users.add(user6);
            users.add(user7);
            users.add(user8);
            users.add(user9);
            users.add(user10);
        }
        else if (ranking_name[8].equals(name)) {
            user8.setName(name);
            user8.setScore(totalScore);
            users.add(user1);
            users.add(user2);
            users.add(user3);
            users.add(user4);
            users.add(user5);
            users.add(user6);
            users.add(user7);
            users.add(user8);
            users.add(user9);
            users.add(user10);
        }
        else if (ranking_name[9].equals(name)) {
            user9.setName(name);
            user9.setScore(totalScore);
            users.add(user1);
            users.add(user2);
            users.add(user3);
            users.add(user4);
            users.add(user5);
            users.add(user6);
            users.add(user7);
            users.add(user8);
            users.add(user9);
            users.add(user10);
        }
        else if (ranking_name[10].equals(name)) {
            user10.setName(name);
            user10.setScore(totalScore);
            users.add(user1);
            users.add(user2);
            users.add(user3);
            users.add(user4);
            users.add(user5);
            users.add(user6);
            users.add(user7);
            users.add(user8);
            users.add(user9);
            users.add(user10);
        }

        Collections.sort(users);

        for(int i = 0;i < users.size(); i ++){
            String name = "top"+String.valueOf(users.size()-i)+"_name";
            String score = "top"+String.valueOf(users.size()-i)+"_score";
            editorRanking.putString(name,users.get(i).getName());
            editorRanking.putInt(score,users.get(i).getScore());
        }

        editorRanking.commit();




        //-----------------------------------------------------------------------------------------------------
        // A WTF way for sorting,
        // if the user score >=top1 then user become top1
        // original 1->2 2->3 3->4 4>5......
        //if the user score <top1 and >=top2,then user become top2
        // original 2->3 3->4 4->5.......

   }




}