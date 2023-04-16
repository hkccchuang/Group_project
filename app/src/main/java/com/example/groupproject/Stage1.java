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

           //sound
            if(id==R.id.btnBack){ btnBack.startAnimation(rotate); chronometer.startAnimation(rotate);
            btnRestart.startAnimation(rotate);btnHint.startAnimation(rotate);title.startAnimation(toLeft);
}//sound//animation
            else if(id==R.id.win){
                beforeNextStage();
                intent=new Intent(Stage1.this,Stage2.class);//Next level!
                nextStageDialog();
            }
            TimerTask task = new TimerTask() {
                public void run() {


                    if(id==R.id.btnBack){
                        finish();
                    }//case back


                }
            };
            timer.schedule(task, 500);

                if (id==R.id.btnHint){

                    giveHint("literally");

            }
                else if(id==R.id.btnRestart){
                    resetStage();
                }



        }

    }


}