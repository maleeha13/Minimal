package com.example.minimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.util.Arrays;
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

        // Expected probability calculation
        // The top discard is 300, so there are 2 cards (101 and 202) smaller than it in the unseen cards
        // The total number of unseen cards is 4
        // Therefore, the expected probability is 2 / 4 = 0.5
        double expectedProbability = 0.0;

        // Assert the result
        assertEquals(expectedProbability, probability, 0.001); // Provide a delta for double comparison
    }
}
