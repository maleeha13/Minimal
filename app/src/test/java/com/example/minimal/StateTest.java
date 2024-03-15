package com.example.minimal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateTest {

    @Test
    public void testCalculateProbability() {
        // Mock the State object
        State state = mock(State.class);

        // Stub the player hand for player 1
        List<Integer> player1Hand = Arrays.asList(101, 202, 303, 404);
        when(state.getPlayersHand(1)).thenReturn(player1Hand);

        // Stub the player hand for player 2
        List<Integer> player2Hand = Arrays.asList(102, 203, 304, 405);
        when(state.getPlayersHand(2)).thenReturn(player2Hand);

        // Set up the discarded cards
        state.discardedCards = Arrays.asList(100);

        // Call the calculateProbability() method
        double probability = state.calculateProbability();

        double expectedProbability = 0.0;

        // Assert the result
        assertEquals(expectedProbability, probability, 0.001); // Provide a delta for double comparison
    }

    @Test
    public void testGetAllMoves() {
        // Mock the Context object

        State state = new State( 1);

        // Set up player hand and discarded cards
        Map<Integer, List<Integer>> playerHand = new HashMap<>();
        List<Integer> currentPlayerHand = Arrays.asList(101, 202, 303, 404, 302);
        playerHand.put(1, currentPlayerHand);
        state.playerHand = playerHand;
        state.discardedCards = Arrays.asList(100);

        // Stub the calculateProbability() method to return a fixed value


        // Call the getAllMoves() method
        List<Move> allMoves = state.getAllMoves(1);
        List<Move> expected = new ArrayList<>();

        List<Integer> cards1 = Arrays.asList(302, 202);
        Move move1 = new Move(cards1, "pile");
        expected.add(move1);

        List<Integer> cards2 = Collections.singletonList(404);
        Move move2 = new Move(cards2, "pile");
        expected.add(move2);

        // Perform assertions
        assertEquals(expected, allMoves);
        // Add more assertions as needed to verify the behavior of getAllMoves()
    }

    @Test
    public void testApplyMove() {
        // Create a mock State object
        State state = new State( 1); // Passing null for context, assuming it's not needed for this test

        // Set up initial state for testing
        state.playerHand.put(1, new ArrayList<>(Arrays.asList(101, 202, 303))); // Player 1's initial hand
        state.discardedCards = new ArrayList<>(Arrays.asList(100, 200)); // Discarded cards

        // Create a Move object representing the move made by the player
        List<Integer> cardsPlayed = Arrays.asList(202); // Cards played by the player
        Move move = new Move(cardsPlayed, "pile"); // Move played from pile

        // Apply the move
        state.applyMove(move, 1);

        // Verify the changes in the game state after the move

        // Check if player's hand has been updated correctly
        List<Integer> expectedPlayerHand = Arrays.asList(101, 303, 100); // Expected player hand after move
        assertEquals(expectedPlayerHand, state.playerHand.get(1));

        // Check if discarded cards have been updated correctly
        List<Integer> expectedDiscardedCards = Arrays.asList(202, 200); // Expected discarded cards after move
        assertTrue("Dropped card should exist in discarded cards", expectedDiscardedCards.containsAll(state.discardedCards));

        // Verify if the last move has been updated
        assertEquals(move, state.getLastMove());

        // Verify if the player tried moves have been updated
        List<Move> expectedPlayerTriedMoves = new ArrayList<>(Arrays.asList(move)); // Expected player tried moves after move
        assertEquals(expectedPlayerTriedMoves, state.triedMoves.get(1));

    }


    @Test
    public void testIsTerminal_SizeLessThan20() {
        // Create a mock State object
        State state = new State( 1); // Passing null for context, assuming it's not needed for this test

        // Set up initial state for testing
        state.playerHand.put(1, new ArrayList<>(Arrays.asList(105, 202, 302))); // Player 1's initial hand
        state.discardedCards = Arrays.asList(100, 101, 102, 103, 104, 106, 107, 108, 109, 110, 111, 112, 113, 200, 201, 203, 204, 205, 206, 207 , 208, 209, 211, 212, 213, 300, 301, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 400, 401, 402, 403,404, 405, 406, 407, 408, 409, 410); // Example discarded cards

        state.updateUnseenCards(); // To ensure there are unseen cards

        // Call the isTerminal method
        boolean terminal = state.isTerminal();

        // Verify if the game is considered terminal
        assertTrue("Game should be terminal when size of unseen cards is less than 20", terminal);
    }

    @Test
    public void testIsTerminal_CanPlayerShow() {
        // Create a mock State object
        State state = new State( 1); // Passing null for context, assuming it's not needed for this test

        // Set up initial state for testing
        state.playerHand.put(1, new ArrayList<>(Arrays.asList(101, 202, 302))); // Player 1's initial hand
        // Assume there are more than 20 unseen cards
        state.updateUnseenCards();
        // Make the player able to show their hand
        state.canPlayerShow();

        // Call the isTerminal method
        boolean terminal = state.isTerminal();

        // Verify if the game is considered terminal
        assertTrue("Game should be terminal when a player can show their hand", terminal);
    }


    @Test
    public void testCanPlayerShow_TotalValueLessThan6() {
        // Create a mock State object
        State state = new State( 1); // Passing null for context, assuming it's not needed for this test

        // Set up initial state for testing
        List<Integer> player1Hand = new ArrayList<>(Arrays.asList(101, 202, 302)); // Player 1's initial hand with total value less than 6
        state.playerHand.put(1, player1Hand); // Assign the hand to player 1

        // Call the canPlayerShow method
        boolean canShow = state.canPlayerShow();

        // Verify if the player can show their hand
        assertTrue("Player should be able to show their hand", canShow);
    }

    @Test
    public void testGetResult_PlayerHasLowestSum() {
        // Create a mock State object
        State state = new State( 1); // Passing null for context, assuming it's not needed for this test

        // Set up initial state for testing
        Map<Integer, List<Integer>> playerHands = new HashMap<>();
        playerHands.put(1, new ArrayList<>(Arrays.asList(101, 202, 303))); // Player 1's hand with sum of last two digits = 6
        playerHands.put(2, new ArrayList<>(Arrays.asList(102, 203, 304))); // Player 2's hand with sum of last two digits = 9
        playerHands.put(3, new ArrayList<>(Arrays.asList(103, 204, 305))); // Player 3's hand with sum of last two digits = 12
        state.playerHand = playerHands;

        // Call the getResult method for player 1
        boolean result = state.getResult(1);

        // Verify if player 1 has the lowest sum
        assertTrue("Player 1 should have the lowest sum", result);
    }

    @Test
    public void testGetResult_PlayerDoesNotHaveLowestSum() {
        // Create a mock State object
        State state = new State( 1); // Passing null for context, assuming it's not needed for this test

        // Set up initial state for testing
        Map<Integer, List<Integer>> playerHands = new HashMap<>();
        playerHands.put(1, new ArrayList<>(Arrays.asList(101, 202, 303))); // Player 1's hand with sum of last two digits = 6
        playerHands.put(2, new ArrayList<>(Arrays.asList(102, 203, 304))); // Player 2's hand with sum of last two digits = 9
        playerHands.put(3, new ArrayList<>(Arrays.asList(103, 204, 305))); // Player 3's hand with sum of last two digits = 12
        state.playerHand = playerHands;

        // Call the getResult method for player 2
        boolean result = state.getResult(2);

        // Verify if player 2 does not have the lowest sum
        assertFalse("Player 2 should not have the lowest sum", result);
    }

    @Test
    public void testGetScores() {
        // Create a mock State object
        State state = new State( 1); // Passing null for context, assuming it's not needed for this test

        // Set up initial state for testing
        Map<Integer, List<Integer>> playerHands = new HashMap<>();
        playerHands.put(1, new ArrayList<>(Arrays.asList(101, 202, 303))); // Player 1's hand with sum of last two digits = 6
        playerHands.put(2, new ArrayList<>(Arrays.asList(102, 203, 304))); // Player 2's hand with sum of last two digits = 9
        playerHands.put(3, new ArrayList<>(Arrays.asList(103, 204, 305))); // Player 3's hand with sum of last two digits = 8
        state.playerHand = playerHands;

        // Call the getScores method
        int[] scores = state.getScores(state);

        // Expected scores based on the sum of last two digits of each player's hand
        int[] expectedScores = {6, 9, 12};

        // Verify if the calculated scores match the expected scores
        assertArrayEquals("Scores should match the expected values", expectedScores, scores);
    }


    @Test
    public void testGetCardsWithSameRank() {
        // Create a new State object for testing (context can be null for this test)
        State state = new State( 1);

        // Define a hand of cards
        List<Integer> hand = new ArrayList<>(Arrays.asList(101, 202, 403, 204, 305));

        // Define the target card (card with rank 3)
        int targetCard = 303;

        // Call the getCardsWithSameRank method
        List<Integer> cardsWithSameRank = state.getCardsWithSameRank(hand, targetCard);

        // Expected cards with the same rank as the target card (cards 203 and 305)
        List<Integer> expectedCardsWithSameRank = Arrays.asList(403);

        // Verify if the identified cards match the expected cards with the same rank
        assertEquals("Cards with same rank should match the expected values", expectedCardsWithSameRank, cardsWithSameRank);
    }


    @Test
    public void testGetRank() {
        // Create a new State object for testing (context can be null for this test)
        State state = new State( 1);

        // Define a card with a rank of 5
        int card = 105;

        // Call the getRank method
        int rank = state.getRank(card);

        // Expected rank is 5
        int expectedRank = 5;

        // Verify if the extracted rank matches the expected rank
        assertEquals("Extracted rank should match the expected value", expectedRank, rank);
    }

    @Test
    public void testUpdateUnseenCards() {
        // Create a new State object for testing (context can be null for this test)
        State state = new State( 1);

        // Initialize player hands and discarded cards
        state.playerHand.put(1, Arrays.asList(101, 102)); // Player 1's hand
        state.playerHand.put(2, Arrays.asList(201, 202)); // Player 2's hand
        state.playerHand.put(3, Arrays.asList(301, 302)); // Player 3's hand
        state.playerHand.put(4, Arrays.asList(401, 402)); // Player 4's hand

        state.discardedCards = Arrays.asList(201, 202, 301, 302); // Discarded cards

        // Call the updateUnseenCards method
        List<Integer> unseenCards = state.updateUnseenCards();

        // Expected unseen cards are those not in any player's hand or in the discarded pile
        List<Integer> expectedUnseenCards = Arrays.asList(
                103, 104, 105, 106, 107, 108, 100, 110, 111, 112, 113,
                203, 204, 205, 206, 207, 208, 200, 210, 211, 212, 213,
                303, 304, 305, 306, 307, 308, 300, 310, 311, 312, 313,
                403, 404, 405, 406, 407, 408, 400, 410, 411, 412, 413
        );

        // Verify if the calculated unseen cards match the expected unseen cards
        assertEquals("Unseen cards should match the expected values", expectedUnseenCards, unseenCards);
    }

    @Test
    public void testGetNextPlayer() {
        // Create a new State object for testing (context can be null for this test)
        State state = new State( 2);

        // Test case 1: Current player is not the last player
        state.playerToMove = 2;
        int nextPlayer = state.getNextPlayer();
        assertEquals("Next player should be 3", 3, nextPlayer);

        // Test case 2: Current player is the last player
        state.playerToMove = 4;
        nextPlayer = state.getNextPlayer();
        assertEquals("Next player should be 1", 1, nextPlayer);
    }
}
