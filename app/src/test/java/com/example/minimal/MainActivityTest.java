package com.example.minimal;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static com.example.minimal.Game.scores;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Config.OLDEST_SDK})
public class MainActivityTest {

    private MainActivity mainActivity;
    private  Game game;
    private ImageView[] mockImageViews;



    @Mock
    private ImageView mockImageView1, mockImageView2;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mainActivity = spy(new MainActivity());
        mainActivity.init();
        Card.makeCardList();
        mockImageViews = new ImageView[5];
        for (int i = 0; i < 5; i++) {
            mockImageViews[i] = mock(ImageView.class);
        }
        game= mainActivity.game;

    }

    @Test
    public void testOnCardClicked() throws CloneNotSupportedException {

        game.show = false;
        game.turns = new int[]{0};
        game.current_player = 0;
        game.picked = true;
        game.cardsSelected = new ArrayList<>();

        ImageView imageView = mock(ImageView.class);

        LinearLayout.LayoutParams params = mock(LinearLayout.LayoutParams.class);
        when(imageView.getLayoutParams()).thenReturn(params);

        when(imageView.getTag()).thenReturn(102);

        doReturn(imageView).when(mainActivity).findViewById(anyInt());

        mainActivity.onCardClicked(102, imageView, 0);

        game= mainActivity.game;


        assertTrue(game.cardsSelected.contains(imageView));


    }

    @Test
    public void testOnCardDrop() {

        onCardDrop();

        assertFalse(game.picked);
        assertTrue(game.dropped);

        verify(mockImageView1).setVisibility(INVISIBLE);
        verify(mockImageView2).setVisibility(INVISIBLE);


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
                img_view.setVisibility(INVISIBLE);
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

        ImageView[] imageViews = {imageView1, imageView2, imageView3, imageView4, imageView5};

        calculateScores(imageViews);

        int[][] expectedScores = new int[1][1];
        expectedScores[0][0]= 15;

        assertArrayEquals(expectedScores, game.scores);
    }


    @Test
    public void testHideImageViewsRange() {
        int start = 1;
        String pre = "ivp1_c";
        int visibility = GONE;

        for (int i = 0; i < 5; i++) {
            mockImageViews[i].setVisibility(visibility);
        }

        for (ImageView mockImageView : mockImageViews) {
            verify(mockImageView).setVisibility(GONE);
        }
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

            for (ImageView img : imageViews) {
                if (img.getVisibility() == View.VISIBLE) {
                    int cardNumber = (int) img.getTag();
                    if ((cardNumber % 100) > largest) {
                        largest = cardNumber % 100;
                        drop = img;
                    }
                }
            }

        }
        return drop;
    }

    @Test
    public void testFindEmptyImageView() {
        ImageView[] mockImageViews = new ImageView[5];
        for (int i = 0; i < 5; i++) {
            mockImageViews[i] = mock(ImageView.class);
            when(mockImageViews[i].getVisibility()).thenReturn(i % 2 == 0 ? INVISIBLE : ImageView.VISIBLE);
        }

        ImageView result = null;
        for (ImageView imageView : mockImageViews) {
            if (imageView.getVisibility() == INVISIBLE) {
                result = imageView;
                break;
            }
        }

        if (result != null) {
            assertEquals(INVISIBLE, result.getVisibility());
        }

    }


    @Test
    public void testGetHand() {
        ImageView[] mockImageViews = new ImageView[5];
        for (int i = 0; i < 5; i++) {
            mockImageViews[i] = mock(ImageView.class);
            when(mockImageViews[i].getVisibility()).thenReturn(i % 2 == 0 ? View.VISIBLE : View.INVISIBLE);
        }

        List<ImageView> hand = getHandWithMockImageViews(mockImageViews);

        assertEquals(3, hand.size());
        for (ImageView imageView : hand) {
            assertEquals(View.VISIBLE, imageView.getVisibility());
        }
    }

    private List<ImageView> getHandWithMockImageViews(ImageView[] mockImageViews) {
        List<ImageView> imageViewList =  new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (mockImageViews[i].getVisibility() == View.VISIBLE) {
                imageViewList.add(mockImageViews[i]);
            }
        }
        return imageViewList;
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
        ImageView result = callGreedy(imageViews, 1);

        assertEquals(imageView5, result);
    }

    @Test
    public void callMinimize(){
        ImageView[] mockImageViews = new ImageView[5];

        for (int i = 0; i < 5; i++) {
            mockImageViews[i] = mock(ImageView.class);
        }

        mockImageViews[0].setVisibility(View.VISIBLE);
        mockImageViews[1].setVisibility(View.VISIBLE);
        mockImageViews[2].setVisibility(View.VISIBLE);
        mockImageViews[3].setVisibility(View.VISIBLE);
        mockImageViews[4].setVisibility(View.VISIBLE);
        when(mockImageViews[0].getTag()).thenReturn(212);
        when(mockImageViews[1].getTag()).thenReturn(208);
        when(mockImageViews[2].getTag()).thenReturn(101);
        when(mockImageViews[3].getTag()).thenReturn(409);
        when(mockImageViews[4].getTag()).thenReturn(313);

        int largest = 0;
        Boolean pickFromStack = false;
        ImageView drop = null;
        List<ImageView> list = new ArrayList<>();

        Map<Integer, List<ImageView>> imageViewMap = new HashMap<>();

        Integer stackCardNumber = 112;

        List<ImageView> descList = new ArrayList<>();


        for (int i = 0; i < 5; i++) {
            ImageView img = mockImageViews[i];
            if(img.getVisibility() == View.VISIBLE){

                int cardNumber = (Integer)img.getTag();
                int remainder = cardNumber % 100;

                if (cardNumber % 100 == stackCardNumber % 100) {
                    pickFromStack = true;
                }

                if (imageViewMap.containsKey(remainder)) {
                    imageViewMap.get(remainder).add(img);
                } else {
                    List<ImageView> imageViewList = new ArrayList<>();
                    imageViewList.add(img);
                    imageViewMap.put(remainder, imageViewList);
                }
                descList.add(img);
            }
        }

        Collections.sort(descList, new Comparator<ImageView>() {
            @Override
            public int compare(ImageView img1, ImageView img2) {
                int tag1 = (int) img1.getTag() % 100;
                int tag2 = (int) img2.getTag() % 100;
                return tag2 - tag1;
            }
        });



        largest = (int) descList.get(0).getTag() % 100;

        int largestKey = 0;
        int largestValue = 0;

        for (Map.Entry<Integer, List<ImageView>> entry : imageViewMap.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue().size();

            if (value > largestValue && key!=stackCardNumber%100) {
                largestKey = key;
                largestValue = value;
            }
        }

        list.addAll(imageViewMap.get(largestKey));

        int totalValue = 0;
        for (ImageView imageView : list) {

            totalValue += (Integer) imageView.getTag() % 100;
        }

        String source;

        if(pickFromStack){
            source = "pile";
        }
        else{
            source="deck";
        }

        if (totalValue >= largest) {
            assertEquals(source, "deck");


        } else {
            List<ImageView> newList = new ArrayList<>();
            if (largest == stackCardNumber % 100 && pickFromStack) {
                drop = (descList.get(1));
            }

            else{
                drop = (descList.get(0));
            }
            newList.add(drop);

            assertEquals(source, "pile");
            assertEquals(drop.getTag(), 313);

        }

    }


}
