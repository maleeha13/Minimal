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

    public State(Context context, int player){
        this.context = context;
        this.discardedCards = new ArrayList<>();
        this.playerHand = new HashMap<>();

        discardedCards = MainActivity.discardedCards;
        playerHand.put(player, getPlayersHand(player));
        System.out.println(" it is player " + player);
        List<Integer> hand = getPlayersHand(player);
        System.out.println("Player " + player + "'s hand: " + handToString(hand));

        //retrieve and initialize values here
    }


    public State Clone(){
        try {
            State clonedState = (State) super.clone();

            // Perform deep cloning for mutable objects
            clonedState.discardedCards = new ArrayList<>(this.discardedCards);
            clonedState.playerHand = new HashMap<>(this.playerHand);

            // Additional deep cloning logic if needed

            return clonedState;
        } catch (CloneNotSupportedException e) {
            // This should not happen since KnockoutWhistState implements Cloneable
            throw new InternalError(e);
        }
    }


    public State CloneAndRandomize(int observer) throws CloneNotSupportedException {
        State st = (State) this.clone();

        // gets the player's hand
        List<Integer> observerHand = st.playerHand.get(observer);

        // Create a new list for seenCards and add the observer's hand
        List<Integer> seenCards = new ArrayList<>(observerHand);

        // seen cards = player's hand + discarded card
        seenCards.addAll(st.discardedCards);




        List<Integer> unseenCards = new ArrayList<>(Card.makeCardList());
        unseenCards.removeAll(seenCards);

        // shuffles the other cards
        Collections.shuffle(unseenCards);

        // deals the cards
        for (int p = 1; p <= 4; p++) {
            if (p != observer) {
                // Deal cards to player p
                // Store the size of player p's hand
                int numCards = st.playerHand.get(p).size();
                // Give player p the first numCards unseen cards
                st.playerHand.put(p, new ArrayList<>(unseenCards.subList(0, numCards)));
                // Remove those cards from unseenCards
                unseenCards.subList(0, numCards).clear();
            }
        }

        return st;



    }


    public void dealCards(){
        ArrayList<Integer> cards = (Card.getCards());
        Collections.shuffle(cards);
        discardedCards=  new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            List<Integer> hand = new ArrayList<>(cards.subList((i - 1) * 5, i * 5));
            playerHand.put(i, hand);
        }

    }

    public List<Integer> getPlayersHand(int player) {
        List<Integer> playerHand = new ArrayList<>(); // Initialize the list

        for (int i = 1; i < 6; i++) {
            int imageViewId = context.getResources().getIdentifier("iv_p" + player + "c" + i, "id", context.getPackageName());
            ImageView imageView = ((Activity) context).findViewById(imageViewId);

            if (imageView != null) {
                System.out.println("ImageView ID: " + imageViewId + " found.");
                if (imageView.getVisibility() == View.VISIBLE) {
                    playerHand.add((Integer) imageView.getTag());
                }
            } else {
                System.out.println("ImageView ID: " + imageViewId + " not found.");
            }
        }


        return playerHand;
    }

    public void doMove(int player, int drop, int pick){
        List<Integer> hand = playerHand.get(player);

        // Remove the drop value from the player's hand
        hand.remove(Integer.valueOf(drop));
        playerHand.put(player, hand);
        hand = playerHand.get(player);
        discardedCards.add(drop);

        // Adds the pick value
        hand.add(Integer.valueOf(pick));
        playerHand.put(player, hand);


    }

    public void getMoves(){

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

}
