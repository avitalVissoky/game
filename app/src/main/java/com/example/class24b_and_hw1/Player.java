package com.example.class24b_and_hw1;

import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

public class Player {

    private LinearLayoutCompat[][] linearMat;
    private Position position;
    private AppCompatImageView img;

    Player (int row, int col, LinearLayoutCompat[][] linearMat, AppCompatImageView img){
        this.position = new Position(row, col);
        this.linearMat = linearMat;
        this.img=img;
    }

    public void setPosition(int row, int col){
        this.position.row = row;
        this.position.col=col;
    }

    public AppCompatImageView getImg(){
        return img;
    }
    public void setImg(AppCompatImageView newImg){
        if (newImg.getParent() != null) {
            ((ViewGroup) newImg.getParent()).removeView(newImg);
        }
        this.img= newImg;

    }

    public int getCurrentCol(){
        return position.col;
    }
    public int getRow(){
        return position.row;
    }

}
