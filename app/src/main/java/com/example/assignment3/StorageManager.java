package com.example.assignment3;

import android.app.Activity;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageManager {
    String filename = "stats.txt";


    //This Function will Reset the Database
    public void resetTheStorage(Activity activity){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = activity.openFileOutput(filename, Context.MODE_PRIVATE); //replace the values everytime
            fileOutputStream.write("0/0".getBytes());

        }catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try {
                assert fileOutputStream != null;
                fileOutputStream.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }



    //This Function will Save the updated Stats as a string
    //for exemple "10/20" - meaning 10 correct out of 20 tries
    public void saveScore(Activity activity, int total, int correctAnswers){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = activity.openFileOutput(filename, Context.MODE_PRIVATE); //replace the values everytime
            fileOutputStream.write((total+"/"+correctAnswers).getBytes());

        }catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try {
                assert fileOutputStream != null;
                fileOutputStream.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        // internal Stream

    }

    //This Function will return the stored stats as a string
    public String getStoredStats(Activity activity)  {
        FileInputStream fileInputStream = null;
        int read;
        String stats = "";
        StringBuilder buffer = new StringBuilder();
        try {
            fileInputStream = activity.openFileInput(filename);
            while(( read = fileInputStream.read() )!= -1 ){
                buffer.append((char)read);
            }
            stats  =  buffer.toString();
        }catch (IOException ex){ex.printStackTrace();}
        finally {
            try {
                assert fileInputStream != null;
                fileInputStream.close();
            }catch (IOException ex){ex.printStackTrace();}

        }
        return stats ;
    }

}
