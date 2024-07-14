package com.example.class24b_and_hw1;

public class Record {

    private String name;
    private double latitude;
    private double longitude;
    private int score;

    public Record(String name, double latitude, double longitude, int score) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return name + " - " + latitude  + " - " + longitude+ " - " + score;
    }

}
