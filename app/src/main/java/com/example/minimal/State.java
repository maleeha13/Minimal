package com.example.minimal;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The State class represents the state of the game at any given point.
 */
public class State implements Cloneable {
    /** List of discarded cards */
    protected List<Integer> discardedCards;

    /** Map representing each player's hand */
    protected Map<Integer, List<Integer>> playerHand;

    /** Context object for the game environment */
    private Context context;

    /** The index of the player whose turn it is to move */
    private Integer playerToMove;

    /** The last move made in the game */
    private Move lastMove;

    /** Map of moves tried by each player */
    private Map<Integer, List<Move>> triedMoves;


    /**
     * Constructs a new State object with the given context and player.
     * @param context The context of the game environment.
     * @param player The index of the player.
     */
    public State(Context context, int player){
        this.context = context;
        this.discardedCards = new ArrayList<>();
        this.playerHand = new HashMap<>();
        this.triedMoves = new HashMap<>();
        this.lastMove = null;
        playerToMove = player;
        discardedCards = new ArrayList<>(MainActivity.cards);
        playerHand.put(player, getPlayersHand(player));
        List<Integer> hand = getPlayersHand(player);
    }


    /**
     * Clones the current State object.
     * @return A deep copy of the State object.
     */
    @Override
    public State clone() {
        try {
            State clonedState = (State) super.clone();

            // Deep copy the discarded cards list
            clonedState.discardedCards = new ArrayList<>(this.discardedCards);

            // Deep copy the player hand map
            clonedState.playerHand = new HashMap<>();
            for (Map.Entry<Integer, List<Integer>> entry : this.playerHand.entrySet()) {
                clonedState.playerHand.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }


            // Deep copy the tried moves map
            clonedState.triedMoves = new HashMap<>();
            for (Map.Entry<Integer, List<Move>> entry : this.triedMoves.entrySet()) {
                List<Move> moves = new ArrayList<>();
                for (Move move : entry.getValue()) {
                    moves.add(move.clone()); // Assuming Move class also implements Cloneable
                }
                clonedState.triedMoves.put(entry.getKey(), moves);
            }

            // Deep copy the last move
            if (this.lastMove != null) {
                clonedState.lastMove = this.lastMove.clone(); // Assuming Move class also implements Cloneable
            }

            // Retain the same context reference or clone it if necessary
            clonedState.context = this.context;

            return clonedState;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported for State class", e);
        }
    }



    /**
     * Clones the current State object and randomizes it for the given observer.
     * @param observer The index of the observing player.
     * @return A deep copy of the State object with randomized information for the observer.
     * @throws CloneNotSupportedException Thrown if cloning is not supported for State class.
     */
    public State CloneAndRandomize(int observer) throws CloneNotSupportedException {
        State st = clone();
        List<Integer> observerHand = st.playerHand.get(observer);

        // Create a new list for seenCards and add the observer's hand
        List<Integer> seenCards = new ArrayList<>(observerHand);
        seenCards.addAll(discardedCards);
        List<Integer> unseenCards = new ArrayList<>(makeList());
        unseenCards.removeAll(seenCards);

        for(int i=0; i<4;i++){
            List<Integer> playerHand = Game.playerHand.get(i);
        }

        // shuffles the other cards
        Collections.shuffle(unseenCards);

        // deals the cards
        for (int p = 1; p <= 4; p++) {
            if (p != observer) {
                List<Integer> existingCards = Game.playerHand.get(p - 1);

                // Initialize player's hand with existing cards

                List<Integer> hand;
                int numExistingCards;
                // Add random cards to complete the hand up to 5 cards
                if(existingCards !=null){
                    hand = new ArrayList<>(existingCards);
                    numExistingCards = existingCards.size();

                }
                else{
                    hand = new ArrayList<>();
                    numExistingCards= 0;
                }
                if (numExistingCards < 5) {
                    int remainingCards = Math.min(5 - numExistingCards, unseenCards.size());
                    if (remainingCards > 0) {
                        hand.addAll(unseenCards.subList(0, remainingCards));
                        unseenCards.subList(0, remainingCards).clear();
                    }
                }
                st.playerHand.put(p, hand);
            }
        }

        return st;
    }


