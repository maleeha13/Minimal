
package com.example.minimal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {


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
                        onCardClicked(cardValue, imageView);
                    }
                });
            }

        }

    }

    private void assign(ImageView imageView){
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
                    onCardClicked(cardValue, imageView);
                }
            });


        }

    }




    private void onCardClicked(int cardValue, ImageView imageView) {
        ImageView stackImageView = findViewById(R.id.stack);

        Drawable cardDrawable = imageView.getDrawable(); // Get the drawable from the clicked card
        stackImageView.setImageDrawable(cardDrawable);
        imageView.setVisibility(View.INVISIBLE);
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

        iv_deck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onDeckClick();
            }
        });
//
//        hideImageViewsRange(2, "iv_new_p", View.VISIBLE);
//        hideImageViewsRange(3, "iv_new_p", View.VISIBLE);
//        hideImageViewsRange(4, "iv_new_p", View.VISIBLE);


    }

    private void onDeckClick() {

        for (int i = 1; i <= 5; i++) {
            int imageViewId = getResources().getIdentifier("iv_p1c" + i, "id", getPackageName());
            ImageView imageView = findViewById(imageViewId);


            if (imageView != null && imageView.getVisibility() == View.INVISIBLE) {
                assign(imageView);
                imageView.setVisibility(View.VISIBLE);            }
        }
    }

}
