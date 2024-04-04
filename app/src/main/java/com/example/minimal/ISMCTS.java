package com.example.minimal;

import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import android.os.AsyncTask;
import android.os.Looper;

/**
 * This class is responsible for executing the main loop of the ISMCTS
 */
public class ISMCTS {

    /**
     * Executes a Monte Carlo simulation task in the background.
     *
     * @param rootState   The initial state of the game.
     * @param itermax     The maximum number of iterations to run in the Monte Carlo simulation.
     * @param hand        The list of ImageViews representing the player's hand.
     * @param dropButton  The button used for dropping cards from the hand.
     * @param game        The instance of the Game class managing the game state.
     * @param deck        The ImageView representing the deck of cards.
     * @param stack       The ImageView representing the stack or discard pile of cards.
     */
    public void runInBackground(State rootState, int itermax, List<ImageView> hand, Button dropButton, Game game, ImageView deck, ImageView stack) {
        new MonteCarloTask(rootState, itermax, hand, dropButton, game, deck, stack).execute();
    }

    /**
     * AsyncTask subclass for running a Monte Carlo simulation task in the background.
     * This task is responsible for evaluating possible moves in a game using Monte Carlo simulation.
     */
    private class MonteCarloTask extends AsyncTask<Void, Void, Move> {

        /** Initial state of the game */
        private State rootState;

        /** Max number of iterations to run the loop */
        private int itermax;

        /** The list of ImageViews representing the player's hand */
        private List<ImageView> hand;

        /** The button used for dropping cards from the hand */
        private Button dropButton;

        /** The instance of the Game class managing the game state */
        private Game game;

        /** The ImageView representing the deck of cards */
        private ImageView deck;

        /** The ImageView representing the stack or discard pile of cards. */
        private ImageView stack;
        private static final long RANDOM_SEED = 123456;
        private Random random;



        /**
         * Constructs a new MonteCarloTask with the specified parameters.
         *
         * @param rootState   The initial state of the game.
         * @param itermax     The maximum number of iterations to run in the Monte Carlo simulation.
         * @param hand        The list of ImageViews representing the player's hand.
         * @param dropButton  The button used for dropping cards from the hand.
         * @param game        The instance of the Game class managing the game state.
         * @param deck        The ImageView representing the deck of cards.
         * @param stack       The ImageView representing the stack or discard pile of cards.
         */
        public MonteCarloTask(State rootState, int itermax, List<ImageView> hand, Button dropButton, Game game, ImageView deck, ImageView stack) {
            this.rootState = rootState;
            this.itermax = itermax;
            this.hand = hand;
            this.dropButton= dropButton;
            this.game = game;
            this.deck = deck;
            this.stack = stack;
            random = new Random(RANDOM_SEED);
        }


        /**
         * Runs the Monte Carlo Tree Search (MCTS) algorithm in the background to find the best move.
         *
         * @param voids Not used.
         * @return The best move found by the ISMCTS algorithm.
         */
        @Override
        protected Move doInBackground(Void... voids) {
            // Create the root node of the Monte Carlo Tree
            MCTSNode rootNode = new MCTSNode(rootState, null, 0, null);
            expandNode(rootNode, rootState);

            MCTSNode node = null;
            MCTSNode parent = null;
            int x =0;
            // ISMCTS
            for (int i = 0; i < itermax; i++) {
                State state = rootState.clone();
                try {
                    // DETERMINIZATION
                    state.updateUnseenCards();
                    state = state.CloneAndRandomize(rootState.getPlayerToMove(), random);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }

                 node = rootNode.cloneNode();


                // Selection phase: descend the tree until a leaf node is reached
                while (!state.isTerminal() && !node.getChildren().isEmpty()) {
                    node = node.UCBSelectChild(state.getAllMoves(state.getPlayerToMove()), 50);


                    state.applyMove(node.getMove(), state.getPlayerToMove());
                }

                // Expansion phase: expand the leaf node if possible
                List<Move> untriedMoves = node.getUntriedMoves(state, node);
                if (!untriedMoves.isEmpty() && !state.isTerminal()) {
                    Move m = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
                    int player = state.getPlayerToMove();
                    state.applyMove(m, player);

                    node = node.addChild(m, player);
                    untriedMoves = node.getUntriedMoves(state, node);
                }

                // Simulation phase: simulate random moves until the end of the game
                while (!state.getAllMoves(state.getPlayerToMove()).isEmpty() && !state.isTerminal()) {
                    List<Move> legalMoves = state.getAllMoves(state.getPlayerToMove());
                    Move randomMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
                    state.applyMove(randomMove, state.getPlayerToMove());
                    boolean simulationResult = state.getResult(state.getPlayerToMove());
                    node.update(state);
                }


                // Backpropagation phase: update the statistics of nodes in the tree
                while (node != null) {
                    node.update(state);
                    parent=node.cloneNode();

                        node = node.getParent();



                }


            }

            // Return the best move
//            if (!parent.getChildren().isEmpty()) {
//                for (MCTSNode child : parent.getChildren()) {
//                    System.out.println("Move: " + child.getMove() + ", Wins: " + child.getWins() + "vis " + child.getVisits());
//                }
                return Collections.max(rootNode.getChildren(), Comparator.comparingInt(MCTSNode::getWins)).getMove();
//            } else {
//                return rootNode.getMove();
//            }
        }

