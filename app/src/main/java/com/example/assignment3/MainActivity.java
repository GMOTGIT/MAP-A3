package com.example.assignment3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    Questions questions;
    AlertDialog.Builder builder;
    int correctAnswers, total, totalCorrect;
    StorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Builder for the end of the quiz
        builder = new AlertDialog.Builder(this);

        progressBar = findViewById(R.id.ProgressBar);

        storageManager = ((myApp) getApplication()).getStorageManager();
        questions = ((myApp) getApplication()).getQuestions();

        FragmentManager fm = getFragmentManager();

        correctAnswers = 0;

        //On Create this app should add and display the first Fragment
        FragmentTransaction transaction = fm.beginTransaction();
        first_fragment firstFragment = new first_fragment();


        //Check if its first time or app activity has been destroyed and reconstructed
        if (questions.firstQuestion()) {
            questions.getQuestion();
        }

        //Send the question to the Fragment
        Bundle bundle = new Bundle();
        bundle.putInt("question", questions.question);
        firstFragment.setArguments(bundle);

        transaction.add(R.id.add_remove_area, firstFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

        //get progress bar
        progressBar.setProgress(questions.getProgress());

        //Get Stats from FS - if it exists
        File file = new File(getApplicationContext().getFilesDir(), "stats.txt");
        if (file.exists()) {
            convertStats();
        }else{
            //Create one
            storageManager.resetTheStorage(this);
        }
    }

    //EveryTime Button is clicked
    public void checkAnswer(View view) {
        TextView question = findViewById(R.id.first_fragmentText);
        //Check Answer Output
        //Update Counters
        //Change Background Color
        //Update Progess Bar
        //Display Message
        //Remove Old Fragment
        //Add New Fragment


        //First check if the run is over
        if (questions.progress >= 5) {
            displayResult();
            return;
        }

        //Getting the answer selected
        Button btn = findViewById(view.getId());
        String optionChoosed = btn.getText().toString();

        //Increase the Progress bar by 1
        progressBar.setProgress(questions.increaseProgress());

        // Checking Answer
        if (optionChoosed.equals(questions.answer)) {
            //Update Counters
            correctAnswers++;

            //set the bg color
            question.setBackgroundColor(Color.parseColor("#86DC3D"));

            //Display Message
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.correct,
                    Toast.LENGTH_SHORT);
            toast.show();

        } else {

            //set the color
            question.setBackgroundColor(Color.parseColor("#8E1600"));

            //Display Message
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.wrong,
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        if (questions.progress >= 5) {
            // setup the alert builder
            displayResult();
        }

        //Get new questions until all the questions
        if (questions.progress < 5) {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    this::nextQuestion,
                    1500);
        }
    }

    //This  function will:
    //Make a new Fragment
    //Set a new random question
    //replace the old fragment with new one
    public void nextQuestion() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        first_fragment firstFragment = new first_fragment();

        Bundle bundle = new Bundle();

        questions.getQuestion();
        bundle.putInt("question", questions.question);
        firstFragment.setArguments(bundle);

        transaction.replace(R.id.add_remove_area, firstFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    //Crating the Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quiz_menu, menu);
        return true;
    }

    //Setting Listeners to Menu options
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.average: {

                // setup the alert builder
                convertStats();

                builder.setTitle(R.string.your_average);
                builder.setMessage(totalCorrect + " / " + total);
                builder.setPositiveButton(R.string.Save, (dialog, which) -> {

                    storageManager.saveScore(MainActivity.this, total, totalCorrect);

                    //reset
                    questions = ((myApp) getApplication()).resetQuestions();
                    correctAnswers = 0;
                    progressBar.setProgress(0);
                    nextQuestion();

                    //toast
                    Toast toast = Toast.makeText(getApplicationContext(),
                            R.string.good_luck,
                            Toast.LENGTH_SHORT);
                    toast.show();
                });

                builder.setNegativeButton("Ignore", (dialog, which) -> {

                });

                AlertDialog dialog = builder.create();
                dialog.show();

                break;
            }
            case R.id.selectNumber: {
                //How to get input from Fragment?
//                EditText question = (EditText) v.findViewById(R.id.edittext);
//                String str = question.getText().toString();
//                questions.chooseNumber(Integer.parseInt(str));
                break;
            }
            case R.id.reset: {
                storageManager.resetTheStorage(MainActivity.this);
                break;
            }
        }
        return true;
    }


    //This Function will get Stats from File System and save them in variables so they can be updated
    //Because im using Private instead of Append, I need to keep track of them in the app in order to update them.
    public void convertStats() {
        String stats = storageManager.getStoredStats(MainActivity.this);
        String totalString = "";
        String totalCorrects = "";
        for (int i = 0; i < stats.toCharArray().length; i++) {
            if (stats.toCharArray()[i] == '/') {
                totalString = stats.substring(0, i);
                totalCorrects = stats.substring(i + 1);
            }
        }

        total = Integer.parseInt(totalString);
        totalCorrect = Integer.parseInt(totalCorrects);
    }

        //This Function will display an AlertDialog
        // This alertDialog will prompt the user to Save or Ignore
        // If user choose to Save, then his stats will be updated and the quiz will restart
        //if he choses to ignore then nothing happen :)
    public void displayResult() {
        builder.setTitle(R.string.result);
        builder.setMessage(correctAnswers + "/5");

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Save stats
            total = total + 5;
            totalCorrect = totalCorrect + correctAnswers;
            storageManager.saveScore(MainActivity.this, total, totalCorrect);

            //reset the quiz
            questions = ((myApp) getApplication()).resetQuestions();
            correctAnswers = 0;
            progressBar.setProgress(0);
            nextQuestion();

            //toast
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.good_luck,
                    Toast.LENGTH_SHORT);
            toast.show();
        });

        builder.setNegativeButton("Ignore", (dialog, which) -> {

        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}