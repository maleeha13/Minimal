package com.example.minimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import androidx.test.core.app.ApplicationProvider;

import org.robolectric.shadows.ShadowDrawable;

import java.util.ArrayList;
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Config.OLDEST_SDK})
public class CardTest {

    private ImageView imageView;

    @Before
    public void setUp() {
        // Initialize ImageView before each test
        Context context = ApplicationProvider.getApplicationContext(); // Get a valid context using Robolectric
        imageView = new ImageView(context);
    }

    @Test
    public void testMakeCardList() {
        ArrayList<Integer> cards = Card.makeCardList();
        int expectedSize = 52; // We expect the list to have 52 cards

        // Check if the size of the list is equal to 52
        assertEquals("The list should contain 52 cards", expectedSize, cards.size());
    }

    @Test
    public void testAssignImages() {
        // Mock ImageView
        ImageView imageView = mock(ImageView.class);

        // Set a drawable to the ImageView
        when(imageView.getDrawable()).thenReturn(mock(Drawable.class)); // Mock the Drawable returned by getDrawable()

        // Call the method to be tested
        Card.assignImages(102, imageView); // Assign an image for card 102 (clubs_2)

        // Verify if the correct tag is set
        verify(imageView).setTag(102);

        // Verify if setImageResource() is called with the correct resource ID
        verify(imageView).setImageResource(R.drawable.clubs_2);
    }



}
