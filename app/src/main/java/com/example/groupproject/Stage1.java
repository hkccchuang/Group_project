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

public class Stage1 extends Stage {

    ImageButton btnBack,btnHint,btnRestart;
    Animation rotate,toLeft;

    Button win;

    TextView title;

    Timer timer=new Timer();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage1);
        loadUI();
        loadAnimation();
        loadSound();
        startChronometer();
    }

    public void loadSound(){
        sp= new SoundPool(1000, AudioManager.STREAM_SYSTEM, 5);
        soundEffect = sp.load(this, R.raw.wood_hit, 1);//confirm.mp3
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
        win=findViewById(R.id.win);


        btnBack.setOnClickListener(this);
        btnHint.setOnClickListener(this);
        btnRestart.setOnClickListener(this);
        win.setOnClickListener(this);
        scoreNumber="score1";//score number score+12345678910
        stageNumber=1;//stage number stage+12345678910
    }


    public void onClick(View view) {

        if(!isFastDoubleClick()) {

            int id=view.getId();

            if(id==R.id.btnBack||id==R.id.win){ btnBack.startAnimation(rotate); chronometer.startAnimation(rotate);
            btnRestart.startAnimation(rotate);btnHint.startAnimation(rotate);title.startAnimation(toLeft);}//animation

            sp.play(soundEffect, 0.3f, 0.3f, 0, 0, 1);//sound

            TimerTask task = new TimerTask() {
                public void run() {


                    if(id==R.id.btnBack){
                        finish();
                    }//case back
                    if(id==R.id.win){
                        beforeNextStage();
                        intent=new Intent(Stage1.this,Stage2.class);//Next level!
                        finish();
                        overridePendingTransition(0,0);//no animation for reset,can add a animation
                        startActivity(intent);
                    }




                }
            };
            timer.schedule(task, 600);

                if (id==R.id.btnHint){

                giveHint("literally");

            }
                else if(id==R.id.btnRestart){
                    resetStage();
                }


        }

    }


}