 package com.example.class24b_and_hw1;

import android.content.Context;
import android.content.Intent;
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

import android.widget.TextView;
import android.widget.Toast;

import com.example.class24b_and_hw1.Interfaces.MoveCallBack;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

 public class PlayGameActivity extends AppCompatActivity {


     private int rows= 10;
     private int cols = 5;
     private com.example.class24b_and_hw1.GameManager gameManager;
     private AppCompatImageView img_go_left;
     private AppCompatImageView img_go_right;
     private AppCompatImageView img_player;
     private LinearLayoutCompat mainLinearLayout;
     private LinearLayoutCompat[][] linearMat;
     private AppCompatImageView [] img_Katanas_live;
     private TextView txt_score_count;
     private Player p;
     private MediaPlayer mediaPlayer;
     private MoveDetector moveDetector;
     private String playerName;
     private int gameSpeed;
     private int gameType;
     private double latitude;
     private double longitude;
     private ArrayList<Timer> timerList = new ArrayList<Timer>();



     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play_game);

        findViews();

        Intent prev = getIntent();
        Bundle prevBundle = prev.getExtras();
        playerName = prevBundle.getString("key_player_name");
        gameSpeed = prevBundle.getInt(Constants.KEY_SPEED);
        gameType = prevBundle.getInt(Constants.KEY_GAME_TYPE);
        latitude = prevBundle.getDouble(Constants.KEY_LATITUDE);
        longitude = prevBundle.getDouble(Constants.KEY_LONGITUDE);

        int startCol =(int)Math.floor(cols/2);
        p = new Player(rows-1,startCol,linearMat, img_player);

        gameManager = new com.example.class24b_and_hw1.GameManager(this,mainLinearLayout,3,cols,rows ,p.getImg());
        update_lives_ui();
        mediaPlayer= MediaPlayer.create(this,R.raw.sword_sound);
        linearMat = gameManager.getGameMatrix();

         if(gameType==2){
             initMoveDetector();
             img_go_left.setVisibility(View.INVISIBLE);
             img_go_right.setVisibility(View.INVISIBLE);
         }
             img_go_right.setOnClickListener(view ->{gameManager.goRight(p);} );
             img_go_left.setOnClickListener(view ->{gameManager.goLeft(p);} );

         startGame();
    }
    private void startGame(){
         startObstaclesInterval();
         startCoinsInterval();
    }

    private void initMoveDetector(){
         moveDetector = new MoveDetector(this, new MoveCallBack() {
             @Override
             public void moveRigthX() {

                 gameManager.goRight(p);
             }
             public void moveLeftX() {

                 gameManager.goLeft(p);
             }
             @Override
             public void moveY() {

             }
         });
        moveDetector.start();
    }


    @Override
    protected void onPause(){
        super.onPause();
        if(gameType==2){
            moveDetector.stop();
        }
        gameManager.pauseElements();
        for(int i = 0 ; i < timerList.size() ; i++){
            timerList.get(i).cancel();
       }

    }
    @Override
    protected void onRestart(){
         super.onRestart();
        if(gameType==2){
            moveDetector.start();
        }
        resumeElements();
        startGame();
    }

     public void resumeElements() {
         gameManager.setPaused(false);
         ArrayList<RoadElement> roadElementsCopy = new ArrayList<>(gameManager.getRoadElements());

         for (RoadElement element : roadElementsCopy) {
             moveElementDownWithInterval(element, element.getIntervalMillis());
         }
     }

     private void startCoinsInterval(){
         Timer timer = new Timer();
         timerList.add(timer);
         TimerTask timerTask = new TimerTask() {
             @Override
             public void run() {
                 runOnUiThread(() -> {
                     if (!gameManager.isPaused()) {
                         initCoin();
                     }
                 });
             }
         };
         if(gameSpeed ==2){
             timer.schedule(timerTask, 500,1000);

         }else{
             timer.schedule(timerTask, 2000,3000);
             }

     }

    private void startObstaclesInterval(){
        Timer timer = new Timer();
        timerList.add(timer);
        TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(() -> {
                if (!gameManager.isPaused()) {
                    initObstacle();
                }
            });
            }
        };
        if(gameSpeed ==2){
            timer.schedule(timerTask, 250,500);
        }else{
            timer.schedule(timerTask, 1000,2000);
        }
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
        txt_score_count = findViewById(R.id.txt_score_count);
        img_Katanas_live = new AppCompatImageView[]{
                findViewById(R.id.img_katana1),
                findViewById(R.id.img_katana2),
                findViewById(R.id.img_katana3),
                findViewById(R.id.img_katana4)
        };
    }

     void initCoin(){
         int randCol = (int)Math.floor(Math.random( )*(cols));
         boolean colIsTaken= isColOccupide(randCol);
            while(colIsTaken){
                randCol = (int)Math.floor(Math.random( )*(cols));
                colIsTaken = isColOccupide(randCol);
            }
         AppCompatImageView imgView = new AppCompatImageView(this);
         imgView.setImageResource(R.drawable.ic_coin);

         RoadElement coin = new RoadElement(0,randCol,linearMat,imgView, RoadElement.ElementType.COIN);
         setImgRoadElementPosition(coin,0,randCol);

         if(gameSpeed == 2){
             moveElementDownWithInterval(coin,250);
         }
         else{
             moveElementDownWithInterval(coin,1000);
         }
     }

    boolean isColOccupide(int col){
         if(linearMat[0][col].getChildAt(0)!= null){
             return true;
         }
         else return false;
    }

    void initObstacle(){
        int randCol = (int)Math.floor(Math.random()*(cols));
        boolean colIsTaken= isColOccupide(randCol);
        while(colIsTaken){
            randCol = (int)Math.floor(Math.random( )*(cols));
            colIsTaken = isColOccupide(randCol);
        }
        AppCompatImageView imgView = new AppCompatImageView(this);
        imgView.setImageResource(R.drawable.ic_demon);

        RoadElement obstacle = new RoadElement(0,randCol,linearMat,imgView, RoadElement.ElementType.OBSTACLE);
        setImgRoadElementPosition(obstacle,0,randCol);

        if(gameSpeed ==2){
            moveElementDownWithInterval(obstacle,250);
        }
        else{
            moveElementDownWithInterval(obstacle,1000);
        }
    }

     void moveElementDownWithInterval(RoadElement roadElement, int intervalMillis) {
         roadElement.setIntervalMillis(intervalMillis);
         gameManager.addRoadElement(roadElement);

         Timer timer = new Timer();
         timerList.add(timer);
         TimerTask task = new TimerTask() {
             @Override
             public void run() {
                 if (!gameManager.isPaused()) {
                     if (roadElement.getType() == RoadElement.ElementType.OBSTACLE) {
                         runOnUiThread(() -> moveObstacleDown(roadElement, timer));
                     } else if (roadElement.getType() == RoadElement.ElementType.COIN) {
                         runOnUiThread(() -> moveCoinDown(roadElement, timer));
                     }
                 }
             }
         };
         timer.schedule(task, intervalMillis, intervalMillis); // Schedule to run every intervalMillis milliseconds
     }

     void moveCoinDown(RoadElement coin,Timer timer){
         int newRow= coin.getRow()+1;
         if (newRow == rows-1){
             coin.getImg().setVisibility(View.INVISIBLE);
             timer.cancel();
             if (coin.getImg() != null && coin.getImg().getParent() != null) {
                 gameManager.removeRoadElement(coin);
                 ((ViewGroup) coin.getImg().getParent()).removeView(coin.getImg());
             }
             if(checkIfCellsAreEqual(coin)){
                 gameManager.addScore();
                 txt_score_count.setText(""+gameManager.getScore());
             }
             return;
         }
         setImgRoadElementPosition(coin,newRow,coin.getCol());
     }

    void moveObstacleDown(RoadElement obstacle, Timer timer){
            int newRow= obstacle.getRow()+1;
            if (newRow == rows-1){
                obstacle.getImg().setVisibility(View.INVISIBLE);
                timer.cancel();
                if (obstacle.getImg() != null && obstacle.getImg().getParent() != null) {
                    gameManager.removeRoadElement(obstacle);
                    ((ViewGroup) obstacle.getImg().getParent()).removeView(obstacle.getImg());
                }
                if(checkIfCellsAreEqual(obstacle)){
                    onCrash();
                }
                return;
            }
            setImgRoadElementPosition(obstacle,newRow,obstacle.getCol());
    }

     boolean checkIfCellsAreEqual(RoadElement roadElement){
        return (roadElement.getCol() == p.getCurrentCol());
     }

     void onCrash(){
        mediaPlayer.start();

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
         Intent i = new Intent(getApplicationContext(),RecordsActivity.class);
         gameManager.saveNewRecord(playerName,latitude, longitude ,gameManager.getScore());
         startActivity(i);
     }

    void setImgRoadElementPosition(RoadElement roadElement, int newRow, int newCol){
        if (roadElement.getImg().getParent() != null) {
            ((ViewGroup) roadElement.getImg().getParent()).removeView(roadElement.getImg());
        }
        linearMat[newRow][newCol].addView(roadElement.getImg());
        roadElement.setPosition(newRow, newCol);
    }
 }