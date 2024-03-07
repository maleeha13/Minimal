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
                    List<Integer> card = state.updateUnseenCards();
//                    System.out.println("is it terminal " + state.isTerminal() + " size is "  + card.size());
                    state = state.CloneAndRandomize(rootState.getPlayerToMove());
                } catch (CloneNotSupportedException e) {

                    throw new RuntimeException(e);
                }
                MCTSNode node = rootNode.cloneNode();

                while (!state.getAllMoves(state.getPlayerToMove()).isEmpty() && node.getUntriedMoves(state).isEmpty() && !state.isTerminal()) {
                    node = node.UCBSelectChild(state.getAllMoves(state.getPlayerToMove()), 0.8);

//                    System.out.println("moves 1 " + state.updateUnseenCards().size());
                    state.applyMove(node.getMove(), state.getPlayerToMove());
//
                }

                List<Move> untriedMoves = node.getUntriedMoves(state);
                if (!untriedMoves.isEmpty() && !state.isTerminal()) {  // if we can expand (i.e. state/node is non-terminal)
                    Move m = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
                    int player = state.getPlayerToMove();
//                    System.out.println("moves 2 " + state.updateUnseenCards().size());

                    state.applyMove(m, player);
                    node = node.addChild(m, player);  // add child and descend tree
                }

                while (!state.getAllMoves(state.getPlayerToMove()).isEmpty() && !state.isTerminal()) {

                    List<Move> legalMoves = state.getAllMoves(state.getPlayerToMove());
                    Move randomMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
//                    System.out.println("moves 3 " + state.updateUnseenCards().size());

                    state.applyMove(randomMove, state.getPlayerToMove());
                    boolean simulationResult = state.getResult(state.getPlayerToMove());
                    node.update(state);

                }

                while (node != null) {
                    node.update(state);
                    node = node.getParent();

                }
            }

            if (!rootNode.getChildren().isEmpty()) {
                System.out.println("Move by ISMCTS: " + Collections.max(rootNode.getChildren(), Comparator.comparingInt(MCTSNode::getWins)).getMove());
//                return Collections.min(rootNode.getChildren(), Comparator.comparingDouble(MCTSNode::getScore)).getMove();

                return Collections.max(rootNode.getChildren(), Comparator.comparingInt(MCTSNode::getWins)).getMove();
            } else {
                System.out.println("move is " + rootNode.getMove());
                return rootNode.getMove();
            }
        }

        @Override
        protected void onPostExecute(Move maxMove) {
            if (maxMove != null) {
                applyBest(maxMove, hand, dropButton, game, deck, stack);
            }
            else{
                System.out.println("is null ");
            }
        }
    }

    private void expandNode(MCTSNode node, State state) {
        List<Move> untriedMoves = new ArrayList<>(node.getUntriedMoves(state));
//        System.out.println("untried moves " + untriedMoves.isEmpty() + " terminal " + state.isTerminal());
        if (!untriedMoves.isEmpty()) {
            Move randomMove = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
            State newState = state.clone();
//            System.out.println("moves 4 " + state.updateUnseenCards().size());

            newState.applyMove(randomMove, newState.getPlayerToMove());
            node.addChild(randomMove, newState.getPlayerToMove());
//            System.out.println("added chikd to root");
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
                }, 500);

                if (Objects.equals(source, "deck")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deck.performClick();
                        }
                    }, 500);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stack.performClick();


                        }
                    }, 500);
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
            show.performClick();
        }
        int[] handSums = new int[4]; // Create an array to store hand sums

        int index = 0;

        for (Map.Entry<Integer, List<Integer>> entry : game.playerHand.entrySet()) {
            List<Integer> hand = entry.getValue();
            // Calculate sum of the player's hand
            int sum = 0;
            for (Integer card : hand) {
                sum += card % 100;
            }

            // Store the sum in the array
            handSums[index] = sum;
            index++;

        }

        for (int sum : handSums) {
//            System.out.println(sum);
        }
    }
}