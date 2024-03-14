package com.example.minimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class gameControllerTest {

    @Mock
    private Card cardMock;

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





    @Test
    public void testOnDeckClick_WhenConditionsMet() {
        // Set up the conditions for the test
        gameController.GameUIListener gameUIListener = mock(gameController.GameUIListener.class);
        Context context = mock(Context.class);

        // Create an instance of gameController with mock dependencies
        gameController gameCont = new gameController(gameUIListener, context);

        gameCont.game.dropped =true; // Set game.dropped to false
        ImageView mockImageView = mock(ImageView.class);
        gameCont.game.iv_deck = mockImageView;
        gameCont.game.iv_deck.setVisibility(View.VISIBLE);

        Card.makeCardList();

        gameCont.onDeckClick(mockImageView);
        // Verify that assign method is called with the correct arguments
        assertTrue(gameCont.game.picked); // Assuming there's a boolean field 'picked'
        assertFalse(gameCont.game.dropped); // As
    }


    @Test
    public void testOnDeckClick_WhenConditionsNotMet() {

        gameController.GameUIListener gameUIListener = mock(gameController.GameUIListener.class);
        Context context = mock(Context.class);

        // Create an instance of gameController with mock dependencies
        gameController gameCont = new gameController(gameUIListener, context);
        ImageView mockImageView = mock(ImageView.class);
        // Set up the conditions for the test
        gameCont.game.dropped =false; // Set game.dropped to false
        gameCont.game.iv_deck = mockImageView;

        gameCont.game.iv_deck.setVisibility(View.GONE);
        // Call the method under test
        Card.makeCardList();

        gameCont.onDeckClick(mockImageView);

        // Verify that assign method is not called
        assertTrue(gameCont.game.picked);
        assertFalse(gameCont.game.dropped);
    }




}
