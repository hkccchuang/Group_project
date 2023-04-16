package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Stage5 extends Stage implements SensorEventListener {//should change the name when copy

    ImageButton btnBack,btnHint,btnRestart;
    Animation rotate,toLeft;

    Button win;

    TextView title;

    Timer timer=new Timer();

    private SensorManager sensorManager;
    private  Sensor mySensor;
    private long lastUpdate,actualTime;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage5);//change here
        loadUI();
        loadAnimation();
        loadSound();
        startChronometer();
        lastUpdate=System.currentTimeMillis();
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(mySensor==null){
            Toast.makeText(this,"no",Toast.LENGTH_LONG).show();
            finish();
        }else{
            sensorManager.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
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




        btnBack.setOnClickListener(this);
        btnHint.setOnClickListener(this);
        btnRestart.setOnClickListener(this);


        scoreNumber="score5";//score number score+12345678910
        stageNumber=5;//stage number stage+12345678910 //should change the name when copy
    }


    public void onClick(View view) {

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

                giveHint("literally");

            }
            else if(view.getId()==R.id.btnRestart){
                resetStage();
            }


        }

    }

    @Override
   protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(this);

               }
     @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float[] values=sensorEvent.values;
            float x= values[0];
            float y= values[1];
            float z= values[2];

            float EG=SensorManager.GRAVITY_EARTH;
            float devAccel=(x*x+y*y+z*z)/(EG*EG);
            if(devAccel>=1.5){
                actualTime=System.currentTimeMillis();
                if((actualTime-lastUpdate)>1000){
                   lastUpdate=actualTime;
                    sensorManager.unregisterListener(this);
                    beforeNextStage();
                    intent=new Intent(Stage5.this,Stage6.class);//Next level!
                    nextStageDialog();

                }

            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}