package com.corp.detective.core;

import android.support.v7.app.AppCompatActivity;

public class CurrentActivity extends AppCompatActivity {
    private static AppCompatActivity activity;

    public static AppCompatActivity getActivity() {
        return activity;
    }

    public static void setActivity(AppCompatActivity activity) {
        CurrentActivity.activity = activity;
    }
}
