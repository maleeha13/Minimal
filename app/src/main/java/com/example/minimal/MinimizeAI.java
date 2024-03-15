package com.example.minimal;

import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;
import java.util.Objects;

/**
 * This class implements the functions to carry out button clicks for the minimize AI
 */
public class MinimizeAI {


    /**
     *
     * @param playerHand - cards to be dropped
     * @param game - instance of the Game class
     * @param dropButton - drop button
     * @param source - pile or deck
     */
    public void minimizeAI(List<ImageView> playerHand, Game game, Button dropButton, String source) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                // Performs on card clicks
                for (ImageView imageView : playerHand) {
                    imageView.performClick();
                }

                // Drops the selected cards
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dropButton.performClick();
                    }
                }, 500);

                // Picks from deck
                if (Objects.equals(source, "deck")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            game.iv_deck.performClick();
                        }
                    }, 500);
                }
                // Picks from pile
                else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            game.stack.performClick();
                        }
                    }, 500);
                }
            }
        });

    }


    /**
     * Clicks the show button if score of player <= 5
     * @param myCards - cards of the player
     * @param show - show button
     */
    public void show( List<ImageView> myCards, Button show) {
        int val = 0;
        for (ImageView imageView : myCards) {
            int tag = (Integer) imageView.getTag();
            val += tag % 100;
        }

        if (val < 6) {
            show.performClick();
        }
    }
}
