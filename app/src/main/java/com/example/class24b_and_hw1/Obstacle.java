package com.example.class24b_and_hw1;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

public class Obstacle {
    private LinearLayoutCompat[][] linearMat;
    private Position position;
    private AppCompatImageView img;

    Obstacle (int row, int col, LinearLayoutCompat[][] linearMat, AppCompatImageView img){
        this.position = new Position(row, col);
        this.linearMat = linearMat;
        this.img = img;
    }

    public void setPosition(int row, int col){
        this.position.row = row;
        this.position.col=col;
    }

    public int getCol(){
        return position.col;
    }
    public int getRow(){
        return position.row;
    }
    public AppCompatImageView getImg(){
        return img;
    }
}
