package com.example.minimal;

import static com.example.minimal.Game.scores;
import static com.example.minimal.StartScreen.currentRound;
import static com.example.minimal.StartScreen.numberOfRounds;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class ScoreController {

    // 2. FINDS THE PLAYER WITH THE LEAST SCORE
    public int calculateMinScore(){
        int currentRound = StartScreen.currentRound;
        int minIndex = 0;
        // Find the minimum score for the current round
        for (int i = 0; i < scores[currentRound].length; i++) {
            if (scores[currentRound][i] < scores[currentRound][minIndex]) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    // 3. RECALCULATES THE SCORE IF NEEDED -> IF THE WINNER ISN'T THE PLAYER WHO SHOWED
    public void showScores(int win, Game game){
        if(win!=game.current_player){
            for(int i=0; i<4; i++){
                if(i==game.current_player){
                    game.scores[currentRound][game.current_player] = 20;
                }
                else{
                    game.scores[currentRound][i] = 0;
                }
            }
        }
    }

    // 4. CALCULATES THE TOTAL SCORE FOR EACH ROUND
    int[] calculateTotalScores() {
        int[] totalScores = new int[4];
        for (int player = 1; player <= 4; player++) {
            int playerTotal = 0;

            for (int round = 1; round <= numberOfRounds; round++) {
                if (round - 1 < scores.length && player - 1 < scores[round - 1].length) {
                    playerTotal += scores[round - 1][player - 1];
                } else {
                    System.out.println("Array index out of bounds.");
                }
            }
            totalScores[player - 1] = playerTotal;
        }
        return totalScores;
    }




}
