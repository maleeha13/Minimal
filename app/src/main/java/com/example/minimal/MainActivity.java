
package com.example.minimal;

import static com.example.minimal.Game.scores;
import static com.example.minimal.StartScreen.chosen;
import static com.example.minimal.StartScreen.currentRound;
import static com.example.minimal.StartScreen.difficulty;
import static com.example.minimal.StartScreen.name;
import static com.example.minimal.StartScreen.numberOfRounds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * This class contains the main logic of the game including the UI logic of card clicking, dropping and picking
 * It also contains functions for next turns, and starting the game
 */
public class MainActivity extends AppCompatActivity implements gameController.GameUIListener  {
    private static boolean isPaused = false;
    CountDownTimer countDownTimer;              // Countdown timer
    private long remainingTime;                 // Time remaining for the player to play
    gameController gameController ;
    ScoreController scoreController ;
    static List<Integer> cards = new ArrayList<>();
    Game game;
    List<List<ImageView>> imageViewsList = new ArrayList<>();
    static ArrayList<Integer> testCards = new ArrayList<>();


    String fileName = "eval_1.txt";

    String fileName1 = "ev_27.txt";


    /**
     * Distributes the cards and starts the game
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllMoves();
        scores = new int[numberOfRounds][4];
        StartScreen.currentRound=0;

        hideImageViewsRange(1, "iv_new_p", View.INVISIBLE);
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


    /**
     * Hides the image views of the other players
     * @param start
     * @param pre
     * @param visibility
     */
    private void hideImageViewsRange(int start, String pre, int visibility) {
        for (int i = 1; i <= 5; i++) {
            int imageViewId = getResources().getIdentifier(pre+ start + "c" + i, "id", getPackageName());

            ImageView imageView = findViewById(imageViewId);

            if (imageView != null) {
                imageView.setVisibility(visibility);
            }
        }
    }


