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
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Stage2 extends Stage {//should change the name when copy

    ImageButton btnBack,btnHint,btnRestart;
    Animation rotate,toLeft;

   Button win;

    TextView title,answer;

    Timer timer=new Timer();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage2);//change here
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


        answer=findViewById(R.id.real);
        answer.setOnClickListener(this);



        btnBack.setOnClickListener(this);
        btnHint.setOnClickListener(this);
        btnRestart.setOnClickListener(this);


        scoreNumber="score2";//score number score+12345678910
        stageNumber=2;//stage number stage+12345678910 //should change the name when copy
    }


    public void onClick(View view) {

        if(!isFastDoubleClick()) {
            if(view.getId()==R.id.btnBack){ btnBack.startAnimation(rotate); chronometer.startAnimation(rotate);
                btnRestart.startAnimation(rotate);btnHint.startAnimation(rotate);title.startAnimation(toLeft);}//animation
            else if(view.getId()==R.id.real){
                beforeNextStage();
                intent=new Intent(Stage2.this,Stage3.class);//Next level!
                nextStageDialog();
            }


            TimerTask task = new TimerTask() {
                public void run() {


                    if(view.getId()==R.id.btnBack){
                        finish();
                    }//case back


                }
            };
            timer.schedule(task, 600);

            if (view.getId()==R.id.btnHint){

                giveHint("The size");

            }
            else if(view.getId()==R.id.btnRestart){
                resetStage();
            }


        }

    }


}