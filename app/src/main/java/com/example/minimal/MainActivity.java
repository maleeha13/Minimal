
package com.example.minimal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {


    int[] turns = {1, 2, 3, 4};
    int current_player=0;
    boolean dropped = false;
    boolean picked = true;
    int selectedCardId;
    private ArrayList<ImageView> cardsSelected = new ArrayList<>();


    ImageView currentCard;
    Drawable previous ;
    Drawable current ;



    ImageView iv_deck;
    int x = 0;

    private void hideImageViewsRange(int start, String pre, int visibility) {
        for (int i = 1; i <= 5; i++) {
            int imageViewId = getResources().getIdentifier(pre+ start + "c" + i, "id", getPackageName());

            ImageView imageView = findViewById(imageViewId);

            if (imageView != null) {
                imageView.setVisibility(visibility);
            }

        }
    }

    private void assignCard(String pre, int start){

        for(int i=1; i<=5; i++){
            x++;
            if (x >= Card.getCards().size()) {

                iv_deck.setVisibility(View.INVISIBLE);
            }
            else{
                int imageViewId = getResources().getIdentifier(pre+ start + "c" + i, "id", getPackageName());
                ImageView imageView = findViewById(imageViewId);
                Card.assignImages(Card.getCards().get(x), imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the card value based on the clicked ImageView
                        int cardValue = Card.getCards().get(x);
                        // Call the method to handle the card click event
                        onCardClicked(cardValue, imageView, start);
                    }
                });
            }

        }

    }

    private void assign(ImageView imageView, int player){
        x++;
        if (x >= Card.getCards().size()) {

            iv_deck.setVisibility(View.INVISIBLE);
        }
        else{
            Card.assignImages(Card.getCards().get(x), imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the card value based on the clicked ImageView
                    int cardValue = Card.getCards().get(x);
                    // Call the method to handle the card click event
                    onCardClicked(cardValue, imageView, player);
                }
            });


        }

    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        hideImageViewsRange(1, "iv_new_p", View.INVISIBLE);
        hideImageViewsRange(2, "iv_new_p", View.INVISIBLE);
        hideImageViewsRange(3, "iv_new_p", View.INVISIBLE);
        hideImageViewsRange(4, "iv_new_p", View.INVISIBLE);

        hideImageViewsRange(1, "iv_p", View.INVISIBLE);
        hideImageViewsRange(2, "iv_p", View.INVISIBLE);
        hideImageViewsRange(3, "iv_p", View.INVISIBLE);
        hideImageViewsRange(4, "iv_p", View.INVISIBLE);

        Card.makeCardList();




    }


    public void startGame(View v) {
        Collections.shuffle(Card.getCards());
        assignCard("iv_p", 1);
        assignCard("iv_p", 2);
        assignCard("iv_p", 3);
        assignCard("iv_p", 4);

        hideImageViewsRange(1, "iv_p", View.VISIBLE);

        hideImageViewsRange(2, "iv_p", View.VISIBLE);
        hideImageViewsRange(3, "iv_p", View.VISIBLE);
        hideImageViewsRange(4, "iv_p", View.VISIBLE);

        iv_deck = (ImageView) findViewById(R.id.iv_deck);
        ImageView stack = (ImageView) findViewById(R.id.stack);


        iv_deck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onDeckClick();
            }
        });

        stack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onPileClick();
            }
        });

        stack.setVisibility(View.INVISIBLE);

//        startTurns();

