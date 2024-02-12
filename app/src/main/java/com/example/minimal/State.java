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

public class State implements Cloneable {
    private List<Integer> discardedCards;  // Replace String with the actual type of keys and values
    private Map<Integer, List<Integer>> playerHand;
    private Context context;
    private Integer playerToMove;
    private Move lastMove;
    private Map<Integer, List<Move>> triedMoves;


    public State(Context context, int player){
        this.context = context;
        this.discardedCards = new ArrayList<>();
        this.playerHand = new HashMap<>();
        this.triedMoves = new HashMap<>();
        this.lastMove = null;
        playerToMove = player;

        discardedCards = new ArrayList<>(MainActivity.cards);
//        System.out.println("lallala" + MainActivity.cards.size());
        playerHand.put(player, getPlayersHand(player));
        List<Integer> hand = getPlayersHand(player);
//        System.out.println("Player " + player + "'s hand: " + handToString(hand));
//        System.out.println("making a new state ");
//        System.out.println("size of discareded cards us " + discardedCards.size());

//        System.out.println("Player " + player + "'s hand: " + handToString(hand));

    }



    public Context getContext(){
        return this.context;
    }


//    @Override
//    public State clone() {
//        try {
//            return (State) super.clone();
//        } catch (CloneNotSupportedException e) {
//            throw new AssertionError("Cloning not supported for State class", e);
//        }
//    }
//


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

//            System.out.println("player hand og is " + playerHand.);

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




    public State CloneAndRandomize(int observer) throws CloneNotSupportedException {
        State st = clone();


        // Clear discarded cards

//        System.out.println("in clone + rand");
        // gets the player's hand
        List<Integer> observerHand = st.playerHand.get(observer);

        // Create a new list for seenCards and add the observer's hand
        List<Integer> seenCards = new ArrayList<>(observerHand);
//        System.out.println("size of seen cards before " + seenCards.size());


        // seen cards = player's hand + discarded card
        seenCards.addAll(discardedCards);
//        System.out.println("size of disc cards  " + discardedCards.size());
//        System.out.println( discardedCards);

//        System.out.println("size of disc cards MAIN " + MainActivity.cards.size());

//        System.out.println(discardedCards);
//        System.out.println("size of disc cards MAIN ACT " + MainActivity.discardedCards.size());



        List<Integer> unseenCards = new ArrayList<>(Card.makeCardList());
//        System.out.println("total " + unseenCards.size());

        unseenCards.removeAll(seenCards);
//        System.out.println("size of seen cards " + seenCards.size());

//        System.out.println("size of unseen cards " + unseenCards.size());

        // shuffles the other cards
        Collections.shuffle(unseenCards);

        // deals the cards
        for (int p = 1; p <= 4; p++) {
            if (p != observer) {
                // Deal cards to player p
                // Store the size of player p's hand

                // Give player p the first numCards unseen cards
                st.playerHand.put(p, new ArrayList<>(unseenCards.subList(0, 5)));
                // Remove those cards from unseenCards
                unseenCards.subList(0, 5).clear();
            }
        }

        // Print every player's hand
        for (int p = 1; p <=4; p++) {
            List<Integer> playerHand = st.playerHand.get(p);
        }

        // Print discarded cards
//        System.out.println("Discarded cards: " + handToString(MainActivity.discardedCards));

        return st;
    }


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



    private String handToString(List<Integer> hand) {
        StringBuilder sb = new StringBuilder("[");
        for (Integer card : hand) {
            sb.append(card).append(", ");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove the trailing comma and space
        }
        sb.append("]");
        return sb.toString();
    }

