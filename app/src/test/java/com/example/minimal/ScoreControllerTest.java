package com.example.minimal;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ScoreControllerTest {

    ScoreController scoreController;

    @Before
    public void setUp() {
        // Initialize your class
        scoreController = new ScoreController();
    }

    @Test
    public void testCalculateMinScore() {
        // Mock the StartScreen class to set the current round
        StartScreen.currentRound = 0; // Assuming current round is 0

        // Define sample scores for testing
        int[][] scores = {
                {10, 20, 30}, // Sample scores for round 0
                {15, 25, 35}, // Sample scores for round 1
                // Add more rounds as needed
        };

        // Set up yourClass to use the mock scores
        Game.scores = scores;

        // Call the method under test
        int minIndex = scoreController.calculateMinScore();

        // Assert that the minimum index is 0 (minimum score is at index 0 for round 0)
        assertEquals(0, minIndex);
    }
}
