package com.example.minimal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.minimal.R;
import com.example.minimal.StartScreen;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class winner_popup extends AppCompatActivity {

    private KonfettiView celeb;
    Context context;

    public winner_popup(Context con) {
        context = con;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_popup);

        // Inflate the layout first
        View winnerView = getLayoutInflater().inflate(R.layout.activity_winner_popup, null);

        celeb = winnerView.findViewById(R.id.celeb);
        celeb.build().addColors(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(-50f, celeb.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 5000L);

        // Your onCreate method remains the same...
    }

    void displayWinner(ScoreController scoreController) {
        int[] totalScores = scoreController.calculateTotalScores();

        // Find the player with the lowest total score
        int winnerPlayer = 1;
        int winnerScore = totalScores[0];

        for (int player = 2; player <= 4; player++) {
            if (totalScores[player - 1] < winnerScore) {
                winnerPlayer = player;
                winnerScore = totalScores[player - 1];
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Use the LayoutInflater from the context variable
        View winnerView = LayoutInflater.from(context).inflate(R.layout.activity_winner_popup, null);

        // Customize the winner message and score
        TextView winnerMessageTextView = winnerView.findViewById(R.id.winnerMessageTextView);
        winnerMessageTextView.setText("Player " + winnerPlayer + " wins!");

        TextView winnerScoreTextView = winnerView.findViewById(R.id.winnerScoreTextView);
        winnerScoreTextView.setText("Score: " + winnerScore);

        // Get the KonfettiView reference
        KonfettiView celeb = winnerView.findViewById(R.id.celeb);

        // Trigger the confetti animation
        celeb.build()
                .addColors(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(-50f, celeb.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 5000L);

        builder.setView(winnerView);
        builder.setTitle("Game Over");

        // Show the AlertDialog
        AlertDialog winnerDialog = builder.create();
        winnerDialog.show();

        Button finishButton = winnerView.findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StartScreen.class);
                startActivity(intent);
            }
        });
    }

}