    public List<Move> getAllMoves(int player) {
        List<Move> allMoves = new ArrayList<>();

        // Get the current player's hand
        List<Integer> currentPlayerHand = playerHand.get(player);
//        System.out.println("player num is " + player);
//        System.out.println("played is " + playerHand.get(player) );

        // Iterate over each card in the player's hand
        for (int i = 0; i < currentPlayerHand.size(); i++) {
            Integer card = currentPlayerHand.get(i);

            // Check for other cards with the same rank

            boolean cardNotInAllMoves = allMoves.stream()
                    .flatMap(move -> move.getCardsPlayed().stream())
                    .noneMatch(card::equals);

            if(cardNotInAllMoves){
                List<Integer> cardsWithSameRank = getCardsWithSameRank(currentPlayerHand, card);


                // If there are cards with the same rank, create a move for playing them together with the pile
                if (!cardsWithSameRank.isEmpty()) {
                    cardsWithSameRank.add(card);  // Add the current card to the list
                    Move movePile = new Move(cardsWithSameRank, "pile");
                    Move moveDeck = new Move(cardsWithSameRank, "deck");

                    allMoves.add(movePile);
                    allMoves.add(moveDeck);

                    // Remove the processed cards from the player's hand
                } else {

                    // If there is only one card with the same rank, add it to the "pile" move
                    Move movePileSingle = new Move(Collections.singletonList(card), "pile");
                    Move moveDeckSingle = new Move(Collections.singletonList(card), "deck");

                    allMoves.add(movePileSingle);
                    allMoves.add(moveDeckSingle);
                }
            }

        }
//

//        System.out.println("Possible moves for Player " + player + ":");
//        for (Move move : allMoves) {
//            System.out.println(move);
////        }
//        for (Move move : allMoves) {
//            System.out.println(move);
//        }
        // You can add more logic here to generate additional moves based on game rules

        return allMoves;
    }


    public void applyMove(Move move, int player) {
        String source = move.getSource();
        List<Integer> cardsPlayed = move.getCardsPlayed();
        List<Move> playerTriedMoves = triedMoves.getOrDefault(player, new ArrayList<>());
        playerTriedMoves.add(move);
        triedMoves.put(player, playerTriedMoves);
        lastMove = move;
        for (Integer card : cardsPlayed) {
            // UPDATES DISCARDED CARD
//            System.out.println("cards are B4 " + MainActivity.cards.size());

            discardedCards.add(card);
//            System.out.println("cards are " + MainActivity.cards.size());
        }

        System.out.println("discarded cards are " + discardedCards);

        List<Integer> playerHandList = playerHand.get(player);
        if (playerHandList != null && !playerHandList.isEmpty()) {
            // Remove cards from the player's hand based on their indices
            for (Integer card : cardsPlayed) {
                // Remove the first occurrence of the card from the player's hand
                // UPDATES PLAYER CARD

                playerHandList.remove(card);
            }

            // Update the player's hand in the playerHand map
            playerHand.put(player, playerHandList);
        } else {
            // Handle the case when playerHandList is null or empty
            System.out.println("Player's hand is empty or not found.");
        }

        if(source == "pile"){
            if (!discardedCards.isEmpty()) {
                // Remove the last added value from the discard pile
                int lastAddedCard = discardedCards.remove(discardedCards.size() - 1);
                playerHandList.add(lastAddedCard);
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


//        System.out.println("dissscc " + discardedCards);

        this.playerToMove=getNextPlayer();


        // Implement logic to apply the specified move to the current state and return the new state.
        // Ensure that the move is valid before applying it.
    }
    // Other methods for your game

    public Boolean isTerminal(){
        List<Integer> card = updateUnseenCards();
//        System.out.println("CARD SIZE HERE  ====" + card.size());

        if(card.isEmpty()){
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }

    public Boolean getResult(int player) {
        // Assuming playerHand is a Map<Integer, List<Integer>> where the key is the player index


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



    List<Integer> updateUnseenCards() {
        // Clear the current unseenCards list
        List<Integer> unseenCards = new ArrayList<>(Card.makeCardList());
//        System.out.println("uneen cards og is " + unseenCards.size());

        // Add all cards to the unseenCards list

        // Remove cards from players' hands
        for (int playerIndex = 1; playerIndex <= 4; playerIndex++) {
            List<Integer> playerHandList = playerHand.get(playerIndex);

            if (playerHandList != null && !playerHandList.isEmpty()) {
                // Remove cards from the unseenCards list
                unseenCards.removeAll(playerHandList);

            }
        }

        // Remove discarded cards from the unseenCards list
        unseenCards.removeAll(discardedCards);
//        System.out.println(" removing disared " + discardedCards.size());


        return unseenCards;
    }
    public Move getLastMove() {
        return lastMove;
    }

    public int getNextPlayer(){
//        System.out.println("changing player.....");
        int next=0;
        if(this.playerToMove==4){
            next=1;
        }

        else{
            next = this.playerToMove++;
        }

        return next;
    }

    public int getPlayerToMove(){
         return playerToMove;
    }

    public List<Integer> getDiscardedCards() {
        return discardedCards;
    }
}
