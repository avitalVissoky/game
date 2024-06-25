 package com.example.class24b_and_hw1;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

 public class MainActivity extends AppCompatActivity {


     int rows= 5;
     int cols = 5;
     private com.example.class24b_and_hw1.GameManager gameManager;
     private AppCompatImageView img_go_left;
     private AppCompatImageView img_go_right;
     private AppCompatImageView img_player;
     private LinearLayoutCompat mainLinearLayout;
     private LinearLayoutCompat[][] linearMat;
     private AppCompatImageView [] img_Katanas_live;
     private Player p;
     private MediaPlayer mediaPlayer;





     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViews();
        int startCol =(int)Math.floor(cols/2);
        p = new Player(rows-1,startCol,linearMat, img_player);

        gameManager = new com.example.class24b_and_hw1.GameManager(this,mainLinearLayout,3,cols,p.getImg());
        update_lives_ui();
        mediaPlayer= MediaPlayer.create(this,R.raw.sword_sound);
        linearMat = gameManager.getGameMatrix();



        //initLayout(mainLinearLayout ,linearMat, imgPlayer);

        img_go_right.setOnClickListener(view ->{goRight(p);} );
        img_go_left.setOnClickListener(view ->{goLeft(p);} );

        startObstaclesInterval();
    }

    private void startObstaclesInterval(){
    Timer timer = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(() -> initObstacle());
        }
    };
    timer.schedule(timerTask, 1000,2000);
    }

    private void update_lives_ui(){
        int SZ = img_Katanas_live.length;
        for(int i = 0 ; i<SZ ; i++){
            img_Katanas_live[i].setVisibility(View.VISIBLE);
        }
        for( int i = 0 ; i<SZ - gameManager.getLives(); i++){
            img_Katanas_live[SZ-i-1].setVisibility(View.INVISIBLE);
        }
    }

    private void findViews(){
        mainLinearLayout= findViewById(R.id.mainLinearLayout);
        img_go_left = findViewById(R.id.img_go_left);
        img_go_right = findViewById(R.id.img_go_right);
        img_player = findViewById(R.id.img_player);
        //imgCrashPlayer=findViewById(R.id.playerCrashImg);
        img_Katanas_live = new AppCompatImageView[]{
                findViewById(R.id.img_katana1),
                findViewById(R.id.img_katana2),
                findViewById(R.id.img_katana3),
                findViewById(R.id.img_katana4)
        };
    }

    void initObstacle(){
        int randCol = (int)Math.floor(Math.random()*(cols));
        AppCompatImageView imgView = new AppCompatImageView(this);
        imgView.setImageResource(R.drawable.ic_demon);

        Obstacle obstacle = new Obstacle(0,randCol,linearMat,imgView);
        setImgObstaclePosition(obstacle,0,randCol);

        moveObstacleDownWithInterval(obstacle,1000);
    }

     void moveObstacleDownWithInterval(Obstacle obstacle, long intervalMillis) {
         Timer timer = new Timer();
         TimerTask task = new TimerTask() {
             @Override
             public void run() {
                     runOnUiThread(() -> moveObstacleDown(obstacle,timer));
             }
         };
         timer.schedule(task,intervalMillis ,intervalMillis); // Schedule to run every intervalMillis milliseconds
     }

    void moveObstacleDown(Obstacle obstacle,Timer timer){
        int newRow= obstacle.getRow()+1;
        if (newRow == rows-1){
            obstacle.getImg().setVisibility(View.INVISIBLE);
            timer.cancel();
            ((ViewGroup) obstacle.getImg().getParent()).removeView(obstacle.getImg());
            if(checkIfCrash(obstacle)){
                onCrash();
            }
            return;
        }
        setImgObstaclePosition(obstacle,newRow,obstacle.getCol());
    }

     boolean checkIfCrash(Obstacle obstacle){
        return (obstacle.getCol() == p.getCurrentCol());
     }

     void onCrash(){
        mediaPlayer.start();

         //Toast.makeText(this,"crash!", Toast.LENGTH_SHORT).show();
         Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
         } else {
             v.vibrate(500);
         }
         img_player.setImageResource(R.drawable.ic_katana);
         img_player.setVisibility(View.VISIBLE);
         Timer timer = new Timer();
         TimerTask timerTask = new TimerTask() {
             @Override
             public void run() {
                 runOnUiThread(() -> {
                     img_player.setImageResource(R.drawable.ic_tanjiro_kamado_head);
                     img_player.setVisibility(View.VISIBLE);
                     timer.cancel();
                 });
             }
         };
         timer.schedule(timerTask, 500,3000);
        gameManager.decreaseLives();
        update_lives_ui();
         if(gameManager.getLives()==0){
             gameOver();
         }
     }

     void gameOver(){
         Toast.makeText(this,"you lose", Toast.LENGTH_SHORT).show();
         gameManager.resetLives(3);
         update_lives_ui();
         //finish();
     }

    void goRight(Player p ){
        int newCol = p.getCurrentCol()+1;
        int curRow = p.getRow();
        setImgPlayerPosition(p,curRow,newCol);

    }

     void goLeft(Player p ){
         int newCol = p.getCurrentCol()-1;
         int curRow = p.getRow();
         setImgPlayerPosition(p,curRow,newCol);
     }

    void setImgObstaclePosition(Obstacle obstacle, int newRow, int newCol){
        if (obstacle.getImg().getParent() != null) {
            ((ViewGroup) obstacle.getImg().getParent()).removeView(obstacle.getImg());
        }
        linearMat[newRow][newCol].addView(obstacle.getImg());
        obstacle.setPosition(newRow, newCol);
    }

    void setImgPlayerPosition(Player p, int row, int newCol){
        if(newCol<0 || newCol>=cols){
            return;
        }
        if (img_player.getParent() != null) {
            ((ViewGroup) img_player.getParent()).removeView(img_player);
        }
        linearMat[row][newCol].addView(img_player);
        p.setPosition(row,newCol);
    }
 }