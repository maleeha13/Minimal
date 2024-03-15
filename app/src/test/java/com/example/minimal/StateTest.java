package com.example.minimal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        State state = mock(State.class);

        List<Integer> player1Hand = Arrays.asList(101, 202, 303, 404);
        when(state.getPlayersHand(1)).thenReturn(player1Hand);


        List<Integer> player2Hand = Arrays.asList(102, 203, 304, 405);
        when(state.getPlayersHand(2)).thenReturn(player2Hand);

        state.discardedCards = Arrays.asList(100);

        double probability = state.calculateProbability();

        double expectedProbability = 0.0;

        assertEquals(expectedProbability, probability, 0.001);
    }

    @Test
    public void testGetAllMoves() {

        State state = new State( 1);

        Map<Integer, List<Integer>> playerHand = new HashMap<>();
        List<Integer> currentPlayerHand = Arrays.asList(101, 202, 303, 404, 302);
        playerHand.put(1, currentPlayerHand);
        state.playerHand = playerHand;
        state.discardedCards = Arrays.asList(100);

        List<Move> allMoves = state.getAllMoves(1);
        List<Move> expected = new ArrayList<>();

        List<Integer> cards1 = Arrays.asList(302, 202);
        Move move1 = new Move(cards1, "pile");
        expected.add(move1);

        List<Integer> cards2 = Collections.singletonList(404);
        Move move2 = new Move(cards2, "pile");
        expected.add(move2);

        assertEquals(expected, allMoves);
    }

    @Test
    public void testApplyMove() {
        State state = new State( 1);

        state.playerHand.put(1, new ArrayList<>(Arrays.asList(101, 202, 303)));
        state.discardedCards = new ArrayList<>(Arrays.asList(100, 200));

        List<Integer> cardsPlayed = Arrays.asList(202);
        Move move = new Move(cardsPlayed, "pile");

        state.applyMove(move, 1);


        List<Integer> expectedPlayerHand = Arrays.asList(101, 303, 100);
        assertEquals(expectedPlayerHand, state.playerHand.get(1));

        List<Integer> expectedDiscardedCards = Arrays.asList(202, 200);
        assertTrue("Dropped card should exist in discarded cards", expectedDiscardedCards.containsAll(state.discardedCards));

        assertEquals(move, state.getLastMove());

        List<Move> expectedPlayerTriedMoves = new ArrayList<>(Arrays.asList(move));
        assertEquals(expectedPlayerTriedMoves, state.triedMoves.get(1));

    }


    @Test
    public void testIsTerminal_SizeLessThan20() {
        State state = new State( 1);

        state.playerHand.put(1, new ArrayList<>(Arrays.asList(105, 202, 302))); // Player 1's initial hand
        state.discardedCards = Arrays.asList(100, 101, 102, 103, 104, 106, 107, 108, 109, 110, 111, 112, 113, 200, 201, 203, 204, 205, 206, 207 , 208, 209, 211, 212, 213, 300, 301, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 400, 401, 402, 403,404, 405, 406, 407, 408, 409, 410); // Example discarded cards

        state.updateUnseenCards();

        boolean terminal = state.isTerminal();

        assertTrue("Game should be terminal when size of unseen cards is less than 20", terminal);
    }

    @Test
    public void testIsTerminal_CanPlayerShow() {
        State state = new State( 1);

        state.playerHand.put(1, new ArrayList<>(Arrays.asList(101, 202, 302)));
        state.updateUnseenCards();
        state.canPlayerShow();

        boolean terminal = state.isTerminal();

        assertTrue("Game should be terminal when a player can show their hand", terminal);
    }


    @Test
    public void testCanPlayerShow_TotalValueLessThan6() {
        State state = new State( 1);

        List<Integer> player1Hand = new ArrayList<>(Arrays.asList(101, 202, 302));
        state.playerHand.put(1, player1Hand);

        boolean canShow = state.canPlayerShow();

        assertTrue("Player should be able to show their hand", canShow);
    }

    @Test
    public void testGetResult_PlayerHasLowestSum() {
        State state = new State( 1);

        Map<Integer, List<Integer>> playerHands = new HashMap<>();
        playerHands.put(1, new ArrayList<>(Arrays.asList(101, 202, 303)));
        playerHands.put(2, new ArrayList<>(Arrays.asList(102, 203, 304)));
        playerHands.put(3, new ArrayList<>(Arrays.asList(103, 204, 305)));
        state.playerHand = playerHands;

        boolean result = state.getResult(1);

        assertTrue("Player 1 should have the lowest sum", result);
    }

    @Test
    public void testGetResult_PlayerDoesNotHaveLowestSum() {
        State state = new State( 1);

        Map<Integer, List<Integer>> playerHands = new HashMap<>();
        playerHands.put(1, new ArrayList<>(Arrays.asList(101, 202, 303)));
        playerHands.put(2, new ArrayList<>(Arrays.asList(102, 203, 304)));
        playerHands.put(3, new ArrayList<>(Arrays.asList(103, 204, 305)));
        state.playerHand = playerHands;

        boolean result = state.getResult(2);

        assertFalse("Player 2 should not have the lowest sum", result);
    }

    @Test
    public void testGetScores() {
        State state = new State( 1);

        Map<Integer, List<Integer>> playerHands = new HashMap<>();
        playerHands.put(1, new ArrayList<>(Arrays.asList(101, 202, 303)));
        playerHands.put(2, new ArrayList<>(Arrays.asList(102, 203, 304)));
        playerHands.put(3, new ArrayList<>(Arrays.asList(103, 204, 305)));
        state.playerHand = playerHands;

        int[] scores = state.getScores(state);
        int[] expectedScores = {6, 9, 12};

        assertArrayEquals("Scores should match the expected values", expectedScores, scores);
    }


    @Test
    public void testGetCardsWithSameRank() {
        State state = new State( 1);

        List<Integer> hand = new ArrayList<>(Arrays.asList(101, 202, 403, 204, 305));

        int targetCard = 303;

        List<Integer> cardsWithSameRank = state.getCardsWithSameRank(hand, targetCard);

        List<Integer> expectedCardsWithSameRank = Arrays.asList(403);

        assertEquals("Cards with same rank should match the expected values", expectedCardsWithSameRank, cardsWithSameRank);
    }


    @Test
    public void testGetRank() {
        State state = new State( 1);
        int card = 105;

        int rank = state.getRank(card);
        int expectedRank = 5;

        assertEquals("Extracted rank should match the expected value", expectedRank, rank);
    }

    @Test
    public void testUpdateUnseenCards() {
        State state = new State( 1);

        state.playerHand.put(1, Arrays.asList(101, 102));
        state.playerHand.put(2, Arrays.asList(201, 202));
        state.playerHand.put(3, Arrays.asList(301, 302));
        state.playerHand.put(4, Arrays.asList(401, 402));

        state.discardedCards = Arrays.asList(201, 202, 301, 302);

        List<Integer> unseenCards = state.updateUnseenCards();

        List<Integer> expectedUnseenCards = Arrays.asList(
                103, 104, 105, 106, 107, 108, 100, 110, 111, 112, 113,
                203, 204, 205, 206, 207, 208, 200, 210, 211, 212, 213,
                303, 304, 305, 306, 307, 308, 300, 310, 311, 312, 313,
                403, 404, 405, 406, 407, 408, 400, 410, 411, 412, 413
        );

        assertEquals("Unseen cards should match the expected values", expectedUnseenCards, unseenCards);
    }

    @Test
    public void testGetNextPlayer() {
        State state = new State( 2);

        state.playerToMove = 2;
        int nextPlayer = state.getNextPlayer();
        assertEquals("Next player should be 3", 3, nextPlayer);

        state.playerToMove = 4;
        nextPlayer = state.getNextPlayer();
        assertEquals("Next player should be 1", 1, nextPlayer);
    }
}
