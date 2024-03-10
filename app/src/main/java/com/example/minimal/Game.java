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

    int[] turns = {1, 2, 3, 4}; // Turns of the game
    static int[][] scores;      // Scores of player for each round
    boolean begin = true;       // Whether game has started
    int current_player=0;       // Keeps track of current player in the game
    boolean dropped = false;    // Whether a card has been dropped at the player's turn
    boolean picked = true;      // Whether a card has been picked at a player's turn
    int selectedCardId;         // The ID of the card clicked by the player
    ArrayList<ImageView> cardsSelected = new ArrayList<>(); // List of cards dropped by the player
    ImageView currentCard;      // The current card that has been dropped
    Drawable previous ;         // The previous card that has been dropped on the pile
    Drawable current ;          // Drawable of the latest card dropped
    ImageView pre;              // Imageview of the previous card dropped on the pile
    int check;                  // Tag of the previous card
    int second;                 // Tag of current card
    ImageView iv_deck;          // Imageview of the deck
    ImageView stack;            // Image view of the stack/pile
    static Map<Integer, List<Integer>> playerHand = new  HashMap<>();   // A map that keeps track of the cards a in a player's hand
    int x = 0;                  // Keeps track of the cards in the deck
    ImageView droppedCard;      // Imageview of the card that has been dropped
    boolean show = false;       // Whether any player has clicked show


}
