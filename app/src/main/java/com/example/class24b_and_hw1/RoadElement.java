package com.example.class24b_and_hw1;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

public class RoadElement {
    enum ElementType{
        OBSTACLE,
        COIN
    }
    private LinearLayoutCompat[][] linearMat;
    private Position position;
    private AppCompatImageView img;
    private ElementType type;
    private int intervalMillis;


    RoadElement(int row, int col, LinearLayoutCompat[][] linearMat, AppCompatImageView img,ElementType type){
        this.position = new Position(row, col);
        this.linearMat = linearMat;
        this.img = img;
        this.type=type;
        this.intervalMillis = 1000; //deafult interval
    }

    public void setIntervalMillis(int intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public int getIntervalMillis() {
        return intervalMillis;
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

    public ElementType getType() {
        return type;
    }
}
