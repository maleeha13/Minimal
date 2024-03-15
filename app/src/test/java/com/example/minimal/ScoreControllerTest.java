package com.example.minimal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
        StartScreen.currentRound = 0;

        int[][] scores = {
                {10, 20, 30},
                {15, 25, 35},

        };

        Game.scores = scores;

        int minIndex = scoreController.calculateMinScore();

        assertEquals(0, minIndex);
    }

    @Test
    public void testShowScores_WinIsNotCurrentPlayer() {
        Game gameMock = mock(Game.class);
        gameMock.current_player=1;
        gameMock.scores = new int[1][4];

        scoreController.showScores(0, gameMock);

        int[][] expectedScores = {
                {0, 20, 0, 0},
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
        scoreController.showScores(1, gameMock);

        int[][] expectedScores = {
                {0, 0, 0, 0},
        };
        assertEquals(expectedScores, gameMock.scores);
    }

    @Test
    public void testCalculateTotalScores() {
        Game gameMock = mock(Game.class);
        int[][] scores = {
                {10, 20, 30, 40},
                {15, 25, 35, 45},
        };

        gameMock.scores = (scores);
        StartScreen.numberOfRounds = 2;

        int[] totalScores = scoreController.calculateTotalScores();

        int[] expectedTotalScores = {25, 45, 65, 85};

        assertArrayEquals(expectedTotalScores, totalScores);
    }
}
