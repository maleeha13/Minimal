package com.example.minimal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void testShowScores_WinIsNotCurrentPlayer() {
        // Mock the Game class
        Game gameMock = mock(Game.class);
        gameMock.current_player=1;
        gameMock.scores = new int[1][4];

        // Call the method under test
        scoreController.showScores(0, gameMock); // Assuming win is not the current player

        // Verify that the scores array is updated correctly
        int[][] expectedScores = {
                {0, 20, 0, 0}, // Assuming current player is 1, other scores are set to 0
                // Add more rounds as needed
        };
        assertEquals(expectedScores, gameMock.scores);
    }

    @Test
    public void testShowScores_WinIsCurrentPlayer() {
        // Mock the Game class
        Game gameMock = mock(Game.class);
        gameMock.current_player=1;
        gameMock.scores = new int[1][4];

        // Call the method under test
        scoreController.showScores(1, gameMock); // Assuming win is the current player

        // Verify that the scores array is updated correctly
        int[][] expectedScores = {
                {0, 0, 0, 0}, // Assuming current player is 1, their score is set to 20, others to 0
                // Add more rounds as needed
        };
        assertEquals(expectedScores, gameMock.scores);
    }

    @Test
    public void testCalculateTotalScores() {
        Game gameMock = mock(Game.class);
        // Mock the scores array for testing
        int[][] scores = {
                {10, 20, 30, 40}, // Sample scores for round 1
                {15, 25, 35, 45}, // Sample scores for round 2
                // Add more rounds as needed
        };

        // Set the scores array in the ScoreController
        gameMock.scores = (scores);
        StartScreen.numberOfRounds = 2;

        // Call the method under test
        int[] totalScores = scoreController.calculateTotalScores();

        // Define the expected total scores
        int[] expectedTotalScores = {25, 45, 65, 85}; // Calculated manually based on sample scores

        // Assert that the calculated total scores match the expected total scores
        assertArrayEquals(expectedTotalScores, totalScores);
    }
}
