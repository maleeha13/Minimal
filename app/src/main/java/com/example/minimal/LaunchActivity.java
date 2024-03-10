package com.example.minimal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Activity for the initial launcher screen
 */
public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    /**
     * Opens the start screen upon the click of he button
     * @param v
     */
    public void beginButton(View v){
        Intent intent = new Intent(LaunchActivity.this, StartScreen.class);
        startActivity(intent);
    }
}