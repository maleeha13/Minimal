package com.example.minimal;

import static com.example.minimal.Game.scores;
import static com.example.minimal.StartScreen.numberOfRounds;
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
import android.widget.Toast;

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

    @Test
    public void testCalculateScores() {
        // Mock ImageView objects
        ImageView imageView1 = mock(ImageView.class);
        when(imageView1.getTag()).thenReturn(101);

        ImageView imageView2 = mock(ImageView.class);
        when(imageView2.getTag()).thenReturn(202);

        ImageView imageView3 = mock(ImageView.class);
        when(imageView3.getTag()).thenReturn(303);

        ImageView imageView4 = mock(ImageView.class);
        when(imageView4.getTag()).thenReturn(404);

        ImageView imageView5 = mock(ImageView.class);
        when(imageView5.getTag()).thenReturn(505);

        // Create an array of image views
        ImageView[] imageViews = {imageView1, imageView2, imageView3, imageView4, imageView5};

        // Call the method under test
        calculateScores(imageViews);

        // Verify that scores are calculated correctly
        int[][] expectedScores = new int[1][1];
        expectedScores[0][0]= 15;

        assertArrayEquals(expectedScores, game.scores);
    }

    private void calculateScores(ImageView[] imageViews) {
        scores = new int[1][1];
        int currentRound = StartScreen.currentRound;
        int score = 0;
        for (int j = 0; j < imageViews.length; j++) {
                ImageView img = imageViews[j];
                if (img != null && img.getVisibility() == View.VISIBLE) {
                    int cardNumber = (int) img.getTag();
                    score = score + (cardNumber % 100);
                }
        }
        game.scores[currentRound][0] = score;
    }

    public ImageView callGreedy(ImageView[] imageViews, int j) {
        ImageView drop = null;
        if (!game.show) {
            int largest = 0;


            // Find the image with the largest value
            for (ImageView img : imageViews) {
                if (img.getVisibility() == View.VISIBLE) {
                    int cardNumber = (int) img.getTag();
                    if ((cardNumber % 100) > largest) {
                        largest = cardNumber % 100;
                        drop = img;
                    }
                }
            }

            // Perform action with the largest image view

        }
        return drop;
    }


    @Test
    public void testCallGreedy() {
        // Mock ImageView objects
        ImageView imageView1 = mock(ImageView.class);
        when(imageView1.getTag()).thenReturn(101);
        when(imageView1.getVisibility()).thenReturn(View.VISIBLE);

        ImageView imageView2 = mock(ImageView.class);
        when(imageView2.getTag()).thenReturn(202);
        when(imageView2.getVisibility()).thenReturn(View.VISIBLE);

        ImageView imageView3 = mock(ImageView.class);
        when(imageView3.getTag()).thenReturn(303);
        when(imageView3.getVisibility()).thenReturn(View.VISIBLE);

        ImageView imageView4 = mock(ImageView.class);
        when(imageView4.getTag()).thenReturn(404);
        when(imageView4.getVisibility()).thenReturn(View.VISIBLE);

        ImageView imageView5 = mock(ImageView.class);
        when(imageView5.getTag()).thenReturn(505);
        when(imageView5.getVisibility()).thenReturn(View.VISIBLE);

        ImageView[] imageViews = {imageView1, imageView2, imageView3, imageView4, imageView5};

        // Call the method under test
        ImageView result = callGreedy(imageViews, 1);

        // Verify that the correct ImageView is returned
        assertEquals(imageView5, result);
    }




}
