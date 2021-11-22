package com.example.assignment3;

import android.app.Application;

public class myApp extends Application {

        private final StorageManager storageManager = new StorageManager();
        private Questions questions = new Questions(this);

        public StorageManager getStorageManager() {
            return storageManager;
        }

    public Questions getQuestions() {
        return questions;
        }

    public Questions resetQuestions() {
        questions = new Questions(this);
        return questions;
        }
    }


