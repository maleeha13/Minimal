package com.example.minimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Config.OLDEST_SDK})
public class MainActivityTest {

    private MainActivity mainActivity;
    private  Game game;



    @Before
    public void setUp() {
        mainActivity = spy(new MainActivity()); // Use spy to partially mock MainActivity
        mainActivity.init();
        Card.makeCardList();
        game= mainActivity.game;

    }

    @Test
    public void testOnCardClicked() throws CloneNotSupportedException {
        // Create a real instance of the Game object

        game.show = false; // Set the show state accordingly
        game.turns = new int[]{0}; // Assuming turns is set up accordingly
        game.current_player = 0;
        game.picked = true;
        game.cardsSelected = new ArrayList<>();

        // Mock ImageView
        ImageView imageView = mock(ImageView.class);

        // Mock LayoutParams
        LinearLayout.LayoutParams params = mock(LinearLayout.LayoutParams.class);
        when(imageView.getLayoutParams()).thenReturn(params);

        // Set a tag to mock the behavior of a clicked card
        when(imageView.getTag()).thenReturn(102); // Assuming the tag is 102

        // Mock the findViewById method to return the mocked ImageView
        doReturn(imageView).when(mainActivity).findViewById(anyInt());

        // Call the actual method on the real MainActivity object
        mainActivity.onCardClicked(102, imageView, 0);

        // Verify if the card is added to the list of selected cards
        game= mainActivity.game;


        assertTrue(game.cardsSelected.contains(imageView));

        // Verify if the margins of ImageView have been updated

    }

}
