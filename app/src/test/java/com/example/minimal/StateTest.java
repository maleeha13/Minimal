package com.example.minimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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


}
