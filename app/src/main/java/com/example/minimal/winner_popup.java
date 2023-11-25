package com.example.minimal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class winner_popup extends AppCompatActivity {

    private KonfettiView celeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("ENETRINGNGN ONCREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_popup);

        celeb = findViewById(R.id.celeb);
        celeb.build().addColors(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA).setDirection(0.0, 359.0).setSpeed(1f, 5f).setFadeOutEnabled(true).setTimeToLive(2000L).addShapes(Shape.RECT, Shape.CIRCLE).addSizes(new Size(12, 5)).setPosition(-50f, celeb.getWidth()+50f, -50f, -50f).streamFor(300, 5000L);
    }
}