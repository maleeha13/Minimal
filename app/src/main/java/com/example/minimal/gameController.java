package com.example.minimal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Collections;

public class gameController {

    private GameUIListener gameUIListener;
    private Game game;
    private Context context;

    public gameController(GameUIListener gameUIListener, Game game, Context context) {
        this.gameUIListener = gameUIListener;
        this.game = game;
        this.context = context;
    }



    public interface GameUIListener {

        void onCardClicked(int cardValue, ImageView imageView, int player);
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
