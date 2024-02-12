package com.example.minimal;

import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

public class MinimizeAI {

    public void minimizeAI(List<List<ImageView>> imageViewsList, ImageView img, Boolean pickFromStack, Game game, Button dropButton, ImageView stackImage){
        if(imageViewsList.size()<1){
            img.performClick();
        }

        else {
            for (List<ImageView> imageViewList : imageViewsList) {
                for (ImageView imageView : imageViewList) {
                    imageView.performClick();
                }
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Perform the second click after a 2-second delay
                dropButton.performClick();
            }

        }, 500) ; // 2000 milliseconds = 2 seconds


        if(!pickFromStack){

                // Delay between the second and third clicks
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Check if the game is paused before performing the third click
                        game.iv_deck.performClick();

                    }
                }, 1000);

            }




        else{

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Check if the game is paused before performing the third click
                    game.stack.performClick();

                }
            }, 1000);


        }
    }




    public void deckDrop(ImageView img, Button dropButton, Game game){


        // Delay between the second and third clicks


    }
}
