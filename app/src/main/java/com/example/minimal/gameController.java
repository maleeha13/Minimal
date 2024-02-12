package com.example.minimal;

import static com.example.minimal.Game.scores;
import static com.example.minimal.StartScreen.currentRound;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class gameController {

    private GameUIListener gameUIListener;
    Game game;
    private Context context;

    public gameController(GameUIListener gameUIListener, Context context) {
        this.gameUIListener = gameUIListener;
        this.game = new Game();
        this.context = context;
        if (context==null){
            System.out.println("%%%%%%%%");
        }
    }




    public interface GameUIListener {

        void onCardClicked(int cardValue, ImageView imageView, int player);


        void onPileClick() throws CloneNotSupportedException;

        void onDeckClick() throws CloneNotSupportedException;

        void nextTurn() throws CloneNotSupportedException;


    }


    public void onDeckClick(ImageView img) {
        if (game.dropped && game.iv_deck.getVisibility() == View.VISIBLE) {
            ImageView imageView = img;

            if (imageView != null) {
                assign(imageView, game.turns[game.current_player]);
                game.second = game.check;
                game.dropped = false;
                game.picked = true;
            }
        }

    }


    public void onPileClick(ImageView imageView, ImageView stackImageView) {
        imageView.setImageDrawable(game.previous);
        imageView.setTag(game.second);
        imageView.setTag(game.second);
        stackImageView.setImageDrawable(game.current);
        game.second =game.check;
        game.dropped=false;
        game.picked=true;

    }




    public void nextTurn(){

        if (game.current_player == 3) {
            game.current_player = 0;
        } else {

            game.current_player = game.current_player + 1;

        }
        game.cardsSelected.clear();
        game.begin = false;

    }

    public void assign(ImageView imageView, int player) {


        Card.assignImages(Card.getCards().get(game.x), imageView);
        int store = game.x;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the card value based on the clicked ImageView
                int cardValue = Card.getCards().get(store);
                // Call the method to handle the card click event
                onCardClicked(cardValue, imageView, player);
            }
        });
    }

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
                // if its the players turn and theyve picked a card and its a new card or one w same value pick it
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