    /**
     * Calculates the probability of picking a smaller card from the unseen cards based on the top discarded card.
     * @return The probability of picking a smaller card.
     */
    public double calculateProbability() {

        List<Integer> unseenCards = updateUnseenCards();
        int topDiscard = discardedCards.isEmpty() ? 0 : discardedCards.get(0);
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

    /**
     * Retrieves the player's hand based on the player index.
     * @param player The index of the player.
     * @return The list of cards in the player's hand.
     */
    public List<Integer> getPlayersHand(int player) {
        List<Integer> playerHand = new ArrayList<>(); // Initialize the list

        for (int i = 1; i < 6; i++) {
            int imageViewId = context.getResources().getIdentifier("iv_p" + player + "c" + i, "id", context.getPackageName());
            ImageView imageView = ((Activity) context).findViewById(imageViewId);

            if (imageView != null) {
                if (imageView.getVisibility() == View.VISIBLE) {
                    playerHand.add((Integer) imageView.getTag());
                }
            } else {
            }
        }
        return playerHand;
    }


    /**
     * Retrieves all possible moves for the given player.
     * @param player The index of the player.
     * @return A list of possible moves for the player.
     */
    public List<Move> getAllMoves(int player) {
        List<Move> allMoves = new ArrayList<>();

        // Get the current player's hand
        List<Integer> currentPlayerHand = playerHand.get(player);
//        System.out.println("size here is " + playerHand.size());
        Integer largestCardWithoutSameRank = null; // Variable to store the largest card without the same rank
//        double prob = 0;
        boolean pickpile =false;
        for (Integer card : currentPlayerHand) {

            Integer discaredCard = discardedCards.get(discardedCards.size()-1);
            if((discaredCard % 100) == (card % 100)){

                pickpile=true;
//                prob = 1.0;
            }

            // Check for other cards with the same rank
            boolean cardNotInAllMoves = allMoves.stream()
                    .flatMap(move -> move.getCardsPlayed().stream())
                    .noneMatch(card::equals);


//            && card%100!=discaredCard%100
            if (cardNotInAllMoves  && card%100!=discaredCard%100) {
                List<Integer> cardsWithSameRank = getCardsWithSameRank(currentPlayerHand, card);
                // If there are cards with the same rank, create a move for playing them together with the pile
                if (!cardsWithSameRank.isEmpty() && (cardsWithSameRank.get(0)%100!=0)) {

                    cardsWithSameRank.add(card);  // Add the current card to the list


                    Double probability = calculateProbability();

//                    System.out.println(" prob is " + prob);
                    if(pickpile){
                        probability=0.0;

                    }

//
//                    Move movePile = new Move(cardsWithSameRank, "deck");
//                    allMoves.add(movePile);
//                    Move moveDeck = new Move(cardsWithSameRank, "pile");
//                    allMoves.add(moveDeck);
                    if (probability > 0.6 ||  updateUnseenCards().isEmpty()) {

                        Move movePile = new Move(cardsWithSameRank, "deck");

//                        System.out.println("there is a move");
                        allMoves.add(movePile);
                    } else {
//                        System.out.println("there is a move deck");

                        Move moveDeck = new Move(cardsWithSameRank, "pile");
                        allMoves.add(moveDeck);
                    }
                } else {
                    // If the current card is higher than the current largest card without the same rank, update it
                    if (largestCardWithoutSameRank == null || card % 100 > largestCardWithoutSameRank % 100) {
                        largestCardWithoutSameRank = card ;

                    }
                }
            }
        }

        // If there is a largest card without the same rank, add it to the moves
        if (largestCardWithoutSameRank != null ) {
            Double probability = calculateProbability();
//
//                Move movePileSingle = new Move(Collections.singletonList(largestCardWithoutSameRank), "deck");
//                allMoves.add(movePileSingle);
//                Move moveDeckSingle = new Move(Collections.singletonList(largestCardWithoutSameRank), "pile");
//                allMoves.add(moveDeckSingle);
            if(pickpile){
                probability=0.0;

            }
            if (probability > 0.6) {
                Move movePileSingle = new Move(Collections.singletonList(largestCardWithoutSameRank), "deck");
                allMoves.add(movePileSingle);
            } else {

                Move moveDeckSingle = new Move(Collections.singletonList(largestCardWithoutSameRank), "pile");
                allMoves.add(moveDeckSingle);
            }
        }

        return allMoves;

    }

    /**
     * Applies the specified move to the game state for the given player.
     * @param move The move to apply.
     * @param player The index of the player making the move.
     */
    public void applyMove(Move move, int player) {

        String source = move.getSource();
        List<Integer> cardsPlayed = move.getCardsPlayed();
        List<Move> playerTriedMoves = triedMoves.getOrDefault(player, new ArrayList<>());
        playerTriedMoves.add(move);
        triedMoves.put(player, playerTriedMoves);
        lastMove = move;
        for (Integer card : cardsPlayed) {
            discardedCards.add(card);
        }

        List<Integer> playerHandList = playerHand.get(player);
        if (playerHandList != null && !playerHandList.isEmpty()) {
            // Remove cards from the player's hand based on their indices
            for (Integer card : cardsPlayed) {
                playerHandList.remove(card);
            }
            // Update the player's hand in the playerHand map
            playerHand.put(player, playerHandList);

        } else {
            // Handle the case when playerHandList is null or empty
        }

        if(Objects.equals(source, "pile")){
            if (!discardedCards.isEmpty()) {
                // Remove the last added value from the discard pile

                int lastAddedCard= discardedCards.get(0);
                discardedCards.remove(0);
                playerHandList.add(lastAddedCard);
                playerHand.put(player, playerHandList);

                List<Integer> unseenCards = updateUnseenCards();
                int firstUnseenCard = unseenCards.get(0);
                playerHandList.add(firstUnseenCard);
                playerHand.put(player, playerHandList);
                // You can use lastAddedCard as needed
            } else {

                // Handle the case when the discard pile is empty
            }
        }

        else{
            List<Integer> unseenCards = updateUnseenCards();
            int firstUnseenCard = unseenCards.get(0);
            playerHandList.add(firstUnseenCard);
            playerHand.put(player, playerHandList);
        }

        this.playerToMove=getNextPlayer();
    }

    /**
     * Checks if the game is in a terminal state.
     * @return true if the game is in a terminal state, false otherwise.
     */
    public Boolean isTerminal(){
        List<Integer> card = updateUnseenCards();
        if(card.size()<20 || canPlayerShow() ){
            System.out.println();
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }


    /**
     * Checks if the current player can show their hand.
     * @return true if the player can show their hand, false otherwise.
     */
    public Boolean canPlayerShow(){
        List<Integer> playerHandList = playerHand.get(playerToMove);
        int totalValue = 0;

        for (Integer card : playerHandList) {
            totalValue += card % 100;
        }

        return totalValue < 6;
    }

    /**
     * Determines if the specified player has won the game.
     * @param player The index of the player to check.
     * @return true if the player has won, false otherwise.
     */
    public Boolean getResult(int player) {
        int[] sums = new int[playerHand.size()];

        // Calculate the sum of the last two digits for each player
        int i = 0;
        for (List<Integer> hand : playerHand.values()) {
            int sum = hand.stream().mapToInt(card -> card % 100).sum();
            sums[i++] = sum;
        }

        // Find the index with the lowest sum
        int lowestIndex = -1;
        int lowestSum = Integer.MAX_VALUE;

        for (i = 0; i < sums.length; i++) {
            if (sums[i] < lowestSum) {
                lowestSum = sums[i];
                lowestIndex = i + 1;  // Adding 1 to convert from 0-based index to 1-based index
            }
        }

        if(player==lowestIndex){
            return  Boolean.TRUE;
        }

        else{
            return  Boolean.FALSE;
        }
    }


    /**
     * Calculates the scores for each player based on the current game state.
     * @param st The current game state.
     * @return An array containing the scores for each player.
     */
    public int[] getScores(State st) {
        int[] sums = new int[st.playerHand.size()];
        int i = 0;
        for (List<Integer> hand : playerHand.values()) {
            int sum = hand.stream().mapToInt(card -> card % 100).sum();
            sums[i++] = sum;
        }

        return sums;
    }

    /**
     * Gets the cards with the same rank
     * @param hand - cards of the player
     * @param targetCard - rank
     * @return cards with same rank
     */
    private List<Integer> getCardsWithSameRank(List<Integer> hand, int targetCard) {
        List<Integer> cardsWithSameRank = new ArrayList<>();
        for (Integer card : hand) {
            if (getRank(card) == getRank(targetCard) && !card.equals(targetCard)) {
                cardsWithSameRank.add(card);
            }
        }
        return cardsWithSameRank;
    }

    /**
     * Gets the rank of the card
     * @param card
     * @return rank
     */
    private int getRank(int card) {
        return card % 100;
    }


    /**
     * Updates the unseen cards in the game
     * @return unseen cards
     */
    List<Integer> updateUnseenCards() {
        List<Integer> unseenCards = new ArrayList<>(makeList());
        for (int playerIndex = 1; playerIndex <= 4; playerIndex++) {
            List<Integer> playerHandList = playerHand.get(playerIndex);

            if (playerHandList != null && !playerHandList.isEmpty()) {
                unseenCards.removeAll(playerHandList);
            }
        }

        unseenCards.removeAll(discardedCards);
        return unseenCards;
    }

    /**
     * Retrieves the last move made in the game.
     * @return The last move made in the game.
     */
    public Move getLastMove() {
        return lastMove;
    }

    /**
     * Updates the next player to move
     * @return the next player to  move
     */
    public int getNextPlayer(){
        int next=0;
        if(this.playerToMove==4){
            next=1;
        }

        else{
            next = this.playerToMove++;
        }

        return next;
    }


    /**
     * Retrieves the index of the player who should make the next move.
     * @return The index of the player to make the next move.
     */
    public int getPlayerToMove(){
        System.out.println("player " + playerToMove);
        return playerToMove;
    }


    /**
     * Creates a list containing all the cards in the game.
     * @return A list containing all the cards in the game.
     */
    public static ArrayList<Integer> makeList() {

        ArrayList<Integer>  cards = new ArrayList<>();
        cards.add(102);
        cards.add(103);
        cards.add(104);
        cards.add(105);
        cards.add(106);
        cards.add(107);
        cards.add(108);
        cards.add(100);
        cards.add(110);
        cards.add(111);
        cards.add(112);
        cards.add(113);
        cards.add(101);

        cards.add(202);
        cards.add(203);
        cards.add(204);
        cards.add(205);
        cards.add(206);
        cards.add(207);
        cards.add(208);
        cards.add(200);
        cards.add(210);
        cards.add(211);
        cards.add(212);
        cards.add(213);
        cards.add(201);

        cards.add(302);
        cards.add(303);
        cards.add(304);
        cards.add(305);
        cards.add(306);
        cards.add(307);
        cards.add(308);
        cards.add(300);
        cards.add(310);
        cards.add(311);
        cards.add(312);
        cards.add(313);
        cards.add(301);

        cards.add(402);
        cards.add(403);
        cards.add(404);
        cards.add(405);
        cards.add(406);
        cards.add(407);
        cards.add(408);
        cards.add(400);
        cards.add(410);
        cards.add(411);
        cards.add(412);
        cards.add(413);
        cards.add(401);

        return cards;
    }
}