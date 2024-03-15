package com.example.minimal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Helps represent a custom toast
 */
public class custom_toast extends AppCompatActivity {

    /**
     * Creates a custom toast with message that a player has shown or deck is over
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_toast);
    }
}