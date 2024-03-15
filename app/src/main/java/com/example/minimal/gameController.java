package com.example.minimal;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is responsible for handling the logic that does not
 * involve UI in terms of card click, card drop and card picking
 */
public class gameController {

    private GameUIListener gameUIListener;      // UI listener
    Game game;                                  // Instance of the game
    private Context context;                    // Instance of teh context of main activity


    /**
     * Initilaizes gameUIlistener, context and game
     * @param gameUIListener
     * @param context
     */
    public gameController(GameUIListener gameUIListener, Context context) {
        this.gameUIListener = gameUIListener;
        this.game = new Game();
        this.context = context;
        if (context==null){
            System.out.println("Context is null");
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * The GameUIListener interface defines methods for handling user interface interactions in a game.
     * Implementations of this interface should provide functionality for responding to various
     * user actions such as clicking on cards, the pile, the deck, or advancing to the next turn.
     */
    public interface GameUIListener {

        /**
         * Called when a card is clicked.
         * @param cardValue The value of the clicked card.
         * @param imageView The ImageView associated with the clicked card.
         * @param player    The player who clicked the card.
         * @throws CloneNotSupportedException if cloning is not supported for any reason
         */
        void onCardClicked(int cardValue, ImageView imageView, int player) throws CloneNotSupportedException;

        /**
         * Called when the pile is clicked.
         * @throws CloneNotSupportedException if cloning is not supported for any reason
         */
        void onPileClick() throws CloneNotSupportedException;

        /**
         * Called when the deck is clicked.
         * @throws CloneNotSupportedException if cloning is not supported for any reason
         */
        void onDeckClick() throws CloneNotSupportedException;

        /**
         * Called when the game is ready to advance to the next turn.
         * @throws CloneNotSupportedException if cloning is not supported for any reason
         */
        void nextTurn() throws CloneNotSupportedException;
    }


    /**
     * Updates variables to update the state of the game
     * @param img the imageview where a card will be assigned
     */
    public void onDeckClick(ImageView img) {
        if (game.dropped && game.iv_deck.getVisibility() == View.VISIBLE) {
            // 4A. ASSIGNS A NEW CARD
            ImageView imageView = img;
            if (imageView != null) {
                assign(imageView, game.turns[game.current_player]);
                game.second = game.check;
                game.dropped = false;
                game.picked = true;
            }
        }
    }


    /**
     * Updates the tags and variables to update the state of the game
     * @param imageView the imageview where a card will be assigned
     * @param stackImageView the imageview of  the stack/pile
     */
    public void onPileClick(ImageView imageView, ImageView stackImageView) {
        // 4B. ASSIGNS THE PREVIOUS PLAYERS CARD
        imageView.setImageDrawable(game.previous);
        imageView.setTag(game.second);
        stackImageView.setImageDrawable(game.current);
        game.second =game.check;
        game.dropped=false;
        game.picked=true;

        List<Integer> playerHandList = new ArrayList<>();
        game.playerHand.get(game.current_player);
        playerHandList.add((Integer) imageView.getTag());
        game.playerHand.put(game.current_player, playerHandList);

    }


    /**
     * Updates the player whose turn it is next
     */
    public void nextTurn(){
        if (game.current_player == 3) {
            game.current_player = 0;
        }
        else {
            game.current_player = game.current_player + 1;

        }
        game.cardsSelected.clear();
        game.begin = false;

    }

    /**
     * Assigns a card to the imageview
     * @param imageView imageview to be assigned to
     * @param player    player whose card needs to be assigned
     */
    public void assign(ImageView imageView, int player) {


        Card.assignImages(Card.getCards().get(game.x), imageView);
        int store = game.x;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the card value based on the clicked ImageView
                int cardValue = Card.getCards().get(store);
                onCardClicked(cardValue, imageView, player);
            }
        });
    }

    /**
     * Function for on card click
     * @param cardValue the rank and suit of the card
     * @param imageView the imageview of the card
     * @param player    the player who clicked the card
     */
        public void onCardClicked(int cardValue, ImageView imageView, int player) {

            // if the the card is already selected - unselect it
            if ( game.cardsSelected.contains(imageView)) {
                game.cardsSelected.remove(imageView);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                params.topMargin += 50;
                imageView.setLayoutParams(params);
                game.currentCard=null;

            }
            else{
                int cardNumber = (int) imageView.getTag();
                int lastDigit = cardNumber % 100;
                ImageView v = null;
                int existing =-1;
                if(!game.cardsSelected.isEmpty()){
                    v = game.cardsSelected.get(0);
                    int x = (int) v.getTag();
                    existing = x % 100;
                }

                // if its the players turn and they've picked a card and its a new card or one w same value pick it
                if(player==game.turns[game.current_player] && game.picked == true &&(game.cardsSelected.isEmpty() || lastDigit==existing)){
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    game.cardsSelected.add(imageView);
                    params.topMargin -= 50;
                    imageView.setLayoutParams(params);
                    game.selectedCardId = imageView.getId();
                }
            }

        }


}