    /**
     * Assigns cards to each player nad sets an onClickListener to each card
     * @param pre
     * @param start
     */
    private void assignCard(String pre, int start){

        for(int i=1; i<=5; i++){

            int imageViewId = getResources().getIdentifier(pre+ start + "c" + i, "id", getPackageName());
            ImageView imageView = findViewById(imageViewId);
            Card.assignImages(Card.getCards().get(game.x), imageView);
//            Card.assignImages(testCards.get(game.x), imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!game.show){
                        int cardValue = Card.getCards().get(game.x);
                    try {
                        onCardClicked(cardValue, imageView, start);
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                    }

                }
            });
            game.x++;

        }

    }

    /**
     * Starts the game
     * Shuffles cards and assigns cards to each player
     * Sets UI to highlight player 1 and starts the timer
     * @throws CloneNotSupportedException
     */
    public void startGame() throws CloneNotSupportedException {
        System.out.println("current round is " + currentRound);

        // Creates new instances of game and score controllers
        gameController = new gameController(this, this);
        scoreController = new ScoreController();
        game = gameController.game;

        // Sets name and avatar
        ImageView play_av = findViewById(R.id.ai_av1);
        if(chosen!=null){
            play_av.setImageDrawable(StartScreen.chosen.getDrawable());
        }
        if(name!=null){
            TextView name = findViewById(R.id.player_name);
            name.setText(StartScreen.name);
        }

        game.x=0;
        Button showButton = findViewById(R.id.show);
        showButton.setVisibility(View.INVISIBLE);

        // Shuffles and assigns cards
        Card.makeCardList();
        Collections.shuffle(Card.getCards());
        ArrayList<Integer> list = makeTestList();
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
        cards = new ArrayList<>();

        // Highlights player 1
        LinearLayout linearLayout = findViewById(R.id.lay1);

        // Create a GradientDrawable
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); // White background
        border.setStroke(12, Color.parseColor("#E17B26")); // Black border with width 2
        linearLayout.setBackground(border);


        String img_name = "ai_av1";
        int resIDImage = getResources().getIdentifier(img_name, "id", getPackageName());

        ImageView image = findViewById(resIDImage);
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setShape(GradientDrawable.OVAL); // Set the shape to Oval for circular shape
        borderDrawable.setStroke(12, Color.parseColor("#E17B26")); // Black border with width 2
        borderDrawable.setSize(image.getWidth(), image.getHeight()); // Set the size of the border drawable to match the ImageView
        image.setBackground(borderDrawable);

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


        // Sets timer for player 1
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            remainingTime = 0;
        }
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



    // 1. CARD IS CLICKED

    /**
     * Allows user to select and unselect cards
     * Also allows user to select multiple card with same rank
     * @param cardValue The value of the clicked card.
     * @param imageView The ImageView associated with the clicked card.
     * @param player    The player who clicked the card.
     * @throws CloneNotSupportedException
     */
    @Override
    public void onCardClicked(int cardValue, ImageView imageView, int player) throws CloneNotSupportedException {

        if(!game.show){
            int tag = (int) imageView.getTag();

            // 2. CARD HAS PREVIOUSLY BEEN CLICKED
            if (game.cardsSelected.contains(imageView)) {
                // 3. UNSELECTS CARD
                game.cardsSelected.remove(imageView);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                params.topMargin += 50;
                imageView.setLayoutParams(params);
                game.currentCard=null;
            }

            else{
                // 4. GETS RANK OF THE CARD
                int cardNumber = (int) imageView.getTag();
                int lastDigit = cardNumber % 100;
                ImageView v;
                int existing =-1;

                // 5. CHECKS IF OTHER CARDS HAVE BEEN CLICKED BEFORE
                if(!game.cardsSelected.isEmpty()){

                    // 6. GETS RANK OF PREVIOUSLY SELECTED CARD
                    v = game.cardsSelected.get(0);
                    int x = (int) v.getTag();
                    existing = x % 100;
                }

                // 7. CHECKS IF CURRENT CARD CARD AND PREV CARD HAS SAME RANK
                if(player==game.turns[game.current_player] && game.picked &&(game.cardsSelected.isEmpty() || lastDigit==existing)){
                    // 8. SELECTS THE CARDS
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    game.cardsSelected.add(imageView);
                    params.topMargin -= 50;
                    imageView.setLayoutParams(params);
                    game.selectedCardId = imageView.getId();
                    RelativeLayout rootView = findViewById(R.id.main);
                    rootView.requestLayout();
                }

                // 9. TELLS USER THEY CANNOT SELECT CARDS WITH DIFFERENT RANKS
                else if ((!(game.cardsSelected.isEmpty()) && (lastDigit!=existing))&& game.picked){
//                    Toast.makeText(this, "Selected cards must have same value " , Toast.LENGTH_LONG).show();

                }

                else if (player!=game.turns[game.current_player]) {
//                    Toast.makeText(this, "Wait for your turn " , Toast.LENGTH_LONG).show();

                }
                else if((game.turns[game.current_player])==tag){
//                    Toast.makeText(this, "That's not your card! " , Toast.LENGTH_LONG).show();
                }

                game.currentCard = findViewById(game.selectedCardId);
            }
        }

        if (game.x > Card.getCards().size() - 1) {
            game.iv_deck.setVisibility(View.INVISIBLE);
            game.show=true;
            dispEndScores();
        }
    }


    /**
     * Allows the player to drop cards
     * Updates the discard stack view to display what the next player can pick up
     * @param v
     */
    // 1. DROP BUTTON IS CLICKED
    public void onCardDrop(View v){

        // 2. CHECKS IF A CARD HAS BEEN SELECTED
        if(game.picked && !(game.cardsSelected.isEmpty())) {
            List<ImageView> handImageViews = getHand(game.current_player + 1);
            List<Integer> tags = new ArrayList<>();
            game.dropped = true;
            game.picked = false;

            // 3. SAVES THE DRAWABLE OF THE CARD TO BE DROPPED
            ImageView selectedCard = findViewById(game.selectedCardId);
            game.check = (int) selectedCard.getTag();
            Drawable cardDrawable = selectedCard.getDrawable(); // Get the drawable from the clicked card
            game.pre = selectedCard;

            // 4. RETRIEVES THE CARD DROPPED BY THE PREVIOUS PLAYER
            ImageView stackImageView = findViewById(R.id.stack);
            game.previous = stackImageView.getDrawable();

            // 5. IF THE STACK IS VISIBLE, UPDATE STACK IMAGE WITH THE PREVIOUS PLAYER'S CARD
            if (stackImageView.getVisibility() == View.VISIBLE) {
                stackImageView.setImageDrawable(game.previous);
                stackImageView.setTag(game.pre.getTag());
            }
            // 6. IF THE STACK IS INVISIBLE, UPDATE STACK IMAGE WITH CURRENT PLAYER'S CARD
            else {
                stackImageView.setImageDrawable(cardDrawable);
                stackImageView.setTag(selectedCard.getTag());
            }


            game.current = cardDrawable;

            // 7. UPDATES THE UI AND MAKES THE "DROPPED" CARDS INVISIBLE
            for (ImageView img_view : game.cardsSelected) {
                if (img_view.getTag() != null) {
                    cards.add((Integer) img_view.getTag());

                    if(cards.size()>55){
                        Button ex = findViewById(R.id.exitButton);
                        ex.performClick();

                    }
                }
                img_view.setVisibility(View.INVISIBLE);
                ViewParent parent = img_view.getParent();
                if (parent instanceof View) {
                    ((View) parent).invalidate();
                }


                game.droppedCard = img_view;
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) img_view.getLayoutParams();
                params.topMargin += 50;
                img_view.setLayoutParams(params);
                RelativeLayout rootView = findViewById(R.id.main);
                rootView.requestLayout();

            }


            stackImageView.setVisibility(View.VISIBLE);

            ///////////////////////////////////////////////////////////////////////////////////////////

            if (game.playerHand != null ) {
                List<Integer> playerHandList = game.playerHand.get(game.current_player);
                if (playerHandList != null && !playerHandList.isEmpty()) {
                    // Remove cards from the player's hand based on their indices
                    for (ImageView card : game.cardsSelected) {
                        playerHandList.remove(card);
                    }

                    game.playerHand.put(game.current_player, playerHandList);
                    handImageViews = getHand(game.current_player + 1);

                }
            }
            ///////////////////////////////////////////////////////////////////////////////////////////

        }

        // 8. INFORMS THE USER TO PICK A CARD FIRST
        else{
//            Toast.makeText(this, "Pick a card to drop" , Toast.LENGTH_LONG).show();

        }


    }


    // 1. PILE IS CLICKED

    /**
     * Allows the user to pick a card from the discard pile
     * Assigns the card on top of the pile to the player
     * Updates the discard pile with the most latest dropped card
     * @throws CloneNotSupportedException
     */
    @Override
    public void onPileClick() throws CloneNotSupportedException {

        // 2. CHECKS IF A CARD HAS BEEN DROPPED
        if(game.dropped && !game.begin){
            ImageView stackImageView = findViewById(R.id.stack);

            // 3. GETS THE IMAGEVIEW WHICH IS INVISIBLE I.E. CARD HAS BEEN DROPPED
            ImageView img = findEmptyImageView();
            if (img != null) {
                game.x++;
                img.setVisibility(View.VISIBLE);
                // 4. UPDATES THE STACK WITH THE PREVIOUS PLAYER'S CARD
                // AS TURN IS OVER AFTER PICKING A CARD AND CALLS NEXT TURN
                stackImageView.setImageDrawable(game.current);
                cards.remove((Integer) stackImageView.getTag());
            }

            // 5. ASSIGNS THE CARD ON TEH DECK TO THE PLAYER AND UPDATES THE UI
            gameController.onPileClick(img, stackImageView);


            if (game.x > Card.getCards().size() - 1) {
                game.iv_deck.setVisibility(View.INVISIBLE);
                Button showButton = findViewById(R.id.show);
                dispEndScores();
            }
            nextTurn();

        }

        // 6. TELLS THE USER TO DROP A CARD FIRST
        else if(!game.dropped){
            Toast.makeText(this, "Drop a card first" , Toast.LENGTH_LONG).show();
        }
        else if(game.begin){
            Toast.makeText(this, "Theres no card to pick yet!" , Toast.LENGTH_LONG).show();

        }

    }


