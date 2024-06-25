package com.example.class24b_and_hw1;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

public class GameManager {

    private int lives=3;
    private int rows = 5;
    private int cols;
    private LinearLayoutCompat mainLinearLayout;
    private LinearLayoutCompat gameMatrix [][];
    private AppCompatImageView imgPlayer;
    private Context context ;

    public GameManager(Context context,LinearLayoutCompat mainLinearLayout,int initialLives, int cols, AppCompatImageView imgPlayer){
        if(initialLives >0 && initialLives<=4){
            this.lives = initialLives;
        }
        this.cols = cols;
        this.context=context;
        this.gameMatrix = new LinearLayoutCompat[rows][cols];
        this.mainLinearLayout=mainLinearLayout;
        this.imgPlayer = imgPlayer;
        initLayout();
    }

    public int getLives(){
        return lives;
    }

    public void decreaseLives(){
        lives--;
    }

    public void resetLives(int livesQuantity){
        if(livesQuantity >0 && livesQuantity<=4){
            lives = livesQuantity;
        }
        else {lives=3;}

    }

    public LinearLayoutCompat[][] getGameMatrix(){
        return gameMatrix;
    }

    private void initLayout(){

        for (int row = 0; row < rows; row++) {
            // Create a horizontal LinearLayout for each row
            LinearLayoutCompat rowLinearLayout = new LinearLayoutCompat(context);
            rowLinearLayout.setOrientation(LinearLayoutCompat.HORIZONTAL);
            LinearLayoutCompat.LayoutParams rowLayoutParams = new LinearLayoutCompat.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,1
            );
            rowLinearLayout.setLayoutParams(rowLayoutParams);

            for (int col = 0; col < cols; col++) {
                addLinearLayoutToCell(row,col,rowLinearLayout);
            }
            mainLinearLayout.addView(rowLinearLayout);
        }
        int startCol =(int)Math.floor(cols/2);
        if (imgPlayer.getParent() != null) {
            ((ViewGroup) imgPlayer.getParent()).removeView(imgPlayer);
        }
        gameMatrix[rows - 1][startCol].addView(imgPlayer);
    }

    private void addLinearLayoutToCell(int row, int col, LinearLayoutCompat rowLinearLayout){

        gameMatrix[row][col] = new LinearLayoutCompat(context);
        gameMatrix[row][col].setOrientation(LinearLayoutCompat.VERTICAL);
        gameMatrix[row][col].setGravity(Gravity.CENTER);
        LinearLayoutCompat.LayoutParams linearLayoutParams = new LinearLayoutCompat.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1
        );
        gameMatrix[row][col].setLayoutParams(linearLayoutParams);
        rowLinearLayout.addView(gameMatrix[row][col]);
    }

    public void goRight(Player p ){
        int newCol = p.getCurrentCol()+1;
        int curRow = p.getRow();
        setImgPlayerPosition(p,curRow,newCol);

    }

    public void goLeft(Player p ){
        int newCol = p.getCurrentCol()-1;
        int curRow = p.getRow();
        setImgPlayerPosition(p,curRow,newCol);
    }

    void setImgPlayerPosition(Player p, int row, int newCol){
        if(newCol<0 || newCol>=cols){
            return;
        }
        if (imgPlayer.getParent() != null) {
            ((ViewGroup) imgPlayer.getParent()).removeView(imgPlayer);
        }
        gameMatrix[row][newCol].addView(imgPlayer);
        p.setPosition(row,newCol);
    }


}
