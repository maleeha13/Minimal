
package com.example.minimal;

import static com.example.minimal.Game.scores;
import static com.example.minimal.StartScreen.currentRound;
import static com.example.minimal.StartScreen.numberOfRounds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class MainActivity extends AppCompatActivity implements gameController.GameUIListener  {

    private static boolean isPaused = false;

    CountDownTimer countDownTimer;
    private long remainingTime;
    gameController gameController ;
    ScoreController scoreController ;
    protected static List<Integer> cards = new ArrayList<>();  // Replace String with the actual type of keys and values
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


        try {
            startGame();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

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

    public void startGame() throws CloneNotSupportedException {

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
        game.stack = (ImageView) findViewById(R.id.stack);
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

                try {
                    onDeckClick();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        game.stack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    onPileClick();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        game.stack.setVisibility(View.INVISIBLE);


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

//        State s = new State(this, game.current_player+1);
//        s.CloneAndRandomize(game.current_player+1);
//
//        MCTSNode rootNode = new MCTSNode(s, null, game.current_player+1, null);
//
//
//// Test the getUntriedMoves method
//        List<Move> untriedMoves = rootNode.getUntriedMoves();
//
//// Print or assert the result
//        System.out.println("Untried Moves: " + untriedMoves);
//
//        if (!untriedMoves.isEmpty()) {
//            Move selectedMove = untriedMoves.get(1); // Select the first move for demonstration
//            System.out.println("Applying move: " + selectedMove);
//            s.applyMove(selectedMove, game.current_player + 1);
//
//            // Create a new child node with the selected move and the resulting state
//            MCTSNode newChildNode = rootNode.addChild(selectedMove, game.current_player + 1);
//
//            // Now you can inspect the updated state or perform further testing
//
//            // Print or assert the updated untried moves
//            List<Move> updatedUntriedMoves = rootNode.getUntriedMoves();
//            System.out.println("Updated Untried Moves: " + updatedUntriedMoves);
////            System.out.println(s.getResult());
//
//        } else {
//            System.out.println("No untried moves available.");
//        }

    }



    @Override
    public void onCardClicked(int cardValue, ImageView imageView, int player) {

        int tag = (int) imageView.getTag();
        if (game.cardsSelected.contains(imageView)) {
            System.out.println("CARD 11111111111111111");

            game.cardsSelected.remove(imageView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.topMargin += 50;
            imageView.setLayoutParams(params);
            game.currentCard=null;

        }

        else{
            System.out.println("CARD 222222222222222222");

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
                RelativeLayout rootView = findViewById(R.id.main);
                System.out.println("it is picking the card " + imageView.getTag()) ;

// Force refresh the layout
                rootView.requestLayout();


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

        if(game.picked && !(game.cardsSelected.isEmpty())) {
            ImageView selectedCard = findViewById(game.selectedCardId);
            ImageView stackImageView = findViewById(R.id.stack);
            game.dropped = true;
            game.picked = false;
            game.previous = stackImageView.getDrawable();

            game.pre = selectedCard;
            game.check = (int) selectedCard.getTag();
            Drawable cardDrawable = selectedCard.getDrawable(); // Get the drawable from the clicked card

            if (stackImageView.getVisibility() == View.VISIBLE) {
                stackImageView.setImageDrawable(game.previous);
            } else {
                stackImageView.setImageDrawable(cardDrawable);
            }

            game.current = cardDrawable;

            for (ImageView img_view : game.cardsSelected) {
                img_view.setVisibility(View.INVISIBLE);
                ViewParent parent = img_view.getParent();
                if (parent instanceof View) {
                    ((View) parent).invalidate();
                }

                System.out.println("Making it invisible on thread: " + Thread.currentThread().getName());
                System.out.println("card is " + img_view.getTag());
                System.out.println("making it invisible " + img_view.getVisibility());
                game.droppedCard = img_view;
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) img_view.getLayoutParams();
                params.topMargin += 50;
                img_view.setLayoutParams(params);
                RelativeLayout rootView = findViewById(R.id.main);

// Force refresh the layout
                rootView.requestLayout();

            }

            stackImageView.setVisibility(View.VISIBLE);
            if (selectedCard.getTag() != null) {
                cards.add((Integer) selectedCard.getTag());


            }


            if (game.playerHand != null ) {
                List<Integer> playerHandList = game.playerHand.get(game.current_player);
                if (playerHandList != null && !playerHandList.isEmpty()) {
                    // Remove cards from the player's hand based on their indices
                    for (ImageView card : game.cardsSelected) {
                        // Remove the first occurrence of the card from the player's hand
                        // UPDATES PLAYER CARD

                        playerHandList.remove(card);
                    }

                    // Update the player's hand in the playerHand map
                    game.playerHand.put(game.current_player, playerHandList);
                }
            }


        }
        else{
            Toast.makeText(this, "Pick a card to drop" , Toast.LENGTH_LONG).show();

        }


    }

    @Override
    public void onPileClick() throws CloneNotSupportedException {
        ImageView stackImageView = findViewById(R.id.stack);
        if(game.dropped && !game.begin){

            ImageView img = findEmptyImageView();
            if (img != null) {
                game.x++;
                img.setVisibility(View.VISIBLE);
                stackImageView.setImageDrawable(game.current);
                cards.remove((Integer) stackImageView.getTag());


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
    public void onDeckClick() throws CloneNotSupportedException {

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



    public void nextTurn() throws CloneNotSupportedException {


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
//            callGreedy(game.current_player + 1);
            callMinimize(game.current_player +1);


//            callMonte();

        } else {
            if (countDownTimer != null) {

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

    public void callMonte() throws CloneNotSupportedException {
        State s = new State(this, game.current_player+1);

        ISMCTS monte = new ISMCTS();
        Button dropButton = findViewById(R.id.drop);
        if(game.playerHand!=null){
            Button showButton = findViewById(R.id.show);
            showButton.setVisibility(View.INVISIBLE);
            monte.show(getHand(game.current_player), game, showButton);

        }
        monte.runInBackground(s, 5, getHand(game.current_player+1), dropButton, game, game.iv_deck, game.stack);

    }

        public List<ImageView> getHand( int j) {
            List<ImageView> imageViewList =  new ArrayList<>();;
            for (int i = 1; i <= 5; i++) {
                int imageViewId = getResources().getIdentifier("iv_p" + j + "c" + i, "id", getPackageName());
                ImageView img = findViewById(imageViewId);
                if(img.getVisibility()==View.VISIBLE){
                    imageViewList.add(img);

                }

            }

            return imageViewList;
        }




    private class MonteCarloTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            State s = new State(MainActivity.this, game.current_player + 1);
            ISMCTS monte = new ISMCTS();
            Button dropButton = findViewById(R.id.drop);
            monte.runInBackground(s, 5, getHand(game.current_player+1), dropButton, game, game.iv_deck, game.droppedCard);
            return null;
        }
    }
    public void callMonte2() {
        new MonteCarloTask().execute();
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





    public void nextRound(View v) throws CloneNotSupportedException {
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


    public void callGreedy(int j){
        int largest = 0;
        ImageView drop = null;
        if (isPaused) {
            return; // Exit the method if the game is paused
        }
        Button dropButton = findViewById(R.id.drop); // Replace R.id.myButton with your actual button ID

        for (int i = 1; i <= 5; i++) {
            int imageViewId = getResources().getIdentifier("iv_p" + j + "c" + i, "id", getPackageName());
            ImageView img = findViewById(imageViewId);

            int cardNumber = (int) img.getTag();
            if ((cardNumber % 100) > largest) {
                largest = cardNumber % 100;
                drop = img;
            }
        }

        GreedyAI greedy = new GreedyAI();
        greedy.greedyAI(j, drop, game, dropButton);

    }

    public void callMinimize(int j){
        int largest = 0;
        Boolean pickFromStack = false;
        ImageView drop = null;
        List<ImageView> list = new ArrayList<>();
        if(game.playerHand!=null){
            Button show = findViewById(R.id.show);
            show.setVisibility(View.INVISIBLE);
            MinimizeAI minimize = new MinimizeAI();
            minimize.show(getHand(game.current_player), game, show);

        }


        Map<Integer, List<ImageView>> imageViewMap = new HashMap<>();

        if (isPaused) {
            return; // Exit the method if the game is paused
        }

        Button dropButton = findViewById(R.id.drop); // Replace R.id.myButton with your actual button ID
        ImageView stack = findViewById(R.id.stack);

        int stackCardNumber = stack.getTag() != null ? (int) stack.getTag() : 0;
        List<ImageView> descList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            int imageViewId = getResources().getIdentifier("iv_p" + j + "c" + i, "id", getPackageName());
            ImageView img = findViewById(imageViewId);
            System.out.println("image is " + img.getTag());
            if(img.getVisibility() == View.VISIBLE){
                System.out.println(" vis is image is " + img.getTag());

                int cardNumber = (int) img.getTag();
                int remainder = cardNumber % 100;

                if (cardNumber % 100 == stackCardNumber % 100) {
                    pickFromStack = true;
                }

                if (imageViewMap.containsKey(remainder)) {
                    // Add the image view to the existing list
                    imageViewMap.get(remainder).add(img);
                } else {
                    // Create a new list and add the image view to it
                    List<ImageView> imageViewList = new ArrayList<>();
                    imageViewList.add(img);
                    imageViewMap.put(remainder, imageViewList);
                }
                descList.add(img);
            }
            System.out.println("image");





        }

        Collections.sort(descList, new Comparator<ImageView>() {
            @Override
            public int compare(ImageView img1, ImageView img2) {
                int tag1 = (int) img1.getTag() % 100;
                int tag2 = (int) img2.getTag() % 100;
                // Sort in descending order
                return tag2 - tag1;
            }
        });



        largest = (int) descList.get(0).getTag() % 100;

// Iterate over the image view map and add all values to the list
        int largestKey = 0;
        int largestValue = 0;

// Find the key with the largest value in the map
        for (Map.Entry<Integer, List<ImageView>> entry : imageViewMap.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue().size();

            if (value > largestValue) {
                largestKey = key;
                largestValue = value;
            }
        }

        System.out.println("largest key is " + largestKey);
        System.out.println("largest val " + largestValue);


// Add the ImageViews corresponding to the largest key to the list
        list.addAll(imageViewMap.get(largestKey));



// Check if the total value in the list is greater than 'largest'
        int totalValue = 0;
        for (ImageView imageView : list) {

            totalValue += (Integer) imageView.getTag() % 100;
            System.out.println("val being added is " + (Integer) imageView.getTag() % 100);
        }



        String source;

        if(pickFromStack){
            source = "pile";
        }
        else{
            source="deck";
        }


        System.out.println("PLAYER ISSSSSSSS " + game.current_player );
        for (ImageView imageView : descList) {

            System.out.println("val in desc list " + imageView.getTag());
        }

        if (totalValue >= largest) {
            System.out.println("mult");
            for (ImageView imageView : list) {
                System.out.println("val in LIST IS " + imageView.getTag());
            }
            MinimizeAI minimize = new MinimizeAI();
            minimize.minimizeAI(list, drop, pickFromStack, game, dropButton, stack, source); // Return the list if its total value is greater than 'largest'
        } else {
            // Create a new list and add 'img' to it
            List<ImageView> newList = new ArrayList<>();
            if (largest == stackCardNumber % 100 && pickFromStack) {
                    drop = (descList.get(1)); // Add the second ImageView in the list to the descList
            }

            else{
                drop = (descList.get(0));
            }
            newList.add(drop);

            for (ImageView imageView : newList) {
                System.out.println("val in LIST IS " + imageView.getTag());
            }
            MinimizeAI minimize = new MinimizeAI();
            minimize.minimizeAI(newList, drop, pickFromStack, game, dropButton, stack, source);
        }







    }




    private void timer() {
        long durationInMillis;

        if (remainingTime > 0 && countDownTimer != null) {
            // If there is remaining time and a valid timer, resume from the remaining time
            durationInMillis = remainingTime;
        } else {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            // If there is no remaining time or no valid timer, start a new timer with the default duration
            durationInMillis = 10000; // Default duration
        }

        // Create a CountDownTimer with the specified duration and interval
        countDownTimer = new CountDownTimer(durationInMillis, 500) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (!isPaused) {
                    // Update the TextView with the remaining time
                    TextView timerTextView = findViewById(R.id.time); // Use the ID you assigned in XML
                    remainingTime = millisUntilFinished;

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
            callGreedy(1);
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