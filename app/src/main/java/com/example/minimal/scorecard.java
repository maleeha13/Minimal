package com.example.minimal;

import static com.example.minimal.Game.scores;
import static com.example.minimal.StartScreen.numberOfRounds;
import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class scorecard extends AppCompatActivity {
    public static AlertDialog dialog;
    Context context;

    public scorecard(Context con) {
        context = con;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);
    }


    // 5. DISPLAYS THE SCOREBOARD
    void showScoreboardPopup(int delayInSeconds ) {
        // Delay the appearance of the scoreboard
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Inflate the layout for the popup
                View popupView = LayoutInflater.from(context).inflate(R.layout.activity_scorecard, null);

                // Create a TableLayout and add it to the popup
                TableLayout tableLayout = popupView.findViewById(R.id.tableLayout);
                createScoreboard(tableLayout, 5); // 5 columns, adjust as needed
                // Build the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                builder.setView(popupView);
                builder.setTitle("Scoreboard"); // Set the title as needed

                // Add any additional customization or buttons to the AlertDialog if needed

                // Show the AlertDialog
                dialog = builder.create();
                dialog.show();
//                System.out.println("init");
            }
        }, delayInSeconds * 1000); // Convert seconds to milliseconds
    }


    private void createScoreboard(TableLayout tableLayout, int numColumns) {
        // Create Header Row
        int totalScores[] = new int[4]; // Array to store total scores for each player

        TableRow headerRow = new TableRow(context);
        TableRow.LayoutParams headerParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        headerRow.setLayoutParams(headerParams);
        headerRow.setBackgroundColor(Color.parseColor("#E17B26")); // Set background color

// Empty cell for the top-left corner
        TextView emptyHeader = new TextView(context);
        emptyHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        headerRow.addView(emptyHeader);

// Player Headers
        for (int player = 1; player <= 4; player++) {
            TextView playerHeader = new TextView(context);
            if (player == 1) {
                playerHeader.setText(StartScreen.name);
            } else {
                playerHeader.setText("Pl " + player);
            }

            playerHeader.setTextSize(22); // Set text size to 22sp
            playerHeader.setTextColor(Color.WHITE); // Set text color
            Typeface customFont = ResourcesCompat.getFont(context, R.font.pixel);
            playerHeader.setTypeface(customFont);

            TableRow.LayoutParams playerParams = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1
            );
            playerParams.setMargins(0, 25, 0, 50); // Set bottom margin to 50px
            playerHeader.setLayoutParams(playerParams);

            headerRow.addView(playerHeader);
        }



        // Add Header Row to the TableLayout
        tableLayout.addView(headerRow);

        // Dynamic Frame Headers
        for (int round = 1; round <= numberOfRounds; round++) {
            TableRow roundRow = new TableRow(context);
            TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            );


            // Round Header
            TextView roundHeader = new TextView(context);
            layoutParams.setMargins(0, 0, 0, 50); // Set bottom margin to 20px

            roundHeader.setText("Round " + round);
            roundHeader.setTextColor(Color.WHITE);

            Typeface customFont = ResourcesCompat.getFont(context, R.font.pixel);

            roundHeader.setTextSize(21); // Set text size to 16sp

            roundHeader.setTypeface(customFont);

            roundHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            layoutParams.setMargins(0, 0, 0, 50); // Set bottom margin to 20px
            roundRow.setLayoutParams(layoutParams);

            roundRow.addView(roundHeader);

            // Create Player Cells for the Current Round
            for (int player = 1; player <= 4; player++) {
                TextView frameCell = new TextView(context);
                // Get the score from the game.scores array

                // Adjust the conditions here to match your array size
                if (round - 1 < scores.length && player - 1 < scores[round - 1].length) {
                    int score = scores[round - 1][player - 1];
                    frameCell.setText(Integer.toString(score));
                    frameCell.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                    frameCell.setGravity(Gravity.CENTER); // Center align the text
                    frameCell.setTextColor(Color.WHITE);

                    roundRow.addView(frameCell);
                } else {
                    // Handle the case where the array bounds are exceeded
                    System.out.println("Array index out of bounds.");
                }
            }

            // Add Round Row to the TableLayout
            tableLayout.addView(roundRow);
        }

        tableLayout.addView(new TableRow(context));

        // Add a row for total scores
        TableRow totalRow = new TableRow(context);
//        totalRow.setLayoutParams(new TableLayout.LayoutParams(
//                TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.WRAP_CONTENT));
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 50); // Set bottom margin to 20px

        TextView totalHeader = new TextView(context);
        totalHeader.setText("Total");
        totalHeader.setTextColor(Color.WHITE);

        totalHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        totalRow.addView(totalHeader);

        // Calculate and display the total score for each player
        for (int player = 1; player <= 4; player++) {
            int playerTotal = 0;

            // Sum up the scores for the player across all rounds
            for (int round = 1; round <= numberOfRounds; round++) {
                // Adjust the conditions here to match your array size
                if (round - 1 < scores.length && player - 1 < scores[round - 1].length) {
                    playerTotal += scores[round - 1][player - 1];
                } else {
                    // Handle the case where the array bounds are exceeded
                    System.out.println("Array index out of bounds.");
                }
            }

            // Display the total score for the player
            TextView totalCell = new TextView(context);
            totalCell.setTextColor(Color.WHITE);

            totalCell.setText(Integer.toString(playerTotal));
            totalCell.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            totalCell.setGravity(Gravity.CENTER); // Center align the text

            totalRow.addView(totalCell);
        }

        // Add Total Row to the TableLayout
        tableLayout.addView(totalRow);

    }
}