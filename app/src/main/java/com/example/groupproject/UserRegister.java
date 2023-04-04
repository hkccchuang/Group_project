package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class UserRegister extends AppCompatActivity implements View.OnClickListener,Animation.AnimationListener{

    Intent intent;
    ImageView image;
    Button cancel,cancel2,confirm;
    EditText editUserName;
    Timer timer=new Timer();//Timer for show animation
    public   SoundPool sp;//sound effect
    int soundEffect;

    private static long lastClickTime = 0;

    Animation toLeft,toRight,fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        getSupportActionBar().hide();
        loadUI();
        loadSound();
        loadAnimation();
    }


    private void loadUI(){
        cancel=findViewById(R.id.btnCancel);
        cancel2=findViewById(R.id.btnCancel2);
        confirm=findViewById(R.id.btnConfirm);
        editUserName=findViewById(R.id.editUserName);
        image=findViewById(R.id.imageRegister);


        cancel.setOnClickListener(this);
        cancel2.setOnClickListener(this);
        confirm.setOnClickListener(this);


    }

    private void loadSound(){
        sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundEffect = sp.load(this, R.raw.confirm, 1);//confirm.mp3
    }

    private void loadAnimation(){
        toRight= AnimationUtils.loadAnimation(this,R.anim.left_right);
        toRight.setAnimationListener(this);
        toLeft= AnimationUtils.loadAnimation(this,R.anim.right_left);
        toLeft.setAnimationListener(this);
        fadeOut=AnimationUtils.loadAnimation(this,R.anim.fade_out);
        fadeOut.setAnimationListener(this);
    }

    @Override
    public void onClick(View view) {

        if(!isFastDoubleClick()){

        sp.play(soundEffect, 1, 1, 0, 0, 1);//confirm.mp3

        if (view.getId() == R.id.btnCancel||view.getId()==R.id.btnCancel2)
        {image.startAnimation(toRight);editUserName.startAnimation(toRight);}
        if(view.getId()==R.id.btnConfirm&&editUserName.getText().toString().isEmpty()!=true)
        {image.startAnimation(toLeft);editUserName.startAnimation(toLeft);}//animation

        TimerTask task = new TimerTask() {
            public void run() {

                if(view.getId()==R.id.btnCancel||view.getId()==R.id.btnCancel2){
                    finish();//go back to main menu
                }
                else if(view.getId()==R.id.btnConfirm&&editUserName.getText().toString().isEmpty()!=true){
                    intent =new Intent(UserRegister.this,StageSelection.class);
                    finish();
                    startActivity(intent);//User name not null then go to stage selection
                }
                else if(editUserName.getText().toString().isEmpty()){
                    editUserName.setHint("Invalid");// hint for invalid name
                }

            }
        };
        timer.schedule(task, 200);}
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }//Ban back

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
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }// A function detected fast clicking,

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }//hide the keyboard when touch the screen

}