package com.example.minimal;

import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

/**
 * This class is responsible for creating an instance of the Greedy AI
 */
public class GreedyAI {

    /**
     * Executes the movements of the AI such as card click, drop and pick
     * @param j
     * @param drop  - card to be dropped
     * @param game  - instance of the game
     * @param dropButton    - instance of the drop button
     */
    public void greedyAI(int j, ImageView drop, Game game,Button dropButton) {

        // Selects the card
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drop.performClick();
            }

        }, 500) ;

        // Drops the card
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dropButton.performClick();
            }

        }, 500) ;

        // Picks a card from the deck
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                game.iv_deck.performClick();

            }
        }, 500);
    }
    
}