        /**
         * Executes after the background computation finishes.
         * Applies the best move found by the ISMCTS algorithm to the game UI.
         *
         * @param maxMove The best move found by the ISMCTS algorithm.
         */
        @Override
        protected void onPostExecute(Move maxMove) {
            if (maxMove != null) {
                applyBest(maxMove, hand, dropButton, game, deck, stack);
            }
            else{
                System.out.println("Max move is null ");
            }
        }
    }

    /**
     * Expands the given node by adding a child node corresponding to a randomly chosen untried move in the given state.
     * If there are no untried moves available, the node remains unchanged.
     *
     * @param node  The node to expand.
     * @param state The state of the game associated with the node.
     */
    private void expandNode(MCTSNode node, State state) {
        List<Move> untriedMoves = new ArrayList<>(node.getUntriedMoves(state, node));

        for (Move move : untriedMoves) {
            State newState = state.clone();
            newState.applyMove(move, newState.getPlayerToMove());
            node.addChild(move, newState.getPlayerToMove());

        }
    }


    /**
     * Applies the best move determined by the Monte Carlo simulation to the game UI.
     *
     * @param maxMove    The best move determined by the Monte Carlo simulation.
     * @param hand       The list of ImageViews representing the player's hand.
     * @param dropButton The button used for dropping cards from the hand.
     * @param game       The instance of the Game class managing the game state.
     * @param deck       The ImageView representing the deck of cards.
     * @param stack      The ImageView representing the stack or discard pile of cards.
     */
    public void applyBest(Move maxMove, List<ImageView> hand, Button dropButton, Game game, ImageView deck, ImageView stack) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                String source = maxMove.getSource();
                List<Integer> cards = maxMove.getCardsPlayed();

                // Simulate clicking on the ImageViews corresponding to the cards played in the best move
                for (ImageView imageView : hand) {
                    int tagValue = (Integer) imageView.getTag();
                    if (cards.contains(tagValue)) {

                        imageView.performClick();
//                        System.out.println("dropping : " + tagValue);
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dropButton.performClick();
                    }
                }, 5);

                // Clicks on the deck or stack based on the source of the best move
                if (Objects.equals(source, "deck")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deck.performClick();
//                            System.out.println("picking from deck");
                        }
                    }, 5);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stack.performClick();


                        }
                    }, 5);
                }
            }
        });
    }


    /**
     * Checks if the sum of values of cards in the player's hand is less than 6.
     * If so, simulates a click on the "show" button.
     * Additionally, calculates the sum of values of cards in each player's hand and stores them in an array.
     *
     * @param myCards The list of ImageViews representing the player's hand.
     * @param game    The instance of the Game class managing the game state.
     * @param show    The button used to indicate readiness to reveal cards.
     */
    public void show( List<ImageView> myCards, Game game, Button show) {
        int val = 0;

        // Calculate the sum of values of cards in the player's hand
        for (ImageView imageView : myCards) {
            int tag = (Integer) imageView.getTag();
            val += tag % 100;
        }

        // If the sum is less than 6, simulate a click on the "show" button
        if (val < 6) {
            show.performClick();
        }
    }

}