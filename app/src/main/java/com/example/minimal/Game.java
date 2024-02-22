package com.example.minimal;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    int[] turns = {1, 2, 3, 4};
//    Integer[] scores = new Integer[4];
    static int[][] scores; // Array to store scores for each player and round

    boolean begin = true;
    int current_player=0;
    boolean dropped = false;
    boolean picked = true;
    int selectedCardId;
    ArrayList<ImageView> cardsSelected = new ArrayList<>();
    ImageView currentCard;
    Drawable previous ;
    Drawable current ;
    ImageView new_pr;
    ImageView pre;
    int check;
    int second;
    ImageView iv_deck;
    ImageView stack;
    static Map<Integer, List<Integer>> playerHand = new  HashMap<>();

    int x = 0;
    ImageView droppedCard;





}
