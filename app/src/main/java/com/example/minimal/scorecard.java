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
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


/**
 * The scorecard class displays the scoreboard for the game.
 */
public class scorecard extends AppCompatActivity {
    public static AlertDialog dialog;
    Context context;
    Button nextButton ;

    /**
     * Constructs a scorecard object with the given context.
     * @param con The context for the scorecard.
     */
    public scorecard(Context con) {
        context = con;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);
    }


    // 5. DISPLAYS THE SCOREBOARD
    /**
     * Displays the scoreboard popup after a specified delay.
     * @param delayInSeconds The delay in seconds before displaying the scoreboard popup.
     */
    void showScoreboardPopup(int delayInSeconds ) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                View popupView = LayoutInflater.from(context).inflate(R.layout.activity_scorecard, null);

                // Create a TableLayout and add it to the popup
                TableLayout tableLayout = popupView.findViewById(R.id.tableLayout);
                nextButton = popupView.findViewById(R.id.nextButton);
                createScoreboard(tableLayout, 5); // 5 columns, adjust as needed
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dial2);
                builder.setView(popupView);
                SpannableString title = new SpannableString("Scoreboard");
                builder.setCancelable(false);

                title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setTitle(title);
                dialog = builder.create();
                dialog.show();
            }
        }, delayInSeconds * 1000);
    }

    /**
     * Creates the scoreboard and adds it to the specified TableLayout.
     *
     * @param tableLayout The TableLayout to which the scoreboard will be added.
     * @param numColumns The number of columns in the scoreboard.
     */
    private void createScoreboard(TableLayout tableLayout, int numColumns) {
        // Create Header Row
        int totalScores[] = new int[4]; // Array to store total scores for each player

        TableRow headerRow = new TableRow(context);
        TableRow.LayoutParams headerParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        headerRow.setLayoutParams(headerParams);


        TextView emptyHeader = new TextView(context);
        emptyHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        headerRow.addView(emptyHeader);


        for (int player = 1; player <= 4; player++) {
            TextView playerHeader = new TextView(context);
            if (player == 1) {
                playerHeader.setText(StartScreen.name);
            } else {
                playerHeader.setText("Pl " + player);
            }

            playerHeader.setTextSize(20);
            playerHeader.setTextColor(Color.WHITE);
            Typeface customFont = ResourcesCompat.getFont(context, R.font.pixel);
            playerHeader.setTypeface(customFont);
            playerHeader.setGravity(Gravity.CENTER);


            TableRow.LayoutParams playerParams = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1
            );
            playerParams.setMargins(0, 25, 0, 50);
            playerHeader.setLayoutParams(playerParams);

            headerRow.addView(playerHeader);
        }



        tableLayout.addView(headerRow);

        for (int round = 1; round <= numberOfRounds; round++) {
            TableRow roundRow = new TableRow(context);
            TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            );


            TextView roundHeader = new TextView(context);
            layoutParams.setMargins(0, 0, 0, 50);

            roundHeader.setText("Round " + round);
            roundHeader.setTextColor(Color.WHITE);

            Typeface customFont = ResourcesCompat.getFont(context, R.font.pixel);

            roundHeader.setTextSize(20);

            roundHeader.setTypeface(customFont);

            roundHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            layoutParams.setMargins(0, 0, 0, 50);
            roundRow.setLayoutParams(layoutParams);

            roundRow.addView(roundHeader);

            for (int player = 1; player <= 4; player++) {
                TextView frameCell = new TextView(context);

                if (round - 1 < scores.length && player - 1 < scores[round - 1].length) {
                    int score = scores[round - 1][player - 1];
                    frameCell.setText(Integer.toString(score));
                    frameCell.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                    frameCell.setGravity(Gravity.CENTER);
                    frameCell.setTextColor(Color.WHITE);

                    roundRow.addView(frameCell);
                } else {
                    System.out.println("Array index out of bounds.");
                }
            }

            tableLayout.addView(roundRow);
        }

        tableLayout.addView(new TableRow(context));

        TableRow totalRow = new TableRow(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 50);

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
                if (round - 1 < scores.length && player - 1 < scores[round - 1].length) {
                    playerTotal += scores[round - 1][player - 1];
                } else {
                    System.out.println("Array index out of bounds.");
                }
            }

            // Display the total score for the player
            TextView totalCell = new TextView(context);
            totalCell.setTextColor(Color.WHITE);

            totalCell.setText(Integer.toString(playerTotal));
            totalCell.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            totalCell.setGravity(Gravity.CENTER);

            totalRow.addView(totalCell);
        }

        tableLayout.addView(totalRow);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) nextButton.getLayoutParams();

        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        nextButton.setLayoutParams(layoutParams);

    }
}