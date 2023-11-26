
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
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class MainActivity extends AppCompatActivity {

    Game game;
    private boolean isPaused = false;

    public static AlertDialog dialog;
    CountDownTimer countDownTimer;


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


//
//                if (game.x > Card.getCards().size() -2) {
//                    game.iv_deck.setVisibility(View.INVISIBLE);
//                    Button showButton = findViewById(R.id.show);
//                    showButton.performClick();
//                }

//                else{
            int imageViewId = getResources().getIdentifier(pre+ start + "c" + i, "id", getPackageName());
            ImageView imageView = findViewById(imageViewId);
            System.out.println("here is assign card it is " + game.x);
            Card.assignImages(Card.getCards().get(game.x), imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cardValue = Card.getCards().get(game.x);
                    onCardClicked(cardValue, imageView, start);
                }
            });
            game.x++;

//                }


        }





    }

    private void assign(ImageView imageView, int player){

//        if (game.x > Card.getCards().size()-2) {
//            game.iv_deck.setVisibility(View.INVISIBLE);
//            Button showButton = findViewById(R.id.show);
//            showButton.performClick();
//        }
//        else{

        System.out.println("here is assign it is " + game.x);

        Card.assignImages(Card.getCards().get(game.x), imageView);
        int store = game.x;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the card value based on the clicked ImageView
                int cardValue = Card.getCards().get(store);
                System.out.println("it is"+ store);
                // Call the method to handle the card click event
                onCardClicked(cardValue, imageView, player);
            }
        });

//        }


    }



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

