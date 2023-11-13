
package com.example.minimal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    int[] turns = {1, 2, 3, 4};
    Integer[] scores = new Integer[4];
    boolean begin = true;
    int current_player=0;
    boolean dropped = false;
    boolean picked = true;
    int selectedCardId;
    private ArrayList<ImageView> cardsSelected = new ArrayList<>();
    ImageView currentCard;
    Drawable previous ;
    Drawable current ;
    ImageView new_pr;
    ImageView pre;
    int check;
    int second;
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
                        int cardValue = Card.getCards().get(x);
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

        Button showButton = findViewById(R.id.show); // Replace R.id.myButton with your actual button ID

        showButton.setVisibility(View.INVISIBLE);
        Card.makeCardList();
        startGame();



    }


    public void startGame() {
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




    }



    private void onCardClicked(int cardValue, ImageView imageView, int player) {
        int tag = (int) imageView.getTag();


        if ( cardsSelected.contains(imageView)) {
            System.out.println("one of the selected cards");
            cardsSelected.remove(imageView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();

            params.topMargin += 50;
            imageView.setLayoutParams(params);
            currentCard=null;


        }



        else{
            int cardNumber = (int) imageView.getTag();
            int lastDigit = cardNumber % 100;

            ImageView v = null;
            int existing =-1;

            if(!cardsSelected.isEmpty()){
                v = cardsSelected.get(0);
                int x = (int) v.getTag();
                existing = x % 100;

            }


            if(player==turns[current_player] && picked == true &&(cardsSelected.isEmpty() || lastDigit==existing)){

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();


                cardsSelected.add(imageView);

                params.topMargin -= 50;
                imageView.setLayoutParams(params);

                ImageView selectedCard = imageView;

                selectedCardId = imageView.getId();

            }
            else if ((!(cardsSelected.isEmpty()) && (lastDigit!=existing))&& picked){
                Toast.makeText(this, "Selected cards must have same value " , Toast.LENGTH_LONG).show();

            } else if (player!=turns[current_player]) {
                Toast.makeText(this, "Wait for your turn " , Toast.LENGTH_LONG).show();

            }


//
            else if((turns[current_player])==tag){
                Toast.makeText(this, "Thats not your card! " , Toast.LENGTH_LONG).show();

            }

            currentCard = findViewById(selectedCardId);


        }




    }


    public void onCardDrop(View v){
        if(picked && !(cardsSelected.isEmpty())){

            ImageView selectedCard = findViewById(selectedCardId);

            ImageView stackImageView = findViewById(R.id.stack);


            dropped=true;
            picked=false;
            previous =stackImageView.getDrawable();

            pre = selectedCard;
            check= (int) selectedCard.getTag();

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

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) img_view.getLayoutParams();

                params.topMargin += 50;
                img_view.setLayoutParams(params);
            }



            stackImageView.setVisibility(View.VISIBLE);


        }

        else{
            Toast.makeText(this, "Pick a card to drop" , Toast.LENGTH_LONG).show();

        }

    }

    private void onPileClick() {
        ImageView stackImageView = findViewById(R.id.stack);


            if(dropped && !begin){
                System.out.println("pile " + check);


                for (int i = 1; i <= 5; i++) {
                    int imageViewId = getResources().getIdentifier("iv_p" + turns[current_player] +"c" + i, "id", getPackageName());
                    ImageView imageView = findViewById(imageViewId);


                    if (imageView != null && imageView.getVisibility() == View.INVISIBLE) {


                        imageView.setImageDrawable(previous);

                        imageView.setTag(second);


                        stackImageView.setImageDrawable(current);

                        imageView.setVisibility(View.VISIBLE);

                        break;


                    }
                }

                second =check;

                dropped=false;
                picked=true;
                nextTurn();


            }

            else if(!dropped){
                Toast.makeText(this, "Drop a card first" , Toast.LENGTH_LONG).show();

            }
            else if(begin){
                Toast.makeText(this, "Theres no card to pick yet!" , Toast.LENGTH_LONG).show();

            }





    }
    private void onDeckClick() {

        if(dropped){


            for (int i = 1; i <= 5; i++) {
                int imageViewId = getResources().getIdentifier("iv_p" + turns[current_player] +"c" + i, "id", getPackageName());
                ImageView imageView = findViewById(imageViewId);


                new_pr =pre;
                if (imageView != null && imageView.getVisibility() == View.INVISIBLE) {

                    assign(imageView, turns[current_player]);
                    imageView.setVisibility(View.VISIBLE);

                    break;
                }
            }
            ImageView stackImageView = findViewById(R.id.stack);

            stackImageView.setImageDrawable(current);
//            new_pr = pre;
            second =check;


            dropped=false;
            picked=true;
            nextTurn();


        }
        else {
            Toast.makeText(this, "Drop a card first" , Toast.LENGTH_LONG).show();

        }


    }

    private void nextTurn(){

        if (current_player == 3) {
            current_player=0;
        }
        else{
            current_player = current_player+1;

        }


        cardsSelected.clear();
        int min = calculateScores();

        System.out.println(scores[min]);
        if(scores[current_player]<=5){
            Button showButton = findViewById(R.id.show); // Replace R.id.myButton with your actual button ID

            showButton.setVisibility(View.VISIBLE);
        }
        begin =false;
    }

    private int calculateScores(){

        for(int j=1; j<5; j++){
            int score =0;
            for (int i = 1; i <= 5; i++) {

                int imageViewId = getResources().getIdentifier("iv_p" + j + "c" + i, "id", getPackageName());

                ImageView img = findViewById(imageViewId);

                if(img.getVisibility()==View.VISIBLE){
                    int cardNumber = (int) img.getTag();

                    score = score + (cardNumber % 100);
                    System.out.println((cardNumber % 100));
                }
            }

            scores[j-1]=score;
        }



        int minIndex = 0;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] < scores[minIndex]) {
                System.out.println(scores[i]);
                minIndex = i;
            }
        }


        return minIndex;
    }

    public void showScores(View v){

        int win = calculateScores();
        System.out.println("THE WINNER IS PLAYER" + win+1);
        Toast.makeText(this, "THE WINNER IS PLAYER " + win+1 , Toast.LENGTH_LONG).show();

    }


}