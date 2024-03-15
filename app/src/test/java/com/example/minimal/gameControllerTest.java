package com.example.minimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import android.content.Context;

import android.view.View;
import android.widget.ImageView;


@RunWith(MockitoJUnitRunner.class)
public class gameControllerTest {

    @Mock
    private Card cardMock;

    @Test
    public void testNextTurn() {

        gameController.GameUIListener gameUIListener = mock(gameController.GameUIListener.class);
        Context context = mock(Context.class);

        gameController gameCont = new gameController(gameUIListener, context);
        gameCont.game.current_player = 2;
        gameCont.nextTurn();

        assertEquals(3, gameCont.game.current_player);
        gameCont.game.current_player = 3;
        gameCont.nextTurn();
        assertEquals(0, gameCont.game.current_player);

        assertTrue(gameCont.game.cardsSelected.isEmpty());

        assertFalse(gameCont.game.begin);
    }





    @Test
    public void testOnDeckClick_WhenConditionsMet() {
        gameController.GameUIListener gameUIListener = mock(gameController.GameUIListener.class);
        Context context = mock(Context.class);

        gameController gameCont = new gameController(gameUIListener, context);

        gameCont.game.dropped =true;
        ImageView mockImageView = mock(ImageView.class);
        gameCont.game.iv_deck = mockImageView;
        gameCont.game.iv_deck.setVisibility(View.VISIBLE);

        Card.makeCardList();

        gameCont.onDeckClick(mockImageView);

        assertTrue(gameCont.game.picked);
        assertFalse(gameCont.game.dropped);
    }


    @Test
    public void testOnDeckClick_WhenConditionsNotMet() {

        gameController.GameUIListener gameUIListener = mock(gameController.GameUIListener.class);
        Context context = mock(Context.class);

        gameController gameCont = new gameController(gameUIListener, context);
        ImageView mockImageView = mock(ImageView.class);

        gameCont.game.dropped =false;
        gameCont.game.iv_deck = mockImageView;

        gameCont.game.iv_deck.setVisibility(View.GONE);
        Card.makeCardList();

        gameCont.onDeckClick(mockImageView);

        assertTrue(gameCont.game.picked);
        assertFalse(gameCont.game.dropped);
    }


    @Test
    public void testSetGame() {
        gameController.GameUIListener gameUIListener = mock(gameController.GameUIListener.class);
        Context context = mock(Context.class);
        gameController controller = new gameController(gameUIListener, context);

        Game mockGame = new Game();

        controller.setGame(mockGame);

        Game actualGame = controller.game;

        assertEquals(mockGame, actualGame);
    }

}
