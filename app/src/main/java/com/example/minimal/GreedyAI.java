package com.example.minimal;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

public class GreedyAI {




    public void greedyAI(int j, ImageView drop, Game game,Button dropButton) {

        // Check if the game is paused



        drop.performClick();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Perform the second click after a 2-second delay

                dropButton.performClick();
            }

        }, 500) ; // 2000 milliseconds = 2 seconds

        // Delay between the second and third clicks
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if the game is paused before performing the third click
                game.iv_deck.performClick();

            }
        }, 1000);
    }



}
