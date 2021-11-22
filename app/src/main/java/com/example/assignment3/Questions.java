package com.example.assignment3;

import android.content.Context;
import android.graphics.Color;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Questions {
    //This will keep track of the progress of the questions
    public int progress;

    public ArrayList<Integer> questions;

    //this will keep track of the correct answer for the question
    public String answer;

    //This will keep track of the selected question
    public int question;

    //Flag to see if activity is starting or recreating
    private boolean firstQuestion;

    //I need context to get String resource in order to change the language of the questions
    private final Context context;

    //Constructor - Setting the questions, context and the first Question flag
    public Questions(Context context){

        this.context = context;
        questions = new ArrayList<>();
        questions.add(R.string.question1);
        questions.add(R.string.question2);
        questions.add(R.string.question3);
        questions.add(R.string.question4);
        questions.add(R.string.question5);

        firstQuestion= true;
    }

    //This Function will Get a random question
    //Store it on question variable
    //remove it from Arraylist
    //Set the answer
   public void getQuestion(){

        int questionNumber = (int) (Math.random() * questions.size());
        if(questions.size()>0) {
            question = questions.get(questionNumber);
            questions.remove(questionNumber);
        }

        if(question == R.string.question1)
            answer = context.getString(R.string.falseButtom);
        if(question == R.string.question2)
            answer= context.getString(R.string.falseButtom);
        if(question == R.string.question3)
            answer= context.getString(R.string.trueButtom);
        if(question == R.string.question4)
            answer= context.getString(R.string.trueButtom);
        if(question == R.string.question5)
            answer= context.getString(R.string.trueButtom);
    }

    //This function Increase the Progress
    public int increaseProgress(){
        progress++;
        return progress;
    }

    //This function get the actual progress
    public int getProgress(){
        return progress;
    }

    //For choosing the number of questions to remove
//    public void chooseNumber(int number){
//        for(int i = 0; i< number ; i++){
//            int questionNumber = (int) (Math.random() * questions.size());
//            questions.remove(questionNumber);
//        }
//    }

    //This function is triggered when the activity first run
    //it will prevent the app to get a new question every time the activity is destroyed and recreated.
    public boolean firstQuestion() {
        if (firstQuestion) {
            firstQuestion = false;
            return true;
        }
       return false;
    }
}
