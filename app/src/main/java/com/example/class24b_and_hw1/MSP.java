package com.example.class24b_and_hw1;

import android.content.Context;
import android.content.SharedPreferences;

public class MSP {

    private static MSP msp;
    private SharedPreferences pref;


    private MSP(Context context){
        pref = context.getSharedPreferences("MySharePreference",Context.MODE_PRIVATE);

    }

    public static void init(Context context){
        if(msp==null){
            msp = new MSP(context);
        }
    }

    public static MSP getInstance(){
        return msp;
    }

    public int readInt(String key, int defVal){
        return pref.getInt(key,defVal);
    }

    public String readString(String key, String defVal){
        return pref.getString(key,defVal);
    }

    public void saveString(String key, String value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public void saveInt(String key, int value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key,value);
        editor.apply();
    }


}
