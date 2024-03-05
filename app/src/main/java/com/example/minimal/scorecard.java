package com.example.minimal;

import static com.example.minimal.Game.scores;
import static com.example.minimal.StartScreen.numberOfRounds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        // Empty cell for the top-left corner
        TextView emptyHeader = new TextView(context);
        emptyHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        headerRow.addView(emptyHeader);

        // Player Headers
        for (int player = 1; player <= 4; player++) {
            TextView playerHeader = new TextView(context);
            playerHeader.setText("Player " + player);
            playerHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            headerRow.addView(playerHeader);
        }

        // Add Header Row to the TableLayout
        tableLayout.addView(headerRow);

        // Dynamic Frame Headers
        for (int round = 1; round <= numberOfRounds; round++) {
            TableRow roundRow = new TableRow(context);
            roundRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // Round Header
            TextView roundHeader = new TextView(context);
            roundHeader.setText("Round " + round);
            roundHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
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
        totalRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        TextView totalHeader = new TextView(context);
        totalHeader.setText("Total Scores");
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
            totalCell.setText(Integer.toString(playerTotal));
            totalCell.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            totalRow.addView(totalCell);
        }

        // Add Total Row to the TableLayout
        tableLayout.addView(totalRow);

    }
}