//package com.example.minimal;
//
//import android.os.Handler;
//import android.widget.Button;
//import android.widget.ImageView;
//
//public class GreedyAI {
//
//    public void greedyAI(int j) {
//        ImageView drop = null;
//        int largest = 0;
//
////        // Check if the game is paused
////        if (isPaused) {
////            return; // Exit the method if the game is paused
////        }
//
//        for (int i = 1; i <= 5; i++) {
//            int imageViewId = getResources().getIdentifier("iv_p" + j + "c" + i, "id", getPackageName());
//            ImageView img = findViewById(imageViewId);
//
//            int cardNumber = (int) img.getTag();
//            if ((cardNumber % 100) > largest) {
//                largest = cardNumber % 100;
//                drop = img;
//            }
//        }
//
//        drop.performClick();
//        Button dropButton = findViewById(R.id.drop); // Replace R.id.myButton with your actual button ID
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Perform the second click after a 2-second delay
//
//                dropButton.performClick();
//            }
//
//        }, 500); // 2000 milliseconds = 2 seconds
//
//        // Delay between the second and third clicks
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Check if the game is paused before performing the third click
//                game.iv_deck.performClick();
//
//            }
//        }, 1000);
//    }
//}
