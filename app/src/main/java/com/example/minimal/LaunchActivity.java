package com.example.minimal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Activity for the initial launcher screen
 */
public class LaunchActivity extends AppCompatActivity {

    /**
     * Main launcher activity that displays the opening start screen of the game
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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

    /**
     * Begins the tutorial
     * @param v
     */
    public void startTut(View v){
        Intent intent = new Intent(LaunchActivity.this, Tutorial.class);
        startActivity(intent);
    }
}