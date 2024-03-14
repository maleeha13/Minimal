package com.example.minimal;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;

public class gameControllerTest {



    @Test
    public void testNextTurn() {
        // Set up the initial state


        // Create an instance of gameController
        gameController.GameUIListener gameUIListener = mock(gameController.GameUIListener.class);
        Context context = mock(Context.class);

        // Create an instance of gameController with mock dependencies
        gameController gameCont = new gameController(gameUIListener, context);
        gameCont.game.current_player = 2; // Assuming current player is 2
        // Call the method under test
        gameCont.nextTurn();

        // Verify if the current player is updated correctly
        assertEquals(3, gameCont.game.current_player); // Next player should be 3

        // Verify if the current player wraps around to 0 correctly
        gameCont.game.current_player = 3;
        gameCont.nextTurn();
        assertEquals(0, gameCont.game.current_player); // Next player should be 0

        // Verify if cardsSelected list is cleared
        assertTrue(gameCont.game.cardsSelected.isEmpty());

        // Verify if begin flag is set to false
        assertFalse(gameCont.game.begin);
    }
}
