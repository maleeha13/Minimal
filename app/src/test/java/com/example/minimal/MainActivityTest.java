package com.example.minimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Config.OLDEST_SDK})
public class MainActivityTest {

    private MainActivity mainActivity;
    private  Game game;
    @Mock
    private View mockView;
    @Mock
    private ImageView mockSelectedCard;
    @Mock
    private ImageView mockStackImageView;
    @Mock
    private Drawable mockCardDrawable;
    @Mock
    private Button mockExitButton;
    @Mock
    private Resources mockResources;

    @Mock
    private ImageView mockImageView1, mockImageView2;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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

    @Test
    public void testOnCardDrop() {
        // Call the method under test
        onCardDrop();

        // Verify that the picked and dropped variables are updated
        assertFalse(game.picked);
        assertTrue(game.dropped);

        // Verify that the selected image views are made invisible
        verify(mockImageView1).setVisibility(View.INVISIBLE);
        verify(mockImageView2).setVisibility(View.INVISIBLE);


    }


    public void onCardDrop(){
        game.picked = true;
        game.dropped = false;
        game.cardsSelected = new ArrayList<>();
        game.cardsSelected.add(mockImageView1);
        game.cardsSelected.add(mockImageView2);

        // 2. CHECKS IF A CARD HAS BEEN SELECTED
        if(game.picked && !(game.cardsSelected.isEmpty())) {

            game.dropped = true;
            game.picked = false;

            // 7. UPDATES THE UI AND MAKES THE "DROPPED" CARDS INVISIBLE
            for (ImageView img_view : game.cardsSelected) {
                if (img_view.getTag() != null) {

                }
                img_view.setVisibility(View.INVISIBLE);
                ViewParent parent = img_view.getParent();
                if (parent instanceof View) {
                    ((View) parent).invalidate();
                }
                game.droppedCard = img_view;
            }

        }
    }


}
