package com.example.minimal;

import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Config.OLDEST_SDK, Config.NEWEST_SDK})
public class GreedyAITest {

    private GreedyAI greedyAI;
    private ImageView mockDrop;
    private Button mockDropButton;
    private Game mockGame;

    @Before
    public void setUp() {
        greedyAI = new GreedyAI();
        mockDrop = new ImageView(RuntimeEnvironment.application);
        mockDropButton = new Button(RuntimeEnvironment.application);
        mockGame = new Game();
    }

    @Test
    public void testGreedyAI() {
        greedyAI.greedyAI(0, mockDrop, mockGame, mockDropButton);
        assertTrue("Dropped image should be visible", mockDrop.getVisibility() == View.VISIBLE);

        // Add your assertions here
    }
}
