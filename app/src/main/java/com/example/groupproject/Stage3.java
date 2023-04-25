package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Stage3 extends Stage implements View.OnTouchListener {//should change the name when copy

    ImageButton btnBack,btnHint,btnRestart;
    Animation rotate,toLeft;

    Button win;

    TextView title;

    Timer timer=new Timer();
    EditText answer;
    ImageView apple,apple1,apple2;
    float x=0;
    float y=0;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage3);//change here
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
        answer=findViewById(R.id.answerEdit);
        apple1=findViewById(R.id.apple1);
        apple=findViewById(R.id.apple);



        apple1.setOnTouchListener(this);
        apple.setOnTouchListener(this);
        btnBack.setOnClickListener(this);
        btnHint.setOnClickListener(this);
        btnRestart.setOnClickListener(this);
        win.setOnClickListener(this);

        scoreNumber="score3";//score number score+12345678910
        stageNumber=3;//stage number stage+12345678910 //should change the name when copy
    }


    public void onClick(View view) {

        if(!isFastDoubleClick()) {


            if(view.getId()==R.id.btnBack){ btnBack.startAnimation(rotate); chronometer.startAnimation(rotate);
                btnRestart.startAnimation(rotate);btnHint.startAnimation(rotate);title.startAnimation(toLeft);}//animation

            try{
            if(view.getId()==R.id.win&&Integer.valueOf(answer.getText().toString())==3&&answer.getText()!=null){
                beforeNextStage();
                intent=new Intent(Stage3.this,Stage4.class);//Next level!
                nextStageDialog();
            }}
            catch (NumberFormatException e){}//successful case



            TimerTask task = new TimerTask() {
                public void run() {


                    if(view.getId()==R.id.btnBack){
                        finish();
                    }//case back





                }
            };
            timer.schedule(task, 600);

            if (view.getId()==R.id.btnHint){

                giveHint("Behind");

            }
            else if(view.getId()==R.id.btnRestart){
                resetStage();
            }


        }

    }




    @Override
    public boolean onTouch(View view, MotionEvent event) {



        if(event.getAction()==MotionEvent.ACTION_DOWN){
            x=event.getX();
            y=event.getY();
        }

        if(event.getAction()==MotionEvent.ACTION_MOVE){
            dx=event.getX()-x;
            dy=event.getY()-y;
            view.setX(view.getX()+dx);
            view.setY(view.getY()+dy);
            x=event.getX();
            y=event.getY();
        }

        return  true;
    }


}