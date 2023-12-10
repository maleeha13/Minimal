package com.example.minimal;

import static com.example.minimal.Game.scores;
import static com.example.minimal.StartScreen.currentRound;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ScoreController {

int calculateMinScore(){

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

}