//        hideImageViewsRange(2, "iv_new_p", View.VISIBLE);
//        hideImageViewsRange(3, "iv_new_p", View.VISIBLE);
//        hideImageViewsRange(4, "iv_new_p", View.VISIBLE);


    }



    private void onCardClicked(int cardValue, ImageView imageView, int player) {
        if (imageView == currentCard) {
//            Log.d("BANNNNN ITTTT","YOUUUU CCCAAANNNNOOOOTTTT DOOOOO THIS");
            cardsSelected.clear();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();



            params.topMargin += 50;
            imageView.setLayoutParams(params);
            currentCard=null;



        }

        else{
            int cardNumber = (int) imageView.getTag();
            int lastDigit = cardNumber % 10;

            ImageView v = null;
            int existing =-1;

            if(!cardsSelected.isEmpty()){
                v = cardsSelected.get(0);
                int x = (int) v.getTag();
                existing = x % 10;

            }
            Log.d("NEWWWWWW", String.valueOf(lastDigit));
            Log.d("OLDLDLLDLDLDL", String.valueOf(existing));


            if(player==turns[current_player] && picked == true &&(cardsSelected.isEmpty() || lastDigit==existing)){


                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();


                cardsSelected.add(imageView);

                params.topMargin -= 50;
                imageView.setLayoutParams(params);

                ImageView selectedCard = imageView;
                selectedCardId = imageView.getId();
                selectedCard.setId(selectedCardId);



            }

            currentCard = findViewById(selectedCardId);

        }




    }


    public void onCardDrop(View v){
        if(picked){

            ImageView selectedCard = findViewById(selectedCardId);

            ImageView stackImageView = findViewById(R.id.stack);


            dropped=true;
            picked=false;
            previous =stackImageView.getDrawable();


            Drawable cardDrawable = selectedCard.getDrawable(); // Get the drawable from the clicked card

            if (stackImageView.getVisibility() == View.VISIBLE) {
                stackImageView.setImageDrawable(previous);

            }
            else{
                stackImageView.setImageDrawable(cardDrawable);

            }
            current = cardDrawable;
//                stackImageView.setImageDrawable(cardDrawable);
            for (ImageView img_view : cardsSelected) {
                img_view.setVisibility(View.INVISIBLE);
            }
//        selectedCard.setVisibility(View.INVISIBLE);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) selectedCard.getLayoutParams();

            params.topMargin += 50;


            selectedCard.setLayoutParams(params);
            stackImageView.setVisibility(View.VISIBLE);

        }

    }

    private void onPileClick() {
        Log.d("DOOOOO UUUUU ENTERRRR", "B4");

        if(dropped==true){
            Log.d("DOOOOO UUUUU ENTERRRR", "CHEKCKCKCKKC");


            for (int i = 1; i <= 5; i++) {
                int imageViewId = getResources().getIdentifier("iv_p" + turns[current_player] +"c" + i, "id", getPackageName());
                ImageView imageView = findViewById(imageViewId);


                if (imageView != null && imageView.getVisibility() == View.INVISIBLE) {

                    // Change to the ID of your target ImageView

                    Drawable cardDrawable = previous; // Get the drawable from the stack ImageView
                    imageView.setImageDrawable(cardDrawable); // Set the drawable to the target ImageView
                    ImageView stackImageView = findViewById(R.id.stack);
                    stackImageView.setImageDrawable(current);
//                    ImageView test = findViewById(R.id.test);
//                    test.setImageDrawable(current);
//                    ImageView test2 = findViewById(R.id.test);
//                    test2.setImageDrawable(previous);
//                    stackImageView.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    break;


                }
            }

            dropped=false;
            picked=true;
            nextTurn();


        }


    }
    private void onDeckClick() {
        if(dropped==true){
            Log.d("PICKCKKCKC", "www");

            for (int i = 1; i <= 5; i++) {
                int imageViewId = getResources().getIdentifier("iv_p" + turns[current_player] +"c" + i, "id", getPackageName());
                ImageView imageView = findViewById(imageViewId);


                if (imageView != null && imageView.getVisibility() == View.INVISIBLE) {

                    assign(imageView, turns[current_player]);
                    imageView.setVisibility(View.VISIBLE);
                break;
                }
            }
            ImageView stackImageView = findViewById(R.id.stack);

            stackImageView.setImageDrawable(current);

            dropped=false;
            picked=true;
            nextTurn();


        }


    }

    private void nextTurn(){

        if (current_player == 3) {
            current_player=0;
        }
        else{
            current_player = current_player+1;

        }
        Log.d("turn befpre", String.valueOf(current_player));
        Log.d("turn after", String.valueOf(current_player));

        cardsSelected.clear();

    }




}
