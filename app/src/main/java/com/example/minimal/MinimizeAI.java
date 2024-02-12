package com.example.minimal;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MinimizeAI {

    public void minimizeAI(List<List<ImageView>> imageViewsList, ImageView img, Boolean pickFromStack, Game game, Button dropButton, ImageView stackImage, String source) {
        System.out.println("-----------------------");
        // Click the first set of image views
//        if (imageViewsList.size() < 1) {
//            img.performClick();
//            System.out.println("click 1");
//        } else {

//        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (List<ImageView> imageViewList : imageViewsList) {
                    for (ImageView imageView : imageViewList) {

                        System.out.println("cards is  " +  imageView.getTag());
                    }
                }

                System.out.println("size is " + imageViewsList.size() );
                if(imageViewsList.size()<1){
                    img.performClick();
                }
                else{
                    for (List<ImageView> imageViewList : imageViewsList) {
                        for (ImageView imageView : imageViewList) {
                            imageView.performClick();
                            System.out.println("click ");
                        }
                    }
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dropButton.performClick();
                    }
                }, 1000);

                if (Objects.equals(source, "deck")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            game.iv_deck.performClick();
                        }
                    }, 2000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            game.stack.performClick();


                        }
                    }, 1000);
                }
            }
        });
    }

    public void deckDrop(ImageView img, Button dropButton, Game game){


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Perform the second click after a 2-second delay

                dropButton.performClick();
            }

        }, 3000) ; // 2000 milliseconds = 2 seconds

        // Delay between the second and third clicks


    }
    public void show( List<ImageView> myCards, Game game, Button show) {
        int val = 0;
        for (ImageView imageView : myCards) {
            int tag = (Integer) imageView.getTag();
            val += tag % 100;
        }

        if (val < 6) {
            System.out.println("lesser than 6 can show ");
            show.performClick();
        }
        int[] handSums = new int[4]; // Create an array to store hand sums

        int index = 0;

        for (Map.Entry<Integer, List<Integer>> entry : game.playerHand.entrySet()) {
//            System.out.println(" enter");
            List<Integer> hand = entry.getValue();
            // Calculate sum of the player's hand
            int sum = 0;
            for (Integer card : hand) {
//                System.out.println("add");
                sum += card % 100;
            }

            // Store the sum in the array
            handSums[index] = sum;
            index++;

        }
//        System.out.println("Hand sums:");
//        for (int sum : handSums) {
//            System.out.println(sum);
//        }
    }
}