// Add this to your onCreate or any appropriate method
        Button pauseButton = findViewById(R.id.pauseButton);
        Button resumeButton = findViewById(R.id.resumeButton);
        Button exitButton = findViewById(R.id.exitButton);


        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseGame();
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeGame();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitGame();
            }
        });

        startGame();



    }


    public void startGame() {
        game = new Game();
        game.x=0;

        Button showButton = findViewById(R.id.show); // Replace R.id.myButton with your actual button ID

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





    }



    private void onCardClicked(int cardValue, ImageView imageView, int player) {


        int tag = (int) imageView.getTag();


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


//
            else if((game.turns[game.current_player])==tag){
                Toast.makeText(this, "Thats not your card! " , Toast.LENGTH_LONG).show();

            }

            game.currentCard = findViewById(game.selectedCardId);


        }




    }


    public void onCardDrop(View v){
//        if (game.x > Card.getCards().size() -1) {
//            System.out.println("DECK IS OVER");
//            game.iv_deck.setVisibility(View.INVISIBLE);
////            Button showButton = findViewById(R.id.show);
////            showButton.performClick();
//        }

//        else{
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

//        }


    }

    private void onPileClick() {
        ImageView stackImageView = findViewById(R.id.stack);


        if(game.dropped && !game.begin){


            for (int i = 1; i <= 5; i++) {
                int imageViewId = getResources().getIdentifier("iv_p" + game.turns[game.current_player] +"c" + i, "id", getPackageName());
                ImageView imageView = findViewById(imageViewId);


                if (imageView != null && imageView.getVisibility() == View.INVISIBLE) {


                    imageView.setImageDrawable(game.previous);

                    imageView.setTag(game.second);


                    imageView.setTag(game.second);
                    stackImageView.setImageDrawable(game.current);

                    if(game.turns[game.current_player]!=0){
                        imageView.setVisibility(View.VISIBLE);

                    }


                    break;


                }
            }

            game.second =game.check;

            game.dropped=false;
            game.picked=true;
            if (game.x > Card.getCards().size() -1) {

                Button showButton = findViewById(R.id.show);
                showButton.performClick();
            }
            nextTurn();




        }

        else if(!game.dropped){
            Toast.makeText(this, "Drop a card first" , Toast.LENGTH_LONG).show();

        }
        else if(game.begin){
            Toast.makeText(this, "Theres no card to pick yet!" , Toast.LENGTH_LONG).show();

        }





    }
    private void onDeckClick() {

        System.out.println("it is "+ game.x);


        if(game.dropped &&  game.iv_deck.getVisibility()==View.VISIBLE ){

            if (game.x > Card.getCards().size() -1) {
                System.out.println("the game will end here");
                game.iv_deck.setVisibility(View.INVISIBLE);
//            Button showButton = findViewById(R.id.show);
//            showButton.performClick();
            }

            else{
                for (int i = 1; i <= 5; i++) {
                    int imageViewId = getResources().getIdentifier("iv_p" + game.turns[game.current_player] +"c" + i, "id", getPackageName());
                    ImageView imageView = findViewById(imageViewId);


                    game.new_pr =game.pre;
                    if (imageView != null && imageView.getVisibility() == View.INVISIBLE) {

                        assign(imageView, game.turns[game.current_player]);
                        System.out.println("assign card");

                        game.x++;

                        imageView.setVisibility(View.VISIBLE);

                        break;
                    }
                }
                ImageView stackImageView = findViewById(R.id.stack);

                stackImageView.setImageDrawable(game.current);
//            new_pr = pre;
                game.second =game.check;
                game.dropped=false;
                game.picked=true;
                nextTurn();


            }
        }



        else {
            Toast.makeText(this, "Drop a card first" , Toast.LENGTH_LONG).show();

        }








    }

    private void nextTurn(){





        System.out.println("the player is "+ game.current_player);
        if(game.current_player!=0){
            System.out.println("make it invisible ");
            String beforelayout = "lay" + (game.current_player + 1);
            int resID = getResources().getIdentifier(beforelayout, "id", getPackageName());

            LinearLayout linearLayoutold = findViewById(resID);

            linearLayoutold.setBackgroundColor(Color.TRANSPARENT);

            linearLayoutold.setBackground(null);
        }
        else{
            String beforelayout = "lay" + 1;
            int resID = getResources().getIdentifier(beforelayout, "id", getPackageName());
            LinearLayout linearLayoutold = findViewById(resID);
            linearLayoutold.setBackgroundColor(Color.TRANSPARENT);

            linearLayoutold.setBackground(null);
        }



        if (game.current_player == 3) {
            game.current_player=0;
        }
        else{

            game.current_player = game.current_player+1;

        }

        String layout = "lay" + (game.current_player + 1);
        int resID = getResources().getIdentifier(layout, "id", getPackageName());

        LinearLayout linearLayout = findViewById(resID);
        // Create a GradientDrawable
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); // White background
        border.setStroke(8, Color.RED); // Black border with width 2
        linearLayout.setBackground(border);
