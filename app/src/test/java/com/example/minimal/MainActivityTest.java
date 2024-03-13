package com.example.minimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.widget.ImageView;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Config.OLDEST_SDK})
public class MainActivityTest {

    private MainActivity mainActivity;

    private gameController gameController;
    private Game gameTest;

    @Before
    public void setUp() throws CloneNotSupportedException {
        mainActivity = new MainActivity(); // Instantiate MainActivity
        mainActivity.init();


        // Set the mocked game object in the gameController

    }
    @Test
    public void testOnCardClicked() {
        // Mock ImageView
        ImageView imageView = mock(ImageView.class);
        when(imageView.getTag()).thenReturn(102); // Assuming the tag is 102
        mainActivity.game.current_player = 0; // Assuming current_player is 0
        mainActivity.game.picked = true; // Assuming picked is true
        mainActivity.game.turns[0] = 0; // Assuming turns is set up accordingly
        mainActivity.game.show = false; // Assuming show is false

        try {
            mainActivity.onCardClicked(102, imageView, 0);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        // Check if the card is added to the list of selected cards
        assertTrue(mainActivity.game.cardsSelected.contains(imageView));

        // Check if the topMargin of ImageView has been updated
        // Assuming the initial topMargin is 0 and decreases by 50 when selected
        assertEquals(-50, ((LinearLayout.LayoutParams) imageView.getLayoutParams()).topMargin);
    }
}
