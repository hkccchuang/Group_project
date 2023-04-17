package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Stage4 extends Stage {//should change the name when copy

    ImageButton btnBack,btnHint,btnRestart;
    Animation rotate,toLeft;

   ImageView egg;

    TextView title;

    Timer timer=new Timer();

    int count=0;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage4);//change here
        loadUI();
        loadAnimation();
        loadSound();
        startChronometer();
    }




    public void loadAnimation(){

        rotate= AnimationUtils.loadAnimation(this,R.anim.rotate_left);
        rotate.setAnimationListener(this);
        toLeft= AnimationUtils.loadAnimation(this,R.anim.left_right);
        toLeft.setAnimationListener(this);
    }

    public void loadUI(){
        chronometer=findViewById(R.id.chronometer);
        btnBack=findViewById(R.id.btnBack);
        btnHint=findViewById(R.id.btnHint);
        title=findViewById(R.id.stage_title);
        btnRestart=findViewById(R.id.btnRestart);
        egg=findViewById(R.id.egg);



        egg.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnHint.setOnClickListener(this);
        btnRestart.setOnClickListener(this);


        scoreNumber="score4";//score number score+12345678910
        stageNumber=4;//stage number stage+12345678910 //should change the name when copy
    }




    public void onClick(View view) {

        if(view.getId()==R.id.egg){count=count+1;}
        if(view.getId()==R.id.egg&&count==10){egg.setImageResource(R.drawable.brokenegg);}
        if(view.getId()==R.id.egg&&count==12){

            beforeNextStage();
            intent=new Intent(Stage4.this,Stage5.class);//Next level!
            nextStageDialog();}


        if(!isFastDoubleClick()) {


            if(view.getId()==R.id.btnBack){ btnBack.startAnimation(rotate); chronometer.startAnimation(rotate);
                btnRestart.startAnimation(rotate);btnHint.startAnimation(rotate);title.startAnimation(toLeft);}//animation



            TimerTask task = new TimerTask() {
                public void run() {


                    if(view.getId()==R.id.btnBack){
                        finish();
                    }//case back



                }
            };
            timer.schedule(task, 600);

            if (view.getId()==R.id.btnHint){

                giveHint("Hit the egg 10 times");

            }
            else if(view.getId()==R.id.btnRestart){
                resetStage();
            }


        }

    }


}