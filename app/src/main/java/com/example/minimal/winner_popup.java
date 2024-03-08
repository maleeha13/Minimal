package com.example.minimal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minimal.R;
import com.example.minimal.StartScreen;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class winner_popup extends AppCompatActivity {

    private KonfettiView celeb;
    Context context;
    static boolean finished = false;

    Context c;
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
        celeb.build().addColors( Color.parseColor("#841616"), Color.parseColor("#D5A430"), Color.parseColor("#376A3A"), Color.parseColor("#564099"))

                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(-50f, celeb.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 5000L);

        c = this;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dial2);
        // Use the LayoutInflater from the context variable
        View winnerView = LayoutInflater.from(context).inflate(R.layout.activity_winner_popup, null);

        // Customize the winner message and score
        TextView winnerMessageTextView = winnerView.findViewById(R.id.winnerMessageTextView);
        if(winnerPlayer==1){
            winnerMessageTextView.setText(StartScreen.name+ " wins!");
        }
        else{
            winnerMessageTextView.setText("Player " + winnerPlayer + " wins!");
        }
        winnerMessageTextView.setTextColor(Color.WHITE); // Set text color


        TextView winnerScoreTextView = winnerView.findViewById(R.id.winnerScoreTextView);
        winnerScoreTextView.setText("Score: " + winnerScore);
        winnerScoreTextView.setTextColor(Color.WHITE); // Set text color


        // Get the KonfettiView reference
        KonfettiView celeb = winnerView.findViewById(R.id.celeb);

        // Trigger the confetti animation
        celeb.build()
                .addColors( Color.parseColor("#841616"), Color.parseColor("#D5A430"), Color.parseColor("#376A3A"), Color.parseColor("#564099"))
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

        ImageView imageView1 = winnerView.findViewById(R.id.imageView1);
        if(winnerPlayer==2){
            Drawable w2Drawable = context.getDrawable(R.drawable.w2); // Assuming w2.png is in the drawable folder
            imageView1.setImageDrawable(w2Drawable);
        }
        else if(winnerPlayer==3){
            Drawable w3Drawable = context.getDrawable(R.drawable.w3); // Assuming w2.png is in the drawable folder
            imageView1.setImageDrawable(w3Drawable);
        }
        else if(winnerPlayer==4){
            Drawable w4Drawable = context.getDrawable(R.drawable.w4); // Assuming w2.png is in the drawable folder
            imageView1.setImageDrawable(w4Drawable);
        }
        else{
            Drawable w1Drawable = context.getDrawable(R.drawable.p1); // Assuming w2.png is in the drawable folder
            imageView1.setImageDrawable(w1Drawable);
        }

        // Show the AlertDialog
        AlertDialog winnerDialog = builder.create();
        winnerDialog.show();

        Button finishButton = winnerView.findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StartScreen.class);
                context.startActivity(intent);

            }
        });


    }

}
