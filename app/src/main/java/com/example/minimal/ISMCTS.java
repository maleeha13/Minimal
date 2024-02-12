package com.example.minimal;


import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public class ISMCTS {

    public void runInBackground(State rootState, int itermax, List<ImageView> hand, Button dropButton, Game game, ImageView deck, ImageView stack) {
        new MonteCarloTask(rootState, itermax, hand, dropButton, game, deck, stack).execute();
    }

    private class MonteCarloTask extends AsyncTask<Void, Void, Move> {
        private State rootState;
        private int itermax;
        private List<ImageView> hand;
        private Button dropButton;
        private Game game;
        private ImageView deck;
        private ImageView stack;

        public MonteCarloTask(State rootState, int itermax, List<ImageView> hand, Button dropButton, Game game, ImageView deck, ImageView stack) {
            this.rootState = rootState;
            this.itermax = itermax;
            this.hand = hand;
            this.dropButton= dropButton;
            this.game = game;
            this.deck = deck;
            this.stack = stack;
        }

        @Override
        protected Move doInBackground(Void... voids) {
            MCTSNode rootNode = new MCTSNode(rootState, null, 0, null);
            expandNode(rootNode, rootState);

            for (int i = 0; i < itermax; i++) {
                State state = rootState.clone();
                try {
                    state = state.CloneAndRandomize(rootState.getPlayerToMove());
                } catch (CloneNotSupportedException e) {

                    throw new RuntimeException(e);
                }
                MCTSNode node = rootNode.cloneNode();

                while (!state.isTerminal()) {
                    if (node.getUntriedMoves(state).isEmpty()) {
                        // If there are no untried moves, select the best child using UCB
                        node = node.UCBSelectChild(state.getAllMoves(state.getPlayerToMove()), 0.7);
                        state.applyMove(node.getMove(), state.getPlayerToMove());
                    } else {
                        // If there are untried moves, randomly select one, apply it, and expand the node
                        List<Move> untriedMoves = node.getUntriedMoves(state);
                        Move randomMove = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
                        state.applyMove(randomMove, state.getPlayerToMove());

                        node = node.addChild(randomMove, state.getPlayerToMove());
                    }
                }

                while (!state.getAllMoves(state.getPlayerToMove()).isEmpty() && !state.isTerminal()) {
                    List<Move> legalMoves = state.getAllMoves(state.getPlayerToMove());
                    Move randomMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
                    state.applyMove(randomMove, state.getPlayerToMove());
                    boolean simulationResult = state.getResult(state.getPlayerToMove());
                    node.update(simulationResult);

                }

                while (node != null) {
                    node.update(state.getResult(node.getPlayerJustMoved()));
                    node = node.getParent();

                }
            }

            if (!rootNode.getChildren().isEmpty()) {
                return Collections.max(rootNode.getChildren(), Comparator.comparingInt(MCTSNode::getVisits)).getMove();
            } else {

                return null;
            }
        }

        @Override
        protected void onPostExecute(Move maxMove) {
            if (maxMove != null) {
                applyBest(maxMove, hand, dropButton, game, deck, stack);
            }
        }
    }

    private void expandNode(MCTSNode node, State state) {
        List<Move> untriedMoves = new ArrayList<>(node.getUntriedMoves(state));
        if (!untriedMoves.isEmpty()) {
            Move randomMove = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
            State newState = state.clone();
            newState.applyMove(randomMove, newState.getPlayerToMove());
            node.addChild(randomMove, newState.getPlayerToMove());
            untriedMoves.remove(randomMove);
        }
    }

    public void applyBest(Move maxMove, List<ImageView> hand, Button dropButton, Game game, ImageView deck, ImageView stack) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                String source = maxMove.getSource();
                List<Integer> cards = maxMove.getCardsPlayed();

                for (ImageView imageView : hand) {
                    int tagValue = (Integer) imageView.getTag();
                    if (cards.contains(tagValue)) {
                        imageView.performClick();


                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dropButton.performClick();
                    }
                }, 1000);

                if (Objects.equals(source, "deck")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deck.performClick();
                        }
                    }, 2000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stack.performClick();


                        }
                    }, 1000);
                }
            }
        });
    }

    public void show( List<ImageView> myCards, Game game, Button show) {
        int val = 0;
        for (ImageView imageView : myCards) {
            int tag = (Integer) imageView.getTag();
            val += tag % 100;
        }

        if (val < 6) {
            System.out.println("lesser than 6 can show ");
            show.performClick();
        }
        int[] handSums = new int[4]; // Create an array to store hand sums

        int index = 0;

        for (Map.Entry<Integer, List<Integer>> entry : game.playerHand.entrySet()) {
            System.out.println(" enter");
            List<Integer> hand = entry.getValue();
            // Calculate sum of the player's hand
            int sum = 0;
            for (Integer card : hand) {
                System.out.println("add");
                sum += card % 100;
            }

            // Store the sum in the array
            handSums[index] = sum;
            index++;

        }
        System.out.println("Hand sums:");
        for (int sum : handSums) {
            System.out.println(sum);
        }
    }
}
