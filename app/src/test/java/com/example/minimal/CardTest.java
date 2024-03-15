package com.example.minimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import androidx.test.core.app.ApplicationProvider;


import java.util.ArrayList;
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Config.OLDEST_SDK})
public class CardTest {

    private ImageView imageView;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        imageView = new ImageView(context);
    }

    @Test
    public void testMakeCardList() {
        ArrayList<Integer> cards = Card.makeCardList();
        int expectedSize = 52;

        assertEquals("The list should contain 52 cards", expectedSize, cards.size());
    }

    @Test
    public void testAssignImages() {
        ImageView imageView = mock(ImageView.class);

        when(imageView.getDrawable()).thenReturn(mock(Drawable.class));

        Card.assignImages(102, imageView);

        verify(imageView).setTag(102);

        verify(imageView).setImageResource(R.drawable.clubs_2);
    }



}
