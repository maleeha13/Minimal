package com.example.minimal;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents an instance of the game
 */
public class Game {

    /** Turns of the game */
    int[] turns = {1, 2, 3, 4};

    /** Scores of player for each round */
    static int[][] scores;

    /** Whether game has started */
    boolean begin = true;

    /** SKeeps track of current player in the game */
    int current_player=0;

    /** Whether a card has been dropped at the player's turn */
    boolean dropped = false;

    /** Whether a card has been picked at a player's turn */
    boolean picked = true;

    /** The ID of the card clicked by the player */
    int selectedCardId;

    /** List of cards dropped by the player */
    ArrayList<ImageView> cardsSelected = new ArrayList<>();

    /** The current card that has been dropped */
    ImageView currentCard;

    /** The previous card that has been dropped on the pile */
    Drawable previous ;

    /** Drawable of the latest card dropped */
    Drawable current ;

    /** Imageview of the previous card dropped on the pile */
    ImageView pre;

    /** Tag of the previous card */
    int check;

    /** Tag of current card */
    int second;

    /** Imageview of the deck */
    ImageView iv_deck;

    /** Image view of the stack/pile */
    ImageView stack;

    /** A map that keeps track of the cards a in a player's hand */
    static Map<Integer, List<Integer>> playerHand = new  HashMap<>();

    /** Keeps track of the cards in the deck */
    int x = 0;

    /** Imageview of the card that has been dropped */
    ImageView droppedCard;

    /** Whether any player has clicked show */
    boolean show = false;


}
