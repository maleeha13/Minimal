package com.example.minimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EvalTests {
    static ArrayList<Integer> testCards = new ArrayList<>();


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
}
