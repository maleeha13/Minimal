package com.example.minimal;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MinimizeAI {


    boolean isClicked = false;
    boolean isDropped = false;
    boolean isPicked = true;

    private CountDownTimer clickTimer;

    public void minimizeAI(List<ImageView> playerHand, ImageView img, Boolean pickFromStack, Game game, Button dropButton, ImageView stackImage, String source) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {


                for (ImageView imageView : playerHand) {

                        imageView.performClick();



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










        //        System.out.println("-----------------------");
//        System.out.println("player iss " + game.current_player);
//
//        if (imageViewsList.size() > 0) {
//            for (List<ImageView> imageViewList : imageViewsList) {
//                for (ImageView imageView : imageViewList) {
//                    imageView.performClick();
//
//                    System.out.println("click ");
//                }
//            }
//        } else {
//            // If no image views to click, perform the initial click
//            img.performClick();
//            System.out.println("click 111111");
//        }
//
//
//        dropButton.performClick();
//        if (Objects.equals(source, "deck")) {
//            game.iv_deck.performClick();
//            System.out.println(" deck ");
//        } else {
//            game.stack.performClick();
//            System.out.println(" stack ");
//        }

//
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                if(isPicked){
//
//                    // Click the first set of image views if available
//                if (imageViewsList.size() > 0) {
//                    for (List<ImageView> imageViewList : imageViewsList) {
//                        for (ImageView imageView : imageViewList) {
//                            imageView.performClick();
//                            System.out.println("click ");
//                            isClicked = true;
//                            isPicked=false;
//
//                        }
//                    }
//                } else {
//                    // If no image views to click, perform the initial click
//                    img.performClick();
//                    isClicked = true;
//                    isPicked=false;
//
//                    System.out.println("click 111111");
//                }
//            }
//                // Delay before clicking the drop button
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(isClicked) {
//
//                            dropButton.performClick();
//                            System.out.println(" drop ");
//                            isDropped=true;
//                            isPicked=false;
//                            isClicked=false;
//                        }
//                        // Delay before clicking the appropriate source
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(isDropped) {
//                                    if (Objects.equals(source, "deck")) {
//                                        game.iv_deck.performClick();
//                                        System.out.println(" deck ");
//                                        isPicked=true;
//                                        isDropped=false;
//                                        isClicked=false;
//
//                                    } else {
//                                        game.stack.performClick();
//                                        isPicked=true;
//                                        isDropped=false;
//                                        isClicked=false;
//
//                                        System.out.println(" stack ");
//                                    }
//                                }
//                            }
//                        }, 2000); // Delay for clicking the source button
//                    }
//                }, 2000); // Delay for clicking the drop button
//            }
//        });
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