//
//        if (game.x >= Card.getCards().size()-1) {
//
//            game.iv_deck.setVisibility(View.INVISIBLE);
//            Button showButton = findViewById(R.id.show);
//            showButton.performClick();
//        }

        Button showButton = findViewById(R.id.show);
        game.cardsSelected.clear();
        int min = calculateScores();



        if(game.current_player!=0){
            greedyAI(game.current_player+1);
        }
        else{
            timer();

        }
        if(scores[currentRound][game.current_player]<=5 && game.current_player==0 ) {

            showButton.setVisibility(View.VISIBLE);
        }
        else{


            showButton.setVisibility(View.INVISIBLE);
        }
        game.begin =false;



    }


    private int calculateScores() {
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

            // Use scores[currentRound][j] to store scores for each player in each round

            scores[currentRound][j - 1] = score;
        }

        int minIndex = 0;

        // Find the minimum score for the current round
        for (int i = 0; i < scores[currentRound].length; i++) {
            if (scores[currentRound][i] < scores[currentRound][minIndex]) {
                minIndex = i;
            }
        }

        return minIndex;
    }


    public void showScores(View v){

        int win = calculateScores();
        if(win!=game.current_player){
            for(int i=0; i<4; i++){
                if(i==game.current_player){
                    game.scores[currentRound][game.current_player] = 20;

                }
                else{
                    game.scores[currentRound][i] = 0;

                }
            }
        }
        Toast.makeText(this, "THE WINNER IS PLAYER " + win+1 , Toast.LENGTH_LONG).show();
        showScoreboardPopup(5);




//
//        Intent intent=new Intent(MainActivity.this, StartScreen.class);
//        startActivityForResult(intent, 1);
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
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog.closeOptionsMenu();
                startGame();

            }
        }
        else{
            displayWinner();


        }


    }

    private void showScoreboardPopup(int delayInSeconds) {
        // Delay the appearance of the scoreboard
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Inflate the layout for the popup
                View popupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_scorecard, null);

                // Create a TableLayout and add it to the popup
                TableLayout tableLayout = popupView.findViewById(R.id.tableLayout);
                createScoreboard(tableLayout, 5); // 5 columns, adjust as needed
                // Build the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(popupView);
                builder.setTitle("Scoreboard"); // Set the title as needed

                // Add any additional customization or buttons to the AlertDialog if needed

                // Show the AlertDialog
                dialog = builder.create();
                dialog.show();
            }
        }, delayInSeconds * 1000); // Convert seconds to milliseconds
    }

    private void createScoreboard(TableLayout tableLayout, int numColumns) {
        // Create Header Row
        int totalScores[] = new int[4]; // Array to store total scores for each player

        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        // Empty cell for the top-left corner
        TextView emptyHeader = new TextView(this);
        emptyHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        headerRow.addView(emptyHeader);

        // Player Headers
        for (int player = 1; player <= 4; player++) {
            TextView playerHeader = new TextView(this);
            playerHeader.setText("Player " + player);
            playerHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            headerRow.addView(playerHeader);
        }

        // Add Header Row to the TableLayout
        tableLayout.addView(headerRow);

        // Dynamic Frame Headers
        for (int round = 1; round <= numberOfRounds; round++) {
            TableRow roundRow = new TableRow(this);
            roundRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // Round Header
            TextView roundHeader = new TextView(this);
            roundHeader.setText("Round " + round);
            roundHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            roundRow.addView(roundHeader);

            // Create Player Cells for the Current Round
            for (int player = 1; player <= 4; player++) {
                TextView frameCell = new TextView(this);
                // Get the score from the game.scores array

                // Adjust the conditions here to match your array size
                if (round - 1 < scores.length && player - 1 < scores[round - 1].length) {
                    int score = scores[round - 1][player - 1];
                    frameCell.setText(Integer.toString(score));
                    frameCell.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                    roundRow.addView(frameCell);
                } else {
                    // Handle the case where the array bounds are exceeded
                    System.out.println("Array index out of bounds.");
                }
            }

            // Add Round Row to the TableLayout
            tableLayout.addView(roundRow);
        }

        tableLayout.addView(new TableRow(this));

        // Add a row for total scores
        TableRow totalRow = new TableRow(this);
        totalRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        TextView totalHeader = new TextView(this);
        totalHeader.setText("Total Scores");
        totalHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        totalRow.addView(totalHeader);

        // Calculate and display the total score for each player
        for (int player = 1; player <= 4; player++) {
            int playerTotal = 0;

            // Sum up the scores for the player across all rounds
            for (int round = 1; round <= numberOfRounds; round++) {
                // Adjust the conditions here to match your array size
                if (round - 1 < scores.length && player - 1 < scores[round - 1].length) {
                    playerTotal += scores[round - 1][player - 1];
                } else {
                    // Handle the case where the array bounds are exceeded
                    System.out.println("Array index out of bounds.");
                }
            }

            // Display the total score for the player
            TextView totalCell = new TextView(this);
            totalCell.setText(Integer.toString(playerTotal));
            totalCell.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            totalRow.addView(totalCell);
        }

        // Add Total Row to the TableLayout
        tableLayout.addView(totalRow);

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
                if (!isPaused) {
                    game.iv_deck.performClick();
                }
            }
        }, 1000);
    }


    private void displayWinner() {



        int[] totalScores = calculateTotalScores();

        // Find the player with the lowest total score
        int winnerPlayer = 1;
        int winnerScore = totalScores[0];

        for (int player = 2; player <= 4; player++) {
            if (totalScores[player - 1] < winnerScore) {
                winnerPlayer = player;
                winnerScore = totalScores[player - 1];
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View winnerView = getLayoutInflater().inflate(R.layout.activity_winner_popup, null);

        // Customize the winner message and score
        TextView winnerMessageTextView = winnerView.findViewById(R.id.winnerMessageTextView);
        winnerMessageTextView.setText("Player " + winnerPlayer + " wins!");

        TextView winnerScoreTextView = winnerView.findViewById(R.id.winnerScoreTextView);
        winnerScoreTextView.setText("Score: " + winnerScore);

        // Get the KonfettiView reference
        KonfettiView celeb = winnerView.findViewById(R.id.celeb);

        // Trigger the confetti animation
        celeb.build()
                .addColors(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(-50f, celeb.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 5000L);

        builder.setView(winnerView);
        builder.setTitle("Game Over");

        // Show the AlertDialog
        AlertDialog winnerDialog = builder.create();
        winnerDialog.show();
        Button finishButton = winnerView.findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, StartScreen.class);


                startActivity(intent);
                // Add any actions you want to perform when the "Finish" button is clicked
                // This will close the activity
            }
        });


    }

    private int[] calculateTotalScores() {
        int[] totalScores = new int[4];

        for (int player = 1; player <= 4; player++) {
            int playerTotal = 0;

            // Sum up the scores for the player across all rounds
            for (int round = 1; round <= numberOfRounds; round++) {
                // Adjust the conditions here to match your array size
                if (round - 1 < scores.length && player - 1 < scores[round - 1].length) {
                    playerTotal += scores[round - 1][player - 1];
                } else {
                    // Handle the case where the array bounds are exceeded
                    System.out.println("Array index out of bounds.");
                }
            }

            totalScores[player - 1] = playerTotal;
        }

        return totalScores;
    }


    private void timer() {
        long durationInMillis = 10000;

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Create a CountDownTimer with the specified duration and interval
        countDownTimer = new CountDownTimer(durationInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                // Update the TextView with the remaining time
                TextView timerTextView = findViewById(R.id.time); // Use the ID you assigned in XML

                long secondsLeft = millisUntilFinished / 1000;
                timerTextView.setText("Time left: " + secondsLeft + " seconds");
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
        Log.d("MyApp", "Handler is running on thread in timer up: " + Thread.currentThread().getName());
//                countDownTimer.cancel();

        // Your UI-related code here, including the existing content of timerUp
        System.out.println("testing" + game.cardsSelected.isEmpty());
        if (game.picked && !(game.cardsSelected.isEmpty())) {
            System.out.println("card is picked but not dropped");
            Button dropButton = findViewById(R.id.drop);
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

    private void pauseGame() {
        // Pause the game logic
        isPaused = true;
        RelativeLayout mainLayout = findViewById(R.id.main);
        mainLayout.setBackgroundColor(Color.GRAY);




        // Cancel the CountDownTimer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Add any other logic needed to pause the game
    }

    private void resumeGame() {
        RelativeLayout mainLayout = findViewById(R.id.main);
        mainLayout.setBackgroundColor(Color.WHITE);
        // Resume the game logic
        isPaused = false;

        // Restart the CountDownTimer if needed
        timer();

        // Add any other logic needed to resume the game
    }


    private void exitGame() {
        Intent intent=new Intent(MainActivity.this, StartScreen.class);


        startActivity(intent);
    }

}