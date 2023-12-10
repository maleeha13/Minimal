
package com.example.minimal;

import static com.example.minimal.Game.scores;
import static com.example.minimal.StartScreen.currentRound;
import static com.example.minimal.StartScreen.numberOfRounds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class MainActivity extends AppCompatActivity implements gameController.GameUIListener {

    private static boolean isPaused = false;

    CountDownTimer countDownTimer;
    private long remainingTime;
    gameController gameController ;
    ScoreController scoreController ;


    Game game;

    List<List<ImageView>> imageViewsList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scores = new int[numberOfRounds][4];
        StartScreen.currentRound=0;


        hideImageViewsRange(1, "iv_new_p", View.INVISIBLE);
        hideImageViewsRange(2, "iv_new_p", View.INVISIBLE);
        hideImageViewsRange(3, "iv_new_p", View.INVISIBLE);
        hideImageViewsRange(4, "iv_new_p", View.INVISIBLE);

        hideImageViewsRange(1, "iv_p", View.INVISIBLE);
        hideImageViewsRange(2, "iv_p", View.INVISIBLE);
        hideImageViewsRange(3, "iv_p", View.INVISIBLE);
        hideImageViewsRange(4, "iv_p", View.INVISIBLE);


        startGame();

    }


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

            int imageViewId = getResources().getIdentifier(pre+ start + "c" + i, "id", getPackageName());
            ImageView imageView = findViewById(imageViewId);
            Card.assignImages(Card.getCards().get(game.x), imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cardValue = Card.getCards().get(game.x);
                    onCardClicked(cardValue, imageView, start);
                }
            });
            game.x++;

        }

    }

    public void startGame() {

        gameController = new gameController(this, this);
        scoreController = new ScoreController();
        game = gameController.game;

        game.x=0;
        Button showButton = findViewById(R.id.show);
        showButton.setVisibility(View.INVISIBLE);

        Card.makeCardList();
        Collections.shuffle(Card.getCards());
        assignCard("iv_p", 1);
        assignCard("iv_p", 2);
        assignCard("iv_p", 3);
        assignCard("iv_p", 4);

        hideImageViewsRange(1, "iv_p", View.VISIBLE);

        hideImageViewsRange(2, "iv_p", View.VISIBLE);
        hideImageViewsRange(3, "iv_p", View.VISIBLE);
        hideImageViewsRange(4, "iv_p", View.VISIBLE);

        game.iv_deck = (ImageView) findViewById(R.id.iv_deck);
        ImageView stack = (ImageView) findViewById(R.id.stack);
        game.iv_deck.setVisibility(View.VISIBLE);

        LinearLayout linearLayout = findViewById(R.id.lay1);

        // Create a GradientDrawable
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); // White background
        border.setStroke(8, Color.RED); // Black border with width 2

        // Set the background drawable for your LinearLayout
        linearLayout.setBackground(border);
        game.iv_deck.setOnClickListener(new View.OnClickListener() {
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
        timer();
        for (int j = 1; j <= 4; j++) {
            List<ImageView> playerImageViews = new ArrayList<>();

            for (int i = 1; i <= 5; i++) {
                int imageViewId = getResources().getIdentifier("iv_p" + j + "c" + i, "id", getPackageName());
                ImageView img = findViewById(imageViewId);
                // Add the ImageView to the list
                playerImageViews.add(img);
            }

            // Add the list of ImageViews for the current player to the main list
            imageViewsList.add(playerImageViews);
        }

    }



    @Override
    public void onCardClicked(int cardValue, ImageView imageView, int player) {

        int tag = (int) imageView.getTag();
        if (game.cardsSelected.contains(imageView)) {
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

            if(player==game.turns[game.current_player] && game.picked == true &&(game.cardsSelected.isEmpty() || lastDigit==existing)){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                game.cardsSelected.add(imageView);
                params.topMargin -= 50;
                imageView.setLayoutParams(params);
                ImageView selectedCard = imageView;
                game.selectedCardId = imageView.getId();

            }
            else if ((!(game.cardsSelected.isEmpty()) && (lastDigit!=existing))&& game.picked){
                Toast.makeText(this, "Selected cards must have same value " , Toast.LENGTH_LONG).show();

            } else if (player!=game.turns[game.current_player]) {
                Toast.makeText(this, "Wait for your turn " , Toast.LENGTH_LONG).show();

            }
            else if((game.turns[game.current_player])==tag){
                Toast.makeText(this, "Thats not your card! " , Toast.LENGTH_LONG).show();
            }

            game.currentCard = findViewById(game.selectedCardId);
        }
    }


    public void onCardDrop(View v){

        if(game.picked && !(game.cardsSelected.isEmpty())){
            ImageView selectedCard = findViewById(game.selectedCardId);
            ImageView stackImageView = findViewById(R.id.stack);
            game.dropped=true;
            game.picked=false;
            game.previous =stackImageView.getDrawable();

            game.pre = selectedCard;
            game.check= (int) selectedCard.getTag();
            Drawable cardDrawable = selectedCard.getDrawable(); // Get the drawable from the clicked card

            if (stackImageView.getVisibility() == View.VISIBLE) {
                stackImageView.setImageDrawable(game.previous);
            }
            else {
                stackImageView.setImageDrawable(cardDrawable);
            }

            game.current = cardDrawable;

            for (ImageView img_view : game.cardsSelected) {
                img_view.setVisibility(View.INVISIBLE);
                game.droppedCard= img_view;
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

    @Override
    public void onPileClick() {
        ImageView stackImageView = findViewById(R.id.stack);
        if(game.dropped && !game.begin){

            ImageView img = findEmptyImageView();
            if (img != null) {
                game.x++;
                img.setVisibility(View.VISIBLE);
                stackImageView.setImageDrawable(game.current);

            }
            gameController.onPileClick(img, stackImageView);

        }

        else if(!game.dropped){
            Toast.makeText(this, "Drop a card first" , Toast.LENGTH_LONG).show();
        }
        else if(game.begin){
            Toast.makeText(this, "Theres no card to pick yet!" , Toast.LENGTH_LONG).show();

        }
        if (game.x > Card.getCards().size() - 1) {
            game.iv_deck.setVisibility(View.INVISIBLE);
            Button showButton = findViewById(R.id.show);
            showButton.performClick();
        }
        nextTurn();

    }

    @Override
    public void onDeckClick() {
        if (game.dropped && game.iv_deck.getVisibility() == View.VISIBLE) {


            ImageView img = findEmptyImageView();
            if (img != null) {
                gameController.onDeckClick(img);
                game.x++;
                img.setVisibility(View.VISIBLE);
                ImageView stackImageView = findViewById(R.id.stack);
                stackImageView.setImageDrawable(game.current);
            }

            nextTurn();

        } else {
            Toast.makeText(this, "Drop a card first", Toast.LENGTH_LONG).show();
        }
        if (game.x > Card.getCards().size() - 1) {
            game.iv_deck.setVisibility(View.INVISIBLE);
            Button showButton = findViewById(R.id.show);
            showButton.performClick();
        }
    }


    private ImageView findEmptyImageView() {
        for (int i = 1; i <= 5; i++) {
            int imageViewId = getResources().getIdentifier("iv_p" + game.turns[game.current_player] + "c" + i, "id", getPackageName());
            ImageView imageView = findViewById(imageViewId);
            if (imageView.getVisibility() == View.INVISIBLE) {
                return imageView;
            }
        }
        return null;
    }



    public void nextTurn() {

        if (game.current_player != 0) {
            String beforelayout = "lay" + (game.current_player + 1);
            int resID = getResources().getIdentifier(beforelayout, "id", getPackageName());

            LinearLayout linearLayoutold = findViewById(resID);

            linearLayoutold.setBackgroundColor(Color.TRANSPARENT);

            linearLayoutold.setBackground(null);
        } else {
            String beforelayout = "lay" + 1;
            int resID = getResources().getIdentifier(beforelayout, "id", getPackageName());
            LinearLayout linearLayoutold = findViewById(resID);
            linearLayoutold.setBackgroundColor(Color.TRANSPARENT);

            linearLayoutold.setBackground(null);
        }

        gameController.nextTurn();
        String layout = "lay" + (game.current_player + 1);
        int resID = getResources().getIdentifier(layout, "id", getPackageName());

        LinearLayout linearLayout = findViewById(resID);
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); // White background
        border.setStroke(8, Color.RED); // Black border with width 2
        linearLayout.setBackground(border);


        Button showButton = findViewById(R.id.show);
        calculateScores();


        if (game.current_player != 0) {
            TextView timerTextView = findViewById(R.id.time); // Use the ID you assigned in XML

            timerTextView.setText("Time left: " + "- seconds");
            greedyAI(game.current_player + 1);
        } else {
            if (countDownTimer != null) {
                System.out.println("cancel it ");

                countDownTimer.cancel();
                countDownTimer = null;
            }
            timer();

        }
        if (scores[currentRound][game.current_player] <= 5 && game.current_player == 0) {

            showButton.setVisibility(View.VISIBLE);
        } else {
            showButton.setVisibility(View.INVISIBLE);
        }


    }


        private void  calculateScores() {
        int currentRound = StartScreen.currentRound;

        for (int j = 1; j < 5; j++) {
            int score = 0;

            for (int i = 1; i <= 5; i++) {
                int imageViewId = getResources().getIdentifier("iv_p" + j + "c" + i, "id", getPackageName());
                ImageView img = findViewById(imageViewId);

                if (img.getVisibility() == View.VISIBLE) {
                    int cardNumber = (int) img.getTag();
                    score = score + (cardNumber % 100);
                }
            }

            scores[currentRound][j - 1] = score;
        }

    }

    public void showScores(View v) {
        calculateScores();
        int win = scoreController.calculateMinScore();
        scoreController.showScores(win, game);
        Toast.makeText(this, "THE WINNER IS PLAYER " + win+1 , Toast.LENGTH_LONG).show();
        scorecard scorecard = new scorecard(this);
        scorecard.showScoreboardPopup(5);
    }





    public void nextRound(View v){
        currentRound++;
        if(currentRound < StartScreen.numberOfRounds){

            View popupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_scorecard, null);

            TableLayout tableLayout = popupView.findViewById(R.id.tableLayout);
            tableLayout.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.GONE);
            if(tableLayout.getVisibility()==View.VISIBLE){
                tableLayout.setVisibility(View.INVISIBLE);
                tableLayout.setVisibility(View.GONE);
            }
            if (scorecard.dialog != null && scorecard.dialog.isShowing()) {
               scorecard.dialog.dismiss();
                closeOptionsMenu();
                startGame();

            }
        }
        else{
            winner_popup popup = new winner_popup(MainActivity.this);
            popup.displayWinner(scoreController);
        }

    }


    public void greedyAI(int j) {
        ImageView drop = null;
        int largest = 0;

        // Check if the game is paused
        if (isPaused) {
            return; // Exit the method if the game is paused
        }

        for (int i = 1; i <= 5; i++) {
            int imageViewId = getResources().getIdentifier("iv_p" + j + "c" + i, "id", getPackageName());
            ImageView img = findViewById(imageViewId);

            int cardNumber = (int) img.getTag();
            if ((cardNumber % 100) > largest) {
                largest = cardNumber % 100;
                drop = img;
            }
        }

        drop.performClick();
        Button dropButton = findViewById(R.id.drop); // Replace R.id.myButton with your actual button ID

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Perform the second click after a 2-second delay

                    dropButton.performClick();
                }

        }, 500); // 2000 milliseconds = 2 seconds

        // Delay between the second and third clicks
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if the game is paused before performing the third click
                    game.iv_deck.performClick();

            }
        }, 1000);
    }







    private void timer() {
        long durationInMillis;

        if (remainingTime > 0 && countDownTimer != null) {
            // If there is remaining time and a valid timer, resume from the remaining time
            durationInMillis = remainingTime;
            System.out.println("Resuming from previous duration: " + durationInMillis);
        } else {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            // If there is no remaining time or no valid timer, start a new timer with the default duration
            durationInMillis = 10000; // Default duration
            System.out.println("Resetting timer with duration: " + durationInMillis);
        }

        // Create a CountDownTimer with the specified duration and interval
        countDownTimer = new CountDownTimer(durationInMillis, 500) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (!isPaused) {
                    // Update the TextView with the remaining time
                    TextView timerTextView = findViewById(R.id.time); // Use the ID you assigned in XML
                    remainingTime = millisUntilFinished;
                    System.out.println("Remaining time is " + millisUntilFinished);

                    timerTextView.setText("Time left: " + (millisUntilFinished / 1000) + " seconds");
                }
            }

            @Override
            public void onFinish() {
                // The timer has finished; perform the desired action here
                timerUp();
            }
        };

        // Start the timer only if it's not paused
        if (!isPaused) {
            countDownTimer.start();
        }
    }



    public void timerUp() {
//                countDownTimer.cancel();

        System.out.println("TIME IS UPPPPPPPPPPP");
        // Your UI-related code here, including the existing content of timerUp
        if (game.picked && !(game.cardsSelected.isEmpty())) {
            Button dropButton = findViewById(R.id.drop);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Perform the second click after a 2-second delay
                    dropButton.performClick();
                }
            }, 500); // 2000 milliseconds = 2 seconds

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Perform the third click after a 2-second delay
                    game.iv_deck.performClick();
                }
            }, 1000);


        } else if (!game.dropped && game.cardsSelected.isEmpty()) {
            // Call greedyAI immediately
            greedyAI(1);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Perform the third click after a 2-second delay
                    game.iv_deck.performClick();
                }
            }, 1000);
        }
    }

    public void resumeGame(View v) {
        // Resume the game logic
        isPaused = false;

        timer();

        RelativeLayout mainLayout = findViewById(R.id.main);
        mainLayout.setBackgroundColor(Color.WHITE);

    }

    public void pauseGame(View v) {
        isPaused = true;

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        RelativeLayout mainLayout = findViewById(R.id.main);
        mainLayout.setBackgroundColor(Color.GRAY);

    }


    public void exitGame(View v) {
        Intent intent=new Intent(MainActivity.this, StartScreen.class);
        startActivity(intent);
    }

}