//     1. DECK IS CLICKED

    /**
     * Allows the user to pick a card by clicking on the deck
     * @throws CloneNotSupportedException
     */
    @Override
    public void onDeckClick() throws CloneNotSupportedException {

        if(!game.show) {
            if (game.dropped && game.iv_deck.getVisibility() == View.VISIBLE) {

                // 2. CHECKS IF A CARD HAS BEEN DROP AND THERE ARE CARDS IN THE DECK
                if (game.dropped && game.iv_deck.getVisibility() == View.VISIBLE) {
                    // 3. GETS THE IMAGEVIEW WHICH IS INVISIBLE
                    ImageView img = findEmptyImageView();
                    if (img != null) {
                        // 4. ASSIGNS A CARD FROM THE SHUFFLED DECK
                        gameController.onDeckClick(img);
                        game.x++;
                        img.setVisibility(View.VISIBLE);
                        ImageView stackImageView = findViewById(R.id.stack);

                        // 5. UPDATES THE STACK WITH THE PREVIOUS PLAYER'S CARD
                        // AS TURN IS OVER AFTER PICKING A CARD AND CALLS NEXT TURN

                        stackImageView.setImageDrawable(game.current);
                    }

                }
                // 6. INFORMS THE USER TO DROP A CARD FIRST

//            System.out.println("game x in deck is " + game.x);
//            System.out.println("stuck here ");
                if (game.x > Card.getCards().size() - 1) {
//                System.out.println("end game ?");
                    game.iv_deck.setVisibility(View.INVISIBLE);
                    Button showButton = findViewById(R.id.show);
                    game.show = true;
                    dispEndScores();
                }
                nextTurn();
            }
            else {
                Toast.makeText(this, "Drop a card first", Toast.LENGTH_LONG).show();
            }
        }

    }


    /**
     * Finds the empty image views in the player's hands
     * @return
     */
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


    /**
     * Implements the next turn and moves the turn from the current player to the next
     * Changes UI by moving the highlight from current player to next
     * @throws CloneNotSupportedException
     */
    public void nextTurn() throws CloneNotSupportedException {
        if(!game.show){
            TextView timerTextView = findViewById(R.id.time); // Use the ID you assigned in XML

            timerTextView.setText("Time left: " + "- seconds");
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
                remainingTime = 0;
            }


            if (game.current_player != 0) {
                String beforelayout = "lay" + (game.current_player + 1);
                int resID = getResources().getIdentifier(beforelayout, "id", getPackageName());
                LinearLayout linearLayoutold = findViewById(resID);
                linearLayoutold.setBackgroundColor(Color.TRANSPARENT);
                linearLayoutold.setBackground(null);
                String img_name = "ai_av" + (game.current_player+1);
                int resIDImage = getResources().getIdentifier(img_name, "id", getPackageName());

                GradientDrawable borderDrawable = new GradientDrawable();
                borderDrawable.setStroke(5, Color.TRANSPARENT); // Set the border width and color
                ImageView img = findViewById(resIDImage);
                img.setBackground(borderDrawable);

            } else {
                String beforelayout = "lay" + 1;
                int resID = getResources().getIdentifier(beforelayout, "id", getPackageName());
                LinearLayout linearLayoutold = findViewById(resID);
                linearLayoutold.setBackgroundColor(Color.TRANSPARENT);
                linearLayoutold.setBackground(null);
                String img_name = "ai_av1";
                int resIDImage = getResources().getIdentifier(img_name, "id", getPackageName());

                GradientDrawable borderDrawable = new GradientDrawable();
                borderDrawable.setStroke(5, Color.TRANSPARENT); // Set the border width and color
                ImageView img = findViewById(resIDImage);

                img.setBackground(borderDrawable);

            }

            gameController.nextTurn();
            String layout = "lay" + (game.current_player + 1);
            int resID = getResources().getIdentifier(layout, "id", getPackageName());

            LinearLayout linearLayout = findViewById(resID);
            GradientDrawable border = new GradientDrawable();
            border.setStroke(12, Color.parseColor("#E17B26")); // Black border with width 2
            linearLayout.setBackground(border);

            String img_name = "ai_av" + (game.current_player+1);
            int resIDImage = getResources().getIdentifier(img_name, "id", getPackageName());

            ImageView image = findViewById(resIDImage);
            GradientDrawable borderDrawable = new GradientDrawable();
            borderDrawable.setShape(GradientDrawable.OVAL); // Set the shape to Oval for circular shape
            borderDrawable.setStroke(12, Color.parseColor("#E17B26")); // Black border with width 2
            borderDrawable.setSize(image.getWidth(), image.getHeight()); // Set the size of the border drawable to match the ImageView
            image.setBackground(borderDrawable);


            Button showButton = findViewById(R.id.show);
            System.out.println("check round " + currentRound);

            if(currentRound< numberOfRounds){
                calculateScores();
            }

            if (game.current_player == 0) {
                timerTextView.setText("Time left: " + "- seconds");
            }
            else{
                timerTextView.setText("Time left: " + "- seconds");
                if(Objects.equals(difficulty, "Easy")){
                    callGreedy(game.current_player + 1);
                }
                else if(Objects.equals(difficulty, "Medium")){
                    callMonte();

                }
                else{
                    callMinimize(game.current_player + 1);
                }
            }

            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
                remainingTime = 0;
            }
            timer();

            if(currentRound< numberOfRounds) {
                // MAKES THE SHOW BUTTON APPEAR ONLY IF SCORE IS <=5
                if (scores[currentRound][game.current_player] <= 5 && game.current_player == 0) {
                    showButton.setVisibility(View.VISIBLE);
                } else {
                    showButton.setVisibility(View.INVISIBLE);
                }
            }
        }

    }

    /**
     * Shows if scores <= 5
     * Calls the ISMCTS
     * @throws CloneNotSupportedException
     */
    public void callMonte() throws CloneNotSupportedException {
        State s = new State(this, game.current_player+1);

        ISMCTS monte = new ISMCTS();
        Button dropButton = findViewById(R.id.drop);
        if(game.playerHand!=null){
            Button showButton = findViewById(R.id.show);
            showButton.setVisibility(View.INVISIBLE);
            monte.show(getHand(game.current_player+1), game, showButton);

        }

        if(!game.show){
            monte.runInBackground(s, 100, getHand(game.current_player+1), dropButton, game, game.iv_deck, game.stack);

        }

    }


    /**
     * Gets the cards in the hands of the player
     * @param j - player whose hand the function returns
     * @return
     */
    public List<ImageView> getHand(int j) {

        List<ImageView> imageViewList =  new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            int imageViewId = getResources().getIdentifier("iv_p" + j + "c" + i, "id", getPackageName());
                ImageView img = findViewById(imageViewId);
                if(img.getVisibility()==View.VISIBLE){
                    imageViewList.add(img);
                }

            }
            return imageViewList;
    }




    // 1. CALCULATES THE SCORES FOR ALL THE PLAYERS

    /**
     * Calculates score for each player and updates it in the array 'scores'
     */
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

    /**
     * Shows toast displaying the player who clicked show and shows scoreboard popup
     * @param v
     * @throws CloneNotSupportedException
     */
    public void showScores(View v) throws CloneNotSupportedException {
        calculateScores();
        int win = scoreController.calculateMinScore();
        showCustomToastWithDelay(3000, Boolean.FALSE);

        scoreController.showScores(win, game);
        game.show=true;

        scorecard sc = new scorecard(this);
        sc.showScoreboardPopup(3);

    }

    /**
     * Shows toast for when deck is over and displays scoreboard
     * @throws CloneNotSupportedException
     */
    public void dispEndScores() throws CloneNotSupportedException {
        int win = scoreController.calculateMinScore();
        showCustomToastWithDelay(3000, Boolean.TRUE);
        game.show=true;
        scorecard sc = new scorecard(this);
        sc.showScoreboardPopup(3);

    }

    /**
     * Shows custom toast display
     * @param duration - for the toast to stay on the screen
     * @param deckOver - whether the deck is over or not
     */
    private void showCustomToastWithDelay(int duration, Boolean deckOver) {
        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_custom_toast, null);

        TextView textView = layout.findViewById(R.id.textViewToast);

        int playerShow = game.current_player+1;
        // Set the text of the TextView
        if(!deckOver){
            if(playerShow==1){
                textView.setText(name + " has clicked show");
            }
            else{
                textView.setText("Player " + playerShow + " has clicked show");
            }
        }

        else{
            textView.setText("Deck finished... Game Over ");
        }

        // Create and customize the toast
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        // Show the toast
        toast.show();

        // Hide the toast after the specified duration
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, duration);
    }


    /**
     * Begins the next round of the game if multiple or shows the final winner popup
     * @param v
     * @throws CloneNotSupportedException
     */
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


    /**
     * Calls the greedy AO
     * @param j - player whose turn it is
     */
    public void callGreedy(int j){
        if(!game.show){
            // show if score<= 5
            if(game.playerHand!=null && game.picked){
                Button showButton = findViewById(R.id.show);
                showButton.setVisibility(View.INVISIBLE);
                show(getHand(game.current_player+1), game, showButton);
            }

            int largest = 0;
            ImageView drop = null;
            if (isPaused) {
                return;
            }
            Button dropButton = findViewById(R.id.drop);

            // finds the image with the largest value
            for (int i = 1; i <= 5; i++) {
                int imageViewId = getResources().getIdentifier("iv_p" + j + "c" + i, "id", getPackageName());
                ImageView img = findViewById(imageViewId);
                if(img.getVisibility() == View.VISIBLE){

                    int cardNumber = (int) img.getTag();
                    if ((cardNumber % 100) > largest) {
                        largest = cardNumber % 100;
                        drop = img;
                    }
                }
            }

            // calls the greedy AI
            GreedyAI greedy = new GreedyAI();
            if (!(game.show)){
                greedy.greedyAI(j, drop, game, dropButton);

            }
        }

    }


    /**
     *
     * @param j
     */
    public void callMinimize(int j){
        int largest = 0;
        Boolean pickFromStack = false;
        ImageView drop = null;
        List<ImageView> list = new ArrayList<>();
        if(game.playerHand!=null){
            Button show = findViewById(R.id.show);
            show.setVisibility(View.INVISIBLE);
            MinimizeAI minimize = new MinimizeAI();
            minimize.show(getHand(game.current_player + 1), game, show);

        }


        Map<Integer, List<ImageView>> imageViewMap = new HashMap<>();

        if (isPaused) {
            return; // Exit the method if the game is paused
        }

        Button dropButton = findViewById(R.id.drop); // Replace R.id.myButton with your actual button ID
        ImageView stack = findViewById(R.id.stack);


        Integer stackCardNumber = (Integer) game.stack.getTag();

        List<ImageView> descList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            int imageViewId = getResources().getIdentifier("iv_p" + j + "c" + i, "id", getPackageName());
            ImageView img = findViewById(imageViewId);
            if(img.getVisibility() == View.VISIBLE){

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

            if (value > largestValue && key!=stackCardNumber%100) {
                largestKey = key;
                largestValue = value;
            }
        }

// Add the ImageViews corresponding to the largest key to the list
        list.addAll(imageViewMap.get(largestKey));

// Check if the total value in the list is greater than 'largest'
        int totalValue = 0;
        for (ImageView imageView : list) {

            totalValue += (Integer) imageView.getTag() % 100;
        }

        String source;

        if(pickFromStack){
            source = "pile";
        }
        else{
            source="deck";
        }

        if (totalValue >= largest) {

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


            MinimizeAI minimize = new MinimizeAI();
            minimize.minimizeAI(newList, drop, pickFromStack, game, dropButton, stack, source);
        }

    }


    /**
     * Initializes or resumes a countdown timer.
     *
     * This method starts a countdown timer and updates a TextView
     * with the remaining time. If there is remaining time and a valid timer, the method
     * resumes the timer from the remaining time. If there is no remaining time or no valid
     * timer, a new timer with the default duration (10,000 milliseconds) is started.
     *
     * The countdown timer ticks every 500 milliseconds. When the timer finishes, the method
     * calls the {@link #timerUp()} method to perform the desired action.
     *
     */
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


    /**
     * Handles actions to be performed when the timer finishes.
     * <p>
     * This method is called when the countdown timer initiated by the {@link #timer()} method
     * finishes. It contains logic to perform different actions based on the game state and user
     * interactions. If cards are picked and some cards are selected, it simulates button clicks
     * after certain delays. If no cards are dropped and no cards are selected, it calls the
     * {@link #callGreedy(int)} method to invoke the greedy AI. Otherwise, it simulates a button
     * click on the deck image view after a delay.
     * </p>
     */
    public void timerUp() {

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


    /**
     * Helps exit the game by navigating back to the start screen
     * @param v
     */
    public void exitGame(View v) {
        Intent intent=new Intent(MainActivity.this, StartScreen.class);
        startActivity(intent);
        finish();
    }


    /**
     * If score of a player is less than 5, clicks the show button
     * @param myCards
     * @param game
     * @param show
     */
    public void show(List<ImageView> myCards, Game game, Button show) {
        int val = 0;
        for (ImageView imageView : myCards) {
            int tag = (Integer) imageView.getTag();
            val += tag % 100;

        }

        if (val < 6) {
            show.performClick();
        }

    }

    public List<Move> getAllMoves() {
        List<Move> allMoves = new ArrayList<>();

        // Get the current player's hand
        List<Integer> currentPlayerHand = new ArrayList<>();
        currentPlayerHand.add(101);
        currentPlayerHand.add(305);
        currentPlayerHand.add(407);
        currentPlayerHand.add(200);
        currentPlayerHand.add(111);

        Integer largestCardWithoutSameRank = null; // Variable to store the largest card without the same rank

        for (Integer card : currentPlayerHand) {
            // Check for other cards with the same rank
            boolean cardNotInAllMoves = allMoves.stream()
                    .flatMap(move -> move.getCardsPlayed().stream())
                    .noneMatch(card::equals);

            if (cardNotInAllMoves) {
                List<Integer> cardsWithSameRank = getCardsWithSameRank(currentPlayerHand, card);

                // If there are cards with the same rank, create a move for playing them together with the pile
                if (!cardsWithSameRank.isEmpty()) {
                    cardsWithSameRank.add(card);  // Add the current card to the list
                    double probability = calculateProbability();

                    // Decide source based on probability
                    String source = probability < 0.6 ? "pile" : "deck";

                    Move movePile = new Move(cardsWithSameRank, source);
//                    Move moveDeck = new Move(cardsWithSameRank, "deck");

                    allMoves.add(movePile);
//                    allMoves.add(moveDeck);
                } else {
                    // If the current card is higher than the current largest card without the same rank, update it
                    if (largestCardWithoutSameRank == null || card % 100 > largestCardWithoutSameRank % 100) {
                        largestCardWithoutSameRank = card ;

                    }
                }
            }
        }

        // If there is a largest card without the same rank, add it to the moves
        if (largestCardWithoutSameRank != null) {
            double probability = calculateProbability();
            String source = probability < 0.6 ? "pile" : "deck";
            Move movePileSingle = new Move(Collections.singletonList(largestCardWithoutSameRank), source);
//            Move moveDeckSingle = new Move(Collections.singletonList(largestCardWithoutSameRank), "deck");

            allMoves.add(movePileSingle);
//            allMoves.add(moveDeckSingle);
        }


        for (Move move : allMoves) {
//            System.out.println(move);
        }

        return allMoves;

    }

    public void finishActivity() {
        finish(); // Finish the activity
    }


    public void callRandom(int j){
        if(game.playerHand!=null){
            Button showButton = findViewById(R.id.show);
            showButton.setVisibility(View.INVISIBLE);
            show(getHand(game.current_player+1), game, showButton);

        }


        Random random = new Random();

        // Initialize the random number
        int randomNumber;
        int imageViewId;
        ImageView img;

        // Repeat until a visible ImageView is found
        do {
            // Generate a random number between 1 and 5 (inclusive)
            randomNumber = random.nextInt(5 - 1 + 1) + 1;

            // Get the resource identifier for the ImageView
            imageViewId = getResources().getIdentifier("iv_p" + j + "c" + randomNumber, "id", getPackageName());

            // Find the ImageView using the resource identifier
            img = findViewById(imageViewId);

            // Check if the ImageView is visible
        } while (img.getVisibility()==View.INVISIBLE);

        if (!(game.show)){
            applyRandom(j, img);

        }
    }
    private List<Integer> getCardsWithSameRank(List<Integer> hand, int targetCard) {
        List<Integer> cardsWithSameRank = new ArrayList<>();
        for (Integer card : hand) {
            if (getRank(card) == getRank(targetCard) && !card.equals(targetCard)) {
                cardsWithSameRank.add(card);
            }
        }
        return cardsWithSameRank;
    }

    private int getRank(int card) {
        // Implement logic to extract and return the rank of the card
        // This depends on how your cards are represented, e.g., as integers where rank is a part of the integer
        return card % 100; // Assuming cards are represented as integers from 0 to 51
    }

    public double calculateProbability() {

        List<Integer> unseenCards = new ArrayList<>();
        unseenCards.add(201);
        unseenCards.add(400);
        unseenCards.add(300);
        unseenCards.add(302);
        unseenCards.add(405);
        unseenCards.add(209);
        unseenCards.add(304);
        unseenCards.add(213);
        unseenCards.add(311);
        unseenCards.add(203);
        unseenCards.add(210);
        unseenCards.add(110);
        unseenCards.add(313);
        unseenCards.add(104);
        unseenCards.add(103);
        unseenCards.add(408);
        unseenCards.add(306);
        unseenCards.add(105);
        unseenCards.add(310);
        unseenCards.add(207);


        List<Integer> discardedCards = new ArrayList<>();
        discardedCards.add(412);
        int topDiscard = discardedCards.isEmpty() ? 0 : discardedCards.get(discardedCards.size() - 1);

        // Count the number of cards smaller than the top discard in the deck
        int smallerCardsCount = 0;
        int topDiscardValue = topDiscard % 100;
        for (Integer card : unseenCards) {
            if (card % 100 < topDiscardValue) {
                smallerCardsCount++;
            }
        }

        // Calculate the probability of picking a smaller card from the deck
        double probability = (double) smallerCardsCount / unseenCards.size();

        return probability;

    }

    public void applyRandom(int player, ImageView img) {


        if (!game.show) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Perform the second click after a 2-second delay

                    img.performClick();
                }

            }, 500); // 2000 milliseconds = 2 seconds

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
                    // Check if the game is paused before performing the third click
                    game.iv_deck.performClick();

                }
            }, 500);
        }
    }





    public static ArrayList<Integer> makeTestList() {

        testCards.add(410);
        testCards.add(103);
        testCards.add(104);
        testCards.add(105);
        testCards.add(106);

        testCards.add(210);
        testCards.add(310);
        testCards.add(100);
        testCards.add(110);
        testCards.add(111);

        testCards.add(112);
        testCards.add(113);
        testCards.add(101);
        testCards.add(101);
        testCards.add(101);

        testCards.add(101);
        testCards.add(101);
        testCards.add(101);
        testCards.add(101);
        testCards.add(101);

        testCards.add(101);
        testCards.add(101);
        testCards.add(101);

        return testCards;


    }
}