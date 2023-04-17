package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputFilter;
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

public class Login extends AppCompatActivity implements View.OnClickListener,Animation.AnimationListener{

    Intent intent;
    ImageView image;
    Button cancel,cancel2,confirm;
    EditText editUserName;
    Timer timer=new Timer();//Timer for show animation
    public   SoundPool sp;//sound effect
    int soundEffect;

    String username;

    private static long lastClickTime = 0;

    Animation toLeft,toRight,fadeOut;

    Boolean isNext = false;

    SharedPreferences userInfo,lockCheck;//user information storage
    SharedPreferences.Editor editor,checkEditor;

    boolean isDelete=false;

    String ranking_name[]=new String[11]; // start
    int ranking_score[]=new int[11];
    String name;
    SharedPreferences.Editor editorRanking,lockEditor;
    public SharedPreferences userScore,userRanking,stageLock;
    int totalScore=0;;//score storage
//end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        loadUI();
        loadSound();
        loadAnimation();
        isDelete=false;
        upload();
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

        userInfo=getSharedPreferences("userName",MODE_PRIVATE);

        editor=userInfo.edit();





    }

    private void loadSound(){
        sp= new SoundPool(1000, AudioManager.STREAM_SYSTEM, 5);
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

        stageLock=getSharedPreferences(editUserName.getText().toString(),MODE_PRIVATE);

        if(!isFastDoubleClick()){

            sp.play(soundEffect, 0.3f, 0.3f, 0, 0, 1);//confirm.mp3

            if (view.getId() == R.id.btnCancel||view.getId()==R.id.btnCancel2)
            {image.startAnimation(toRight);editUserName.startAnimation(toRight);}
            if(view.getId()==R.id.btnConfirm&&
                    editUserName.getText().toString().isEmpty()!=true)
            {image.startAnimation(toLeft);editUserName.startAnimation(toLeft);}//animation

            TimerTask task = new TimerTask() {
                public void run() {

                    if(view.getId()==R.id.btnCancel||view.getId()==R.id.btnCancel2){

                        finish();//Go back to main menu

                    }

                    else if(isExist(editUserName.getText().toString())&&userInfo.getString("name","0")!=editUserName.getText().toString()){
                        editor.clear();
                        editor.commit();
                        upload();
                        sorting_UpLoad();
                        downLoad(editUserName.getText().toString());
                        finish(); //case 1
                    }
                    else if(view.getId()!=R.id.btnCancel&&!isExist(editUserName.getText().toString())){
                        editUserName.setText("");
                        editUserName.setHint("Not exist or already login");
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
        if (0 < timeD && timeD < 800) {
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

    protected void onDestroy(){
        super.onDestroy();
        sp.release();//release sound pool
    }

    public void downLoad(String name){

        lockCheck=getSharedPreferences(name,MODE_PRIVATE);


        if(lockCheck.getString("name","0")!="0"){
        editor.putString("name",lockCheck.getString("name","0"));}


        if(lockCheck.getInt("score1",0)!=0){
            editor.putInt("score1",lockCheck.getInt("score1",0));}

        if(lockCheck.getInt("score2",0)!=0){
            editor.putInt("score2",lockCheck.getInt("score2",0));}

        if(lockCheck.getInt("score3",0)!=0){
            editor.putInt("score3",lockCheck.getInt("score3",0));}

        if(lockCheck.getInt("score4",0)!=0){
            editor.putInt("score4",lockCheck.getInt("score4",0));}

        if(lockCheck.getInt("score5",0)!=0){
            editor.putInt("score5",lockCheck.getInt("score5",0));}

        if(lockCheck.getInt("score6",0)!=0){
            editor.putInt("score6",lockCheck.getInt("score6",0));}

        if(lockCheck.getInt("score7",0)!=0){
            editor.putInt("score7",lockCheck.getInt("score7",0));}

        if(lockCheck.getInt("score8",0)!=0){
            editor.putInt("score8",lockCheck.getInt("score8",0));}

        if(lockCheck.getInt("score9",0)!=0){
            editor.putInt("score9",lockCheck.getInt("score9",0));}

        editor.commit();
    }

    public boolean isExist(String name){
        lockCheck=getSharedPreferences(name,MODE_PRIVATE);
        if(lockCheck.getString("name","0")!="0")return true;
        else return false;
    }

    public void sorting_UpLoad(){//topx_score the key of preferences for score storage,
        // topx_name is the key of preferences for name storage
        // userName store the user data which are using the app now score1-10 for each stage score
        //ranking store all the past user data,top1-10_ for top10 user

        userScore=getSharedPreferences("userName",MODE_PRIVATE);
        userRanking=getSharedPreferences("ranking",MODE_PRIVATE);

        editorRanking=userRanking.edit();
        editor=userScore.edit();
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


        if(userRanking.getInt("top1_score", 0) <= totalScore&&userRanking.getString("top1_name","0").equals(name)){
            editorRanking.putInt("top1_score", totalScore);
            editorRanking.putString("top1_name", name);
        }
        else if(userRanking.getInt("top2_score", 0) <= totalScore&&userRanking.getString("top2_name","0").equals(name)){
            editorRanking.putInt("top2_score", totalScore);
            editorRanking.putString("top2_name", name);
        }
        else if(userRanking.getInt("top3_score", 0) <= totalScore&&userRanking.getString("top3_name","0").equals(name)){
            editorRanking.putInt("top3_score", totalScore);
            editorRanking.putString("top3_name", name);
        }
        else if(userRanking.getInt("top4_score", 0) <= totalScore&&userRanking.getString("top4_name","0").equals(name)){
            editorRanking.putInt("top4_score", totalScore);
            editorRanking.putString("top4_name", name);
        }
        else if(userRanking.getInt("top5_score", 0) <= totalScore&&userRanking.getString("top5_name","0").equals(name)){
            editorRanking.putInt("top5_score", totalScore);
            editorRanking.putString("top5_name", name);
        }
        else if(userRanking.getInt("top6_score", 0) <= totalScore&&userRanking.getString("top6_name","0").equals(name)){
            editorRanking.putInt("top6_score", totalScore);
            editorRanking.putString("top6_name", name);
        }
        else if(userRanking.getInt("top7_score", 0) <= totalScore&&userRanking.getString("top7_name","0").equals(name)){
            editorRanking.putInt("top7_score", totalScore);
            editorRanking.putString("top7_name", name);
        }
        else if(userRanking.getInt("top8_score", 0) <= totalScore&&userRanking.getString("top8_name","0").equals(name)){
            editorRanking.putInt("top8_score", totalScore);
            editorRanking.putString("top8_name", name);
        }
        else if(userRanking.getInt("top9_score", 0) <= totalScore&&userRanking.getString("top9_name","0").equals(name)){
            editorRanking.putInt("top9_score", totalScore);
            editorRanking.putString("top9_name", name);
        }
        else if(userRanking.getInt("top10_score", 0) <= totalScore&&userRanking.getString("top10_name","0").equals(name)){
            editorRanking.putInt("top10_score", totalScore);
            editorRanking.putString("top10_name", name);
        }
        else if (userRanking.getInt("top1_score", 0) <= totalScore) {

            if(!userRanking.getString("top1_name","0").equals(name)){
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

            if(!userRanking.getString("top2_name","0").equals(name)){
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

            if(!userRanking.getString("top3_name","0").equals(name)){
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

            if(!userRanking.getString("top4_name","0").equals(name)){
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

            if(!userRanking.getString("top5_name","0").equals(name)){
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

            if(!userRanking.getString("top6_name","0").equals(name)){
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

            if(!userRanking.getString("top7_name","0").equals(name)){
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

            if(!userRanking.getString("top8_name","0").equals(name)){
                editorRanking.putInt("top9_score", ranking_score[8]);
                editorRanking.putString("top9_name", ranking_name[8]);}

            if(userRanking.getInt("top10_score",0)!=0){
                editorRanking.putInt("top10_score", ranking_score[9]);
                editorRanking.putString("top10_name", ranking_name[9]);}

            editorRanking.putInt("top8_score", totalScore);
            editorRanking.putString("top8_name", name);
        } else if (userRanking.getInt("top9_score", 0) <= totalScore) {



            if(!userRanking.getString("top9_name","0").equals(name)){
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

    public void upload(){

        stageLock=getSharedPreferences(userInfo.getString("name",""),MODE_PRIVATE);
        lockEditor=stageLock.edit();

        if(userInfo.getString("name","0")!="0"){
            lockEditor.putString("name",userInfo.getString("name","0"));} //upload a file by using the user name

        if(userInfo.getInt("score1",0)!=0){//upload score data
            lockEditor.putInt("score1",userInfo.getInt("score1",0));
        }
        if(userInfo.getInt("score2",0)!=0){//upload score data
            lockEditor.putInt("score2",userInfo.getInt("score2",0));
        }
        if(userInfo.getInt("score3",0)!=0){//upload score data
            lockEditor.putInt("score3",userInfo.getInt("score3",0));
        }
        if(userInfo.getInt("score4",0)!=0){//upload score data
            lockEditor.putInt("score4",userInfo.getInt("score4",0));
        }
        if(userInfo.getInt("score5",0)!=0){//upload score data
            lockEditor.putInt("score5",userInfo.getInt("score5",0));
        }
        if(userInfo.getInt("score6",0)!=0){//upload score data
            lockEditor.putInt("score6",userInfo.getInt("score6",0));
        }
        if(userInfo.getInt("score7",0)!=0){//upload score data
            lockEditor.putInt("score7",userInfo.getInt("score7",0));
        }
        if(userInfo.getInt("score8",0)!=0){//upload score data
            lockEditor.putInt("score8",userInfo.getInt("score8",0));
        }
        if(userInfo.getInt("score9",0)!=0){//upload score data
            lockEditor.putInt("score9",userInfo.getInt("score9",0));
        }
        if(userInfo.getInt("score10",0)!=0){//upload score data
            lockEditor.putInt("score10",userInfo.getInt("score10",0));
        }

        lockEditor.commit();
    }
}